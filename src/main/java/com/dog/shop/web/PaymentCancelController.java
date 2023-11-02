package com.dog.shop.web;

import com.dog.shop.domain.Order;
import com.dog.shop.domain.Payment;
import com.dog.shop.domain.PaymentCancellation;
import com.dog.shop.repository.order.OrderRepository;
import com.dog.shop.repository.payment.PaymentCancellationRepository;
import com.dog.shop.repository.payment.PaymentRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Hex;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

@Controller
public class PaymentCancelController {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentCancellationRepository paymentCancellationRepository;


    @GetMapping("/payment/cancel/{orderId}")
    public void nicePayCancel(@PathVariable Long orderId) {

        // TID하고 CancelAmt를 찾아와서 넣어야함!
        // TODO 에러처리 필요
        Order order = orderRepository.findById(orderId).orElseThrow();
        Payment payment = order.getPayment();

        int AMT = payment.getAmt();
        String TID = payment.getTid();
        String partialCancelCode = "1";

        // URL 설정

        // HTTP 연결 생성

        // 응답 코드 확인
        int responseCode = 0;
        HttpURLConnection conn = null;
        try {
        URL url = new URL("http://localhost:8080/nicepay/cancel");
            conn = (HttpURLConnection) url.openConnection();
        // POST 방식으로 요청 설정
        conn.setRequestMethod("POST");
            // 출력 스트림 사용 설정
            conn.setDoOutput(true);

            // 파라미터 설정
            String params = "TID=" + TID + "&CancelAmt=" + URLEncoder.encode(String.valueOf(AMT), "UTF-8")
                    + "&Moid=" + URLEncoder.encode(String.valueOf(orderId), "UTF-8");
            params += "&PartialCancelCode=" + URLEncoder.encode(partialCancelCode, "UTF-8");

            // 출력 스트림으로 파라미터 전송
            OutputStream os = conn.getOutputStream();
            os.write(params.getBytes(StandardCharsets.UTF_8));


            responseCode = conn.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }




//        return "redirect:/";
    }





    @RequestMapping(value = "/nicepay/cancel", method = RequestMethod.POST)
    @ResponseBody
    public void test4(HttpServletRequest request) throws Exception {

        request.setCharacterEncoding("utf-8");

        /*
         ****************************************************************************************
         * <취소요청 파라미터> 취소시 전달하는 파라미터입니다. 샘플페이지에서는 기본(필수) 파라미터만 예시되어 있으며, 추가 가능한 옵션 파라미터는
         * 연동메뉴얼을 참고하세요.
         ****************************************************************************************
         */
        String tid = (String) request.getParameter("TID"); // 거래 ID
        String cancelAmt = (String) request.getParameter("CancelAmt"); // 취소금액
        // String partialCancelCode = (String)request.getParameter("PartialCancelCode");
        // 여부 전체취소 : 0 / 부분취소 : 1
        String partialCancelCode = (String) request.getParameter("partialCancelCode");; // TODO 부분 취소인지 확인해야함
        String mid = "nicepay00m"; // 상점 ID
        String moid = (String) request.getParameter("Moid"); // 주문번호
        String cancelMsg = "고객요청"; // 취소사유

        /*
         ****************************************************************************************
         * <해쉬암호화> (수정하지 마세요) SHA-256 해쉬암호화는 거래 위변조를 막기위한 방법입니다.
         ****************************************************************************************
         */
        DataEncrypt sha256Enc = new DataEncrypt();
        String merchantKey = "EYzu8jGGMfqaDEp76gSckuvnaHHu+bC4opsSN6lHv3b2lurNYkVXrZ7Z1AoqQnXI3eLuaUFyoRNC6FkrzVjceg=="; // 상점키
        String ediDate = getyyyyMMddHHmmss();
        String signData = sha256Enc.encrypt(mid + cancelAmt + ediDate + merchantKey);

        /*
         ****************************************************************************************
         * <취소 요청> 취소에 필요한 데이터 생성 후 server to server 통신을 통해 취소 처리 합니다. 취소 사유(CancelMsg)
         * 와 같이 한글 텍스트가 필요한 파라미터는 euc-kr encoding 처리가 필요합니다.
         ****************************************************************************************
         */
        StringBuffer requestData = new StringBuffer();
        requestData.append("TID=").append(tid).append("&");
        requestData.append("MID=").append(mid).append("&");
        requestData.append("Moid=").append(moid).append("&");
        requestData.append("CancelAmt=").append(cancelAmt).append("&");
        requestData.append("CancelMsg=").append(URLEncoder.encode(cancelMsg, "euc-kr")).append("&");
        requestData.append("PartialCancelCode=").append(partialCancelCode).append("&");
        requestData.append("EdiDate=").append(ediDate).append("&");
        requestData.append("CharSet=").append("utf-8").append("&");
        requestData.append("SignData=").append(signData);
        String resultJsonStr = connectToServer(requestData.toString(),
                "https://webapi.nicepay.co.kr/webapi/cancel_process.jsp");

        /*
         ****************************************************************************************
         * <취소 결과 파라미터 정의> 샘플페이지에서는 취소 결과 파라미터 중 일부만 예시되어 있으며, 추가적으로 사용하실 파라미터는 연동메뉴얼을
         * 참고하세요.
         ****************************************************************************************
         */
        String ResultCode = "";
        String ResultMsg = "";
        String CancelAmt = "";
        String CancelDate = "";
        String CancelTime = "";
        String TID = "";
        String Signature = "";

        /*
         ****************************************************************************************
         * Signature : 요청 데이터에 대한 무결성 검증을 위해 전달하는 파라미터로 허위 결제 요청 등 결제 및 보안 관련 이슈가 발생할 만한
         * 요소를 방지하기 위해 연동 시 사용하시기 바라며 위변조 검증 미사용으로 인해 발생하는 이슈는 당사의 책임이 없음 참고하시기 바랍니다.
         ****************************************************************************************
         */
        // String Signature = ""; String cancelSignature = "";

        if ("9999".equals(resultJsonStr)) {
            ResultCode = "9999";
            ResultMsg = "통신실패";
        } else {
            HashMap resultData = jsonStringToHashMap(resultJsonStr);
            ResultCode = (String) resultData.get("ResultCode"); // 결과코드 (취소성공: 2001, 취소성공(LGU 계좌이체):2211)
            ResultMsg = (String) resultData.get("ResultMsg"); // 결과메시지
            CancelAmt = (String) resultData.get("CancelAmt"); // 취소금액
            CancelDate = (String) resultData.get("CancelDate"); // 취소일
            CancelTime = (String) resultData.get("CancelTime"); // 취소시간
            TID = (String) resultData.get("TID"); // 거래아이디 TID

            System.out.println("결과코드 (ResultCode): " + ResultCode);
            System.out.println("결과메시지 (ResultMsg): " + ResultMsg);
            System.out.println("취소금액 (CancelAmt): " + CancelAmt);
            System.out.println("취소일 (CancelDate): " + CancelDate);
            System.out.println("취소시간 (CancelTime): " + CancelTime);
            System.out.println("거래아이디 TID (TID): " + TID);

            if ("2010".equals(ResultCode)) {
                // TODO 에러처리 필요!
            }


            // Signature = (String)resultData.get("Signature");
            // cancelSignature = sha256Enc.encrypt(TID + mid + CancelAmt + merchantKey);

            // 1. TID를 사용하여 기존의 TestPayment 엔터티를 찾습니다.

            // TODO 에러처리 필요
            Payment existingPayment = paymentRepository.findByTid(TID)
                    .orElseThrow();

            // 2. 새로운 PaymentCancellation 객체를 생성하고, 해당 객체에 취소 데이터를 설정합니다.

            PaymentCancellation cancellation = new PaymentCancellation();
            cancellation.setCancelResultCode(ResultCode);
            cancellation.setCancelResultMsg(ResultMsg);
            cancellation.setCancelAmt(Integer.parseInt(CancelAmt)); // String을 int로 변환
            cancellation.setPayment(existingPayment); // 관계 설정
            // 취소일과 취소시간을 합쳐서 LocalDateTime 객체를 생성합니다.
            if (!"00000000".equals(CancelDate) && !"000000".equals(CancelTime)) {
                String combinedDateTimeStr = CancelDate.substring(0, 4) + "-" + CancelDate.substring(4, 6) + "-"
                        + CancelDate.substring(6) + "T" + new StringBuilder(CancelTime).insert(2, ":").insert(5, ":");
                LocalDateTime cancelDateTime = LocalDateTime.parse(combinedDateTimeStr);
                cancellation.setCancelDateTime(cancelDateTime); // 합친 날짜와 시간을 설정

                // 3. TestPayment 객체와 PaymentCancellation 객체 간의 관계를 설정합니다.
                existingPayment.getPaymentCancellationList().add(cancellation);

                // 1. moid (reservationId)를 사용하여 Reservation 엔터티를 찾습니다.
                Long orderId = Long.parseLong(moid);
                Order order = orderRepository.findById(orderId).orElseThrow();

                // 2. Reservation 엔터티의 상태를 '취소'로 변경합니다.
                order.setStatus("Cancel"); // 예: 상태를 나타내는 enum이나 상수를 사용하는
                // 경우
//				reservation.setStatus(ReservationStatus.CANCELLED); // 예: 상태를 나타내는 enum이나 상수를 사용하는 경우

                // 3. 변경된 Reservation 엔터티를 저장합니다.
                orderRepository.save(order);
                // 변환된 LocalDateTime 객체 사용
            } else {
                // '00000000' 또는 '000000' 값에 대한 처리 로직
                // TODO 이때는 취소가 안되는경우 에러 처리
                // throw new CommonException(ErrorCode.)
            }

            // 4. PaymentCancellation 객체를 저장합니다.
            paymentCancellationRepository.save(cancellation);

        }


    }


    public class DataEncrypt {
        MessageDigest md;
        String strSRCData = "";
        String strENCData = "";
        String strOUTData = "";

        public DataEncrypt() {
        }

        public String encrypt(String strData) {
            String passACL = null;
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA-256");
                md.reset();
                md.update(strData.getBytes());
                byte[] raw = md.digest();
                passACL = encodeHex(raw);
            } catch (Exception e) {
                System.out.print("암호화 에러" + e.toString());
            }
            return passACL;
        }

        public String encodeHex(byte[] b) {
            char[] c = Hex.encodeHex(b);
            return new String(c);
        }
    }


    private static HashMap jsonStringToHashMap(String str) throws Exception {
        HashMap dataMap = new HashMap();
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(str);
            JSONObject jsonObject = (JSONObject) obj;

            Iterator<String> keyStr = jsonObject.keySet().iterator();
            while (keyStr.hasNext()) {
                String key = keyStr.next();
                Object value = jsonObject.get(key);

                dataMap.put(key, value);
            }
        } catch (Exception e) {

        }
        return dataMap;
    }


    public String connectToServer(String data, String reqUrl) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader resultReader = null;
        PrintWriter pw = null;
        URL url = null;

        int statusCode = 0;
        StringBuffer recvBuffer = new StringBuffer();
        try {
            url = new URL(reqUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(25000);
            conn.setDoOutput(true);

            pw = new PrintWriter(conn.getOutputStream());
            pw.write(data);
            pw.flush();

            statusCode = conn.getResponseCode();
            resultReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            for (String temp; (temp = resultReader.readLine()) != null;) {
                recvBuffer.append(temp).append("\n");
            }

            if (!(statusCode == HttpURLConnection.HTTP_OK)) {
                throw new Exception();
            }

            return recvBuffer.toString().trim();
        } catch (Exception e) {
            return "9999";
        } finally {
            recvBuffer.setLength(0);

            try {
                if (resultReader != null) {
                    resultReader.close();
                }
            } catch (Exception ex) {
                resultReader = null;
            }

            try {
                if (pw != null) {
                    pw.close();
                }
            } catch (Exception ex) {
                pw = null;
            }

            try {
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception ex) {
                conn = null;
            }
        }
    }


    private String getyyyyMMddHHmmss() {
        SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
        return yyyyMMddHHmmss.format(new Date());
    }

}
