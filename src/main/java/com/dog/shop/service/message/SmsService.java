package com.dog.shop.service.message;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.dog.shop.dto.messageDto.MessageReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class SmsService {
   private static String hostNameUrl = "https://sens.apigw.ntruss.com";
//   private static String requestUrl = "/push/v2/services/";
//   private static String requestUrlType = "/messages";
   private static String accessKey = "RqaihL7bel8o2NZwp2f4";
   private static String secretKey = "p3NffSluYY9Bghn4dCw4v1b5LrrCZFBVNA1XWBha";
   private static String serviceId = "ncp:sms:kr:295848412686:lgucamp";


   public Map<String, Object> sendMessageToPhone(MessageReqDTO messageReqDTO) {

      String phone = messageReqDTO.getPhone(); // 보내줘야 하는 phoneNum

      Map<String, Object> requestMap = new HashMap<>();
      requestMap.put("type", "SMS");
      requestMap.put("contentType", "COMM");
      requestMap.put("countryCode", "82");
      requestMap.put("from", "01046057518");
      requestMap.put("subject", "subject");

      // 인증번호 생성
      Random random = new Random();
      int randomNumber = random.nextInt(999999); // 0부터 999999 사이의 난수를 생성합니다.
      String authNumber = String.format("%06d", randomNumber); // 6자리 숫자로 변환합니다.

      StringBuilder sb = new StringBuilder();
      sb.append("[D@g] | 본인확인 인증번호 [");
      sb.append(authNumber); // generateRandomNumber() 메소드는 랜덤번호를 생성하는 메소드입니다.
      sb.append("]를 화면에 입력해주세요.");
      String ment = sb.toString();

      requestMap.put("content", ment);

      List<Map<String, String>> messages = new ArrayList<>();
      Map<String, String> message = new HashMap<>();
      message.put("to", phone);
      messages.add(message);
      requestMap.put("messages", messages);

      Map<String, Object> resultMap = null;

      Gson gson = new Gson();
      String json = gson.toJson(requestMap);
      String uri = "/sms/v2/services/" + serviceId + "/messages";
      resultMap = sendPushInfo(uri, "POST", json);

      return resultMap;
   }

   private static Map<String, Object> sendPushInfo(String uri, String method, String body) {
      Map<String, Object> resultMap = null;
      String timestamp = Long.toString(System.currentTimeMillis());
      String apiUrl = hostNameUrl + uri;
      // String apiUrl = uri;

      HttpURLConnection con = null;
      try {
         URL url = new URL(apiUrl);
         con = (HttpURLConnection) url.openConnection();
         con.setUseCaches(false);
         con.setDoInput(true);
         con.setDoOutput(true);
         con.setRequestProperty("content-type", "application/json");
         con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
         con.setRequestProperty("x-ncp-iam-access-key", accessKey);
         con.setRequestProperty("x-ncp-apigw-signature-v2",
                 makeSignature(uri, timestamp, method, accessKey, secretKey));
         con.setRequestMethod(method);

         if (body != null && !body.isEmpty() && ("POST".equals(method) || "PUT".equals(method))) {
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());

            wr.write(body.getBytes());
            wr.flush();
            wr.close();
         }

         int responseCode = con.getResponseCode();
         System.out.println(responseCode);
         BufferedReader br = null;
         if (responseCode == 200 || responseCode == 202) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
         } else if (responseCode == 201 || responseCode == 204) {

         } else {
            System.out.println("에러 페이지");
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
         }

         String inputLine = null;
         System.out.println("여기까지는 오고 있니??");
         resultMap = new HashMap<String, Object>();
         if (br != null) {
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
               response.append(inputLine);
               System.out.println(response.toString());
            }
            br.close();

         } else {
            // TODO 예외 처리
         }

         resultMap.put("reponseCode", responseCode);

      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         if (con != null) {
            con.disconnect();
         }
      }

      return resultMap;

   }

   private static String makeSignature(String url, String timestamp, String method, String accessKey, String secretKey)
           throws Exception {
      String space = " ";
      String newLine = "\n";

      String message = new StringBuilder().append(method).append(space).append(url).append(newLine).append(timestamp)
              .append(newLine).append(accessKey).toString();

      SecretKeySpec signingKey = null;
      String encodeBase64String = null;

      try {
         signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
         Mac mac = Mac.getInstance("HmacSHA256");
         mac.init(signingKey);
         byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
         encodeBase64String = Base64.encodeBase64String(rawHmac);
      } catch (Exception e) {
         encodeBase64String = e.toString();
      }

      return encodeBase64String;
   }

//   public Map<String, Object> sendSms(PhoneDataDto phoneDataDto) {
//
//      Map<String, Object> requestMap = new HashMap<>();
//      requestMap.put("type", "SMS");
//      requestMap.put("contentType", "COMM");
//      requestMap.put("countryCode", "82");
//      requestMap.put("from", "01046057518");
//      requestMap.put("subject", "subject");
//
//      // 인증번호 생성
//      Random random = new Random();
//      int randomNumber = random.nextInt(999999); // 0부터 999999 사이의 난수를 생성합니다.
//      String authNumber = String.format("%06d", randomNumber); // 6자리 숫자로 변환합니다.
//
//      StringBuilder sb = new StringBuilder();
//      sb.append("[D@g] | 본인확인 인증번호 [");
//      sb.append(authNumber); // generateRandomNumber() 메소드는 랜덤번호를 생성하는 메소드입니다.
//      sb.append("]를 화면에 입력해주세요.");
//      String ment = sb.toString();
//
//      requestMap.put("content", ment);
//      String phoneNumber = phoneDataDto.getPhoneNumber();
//      this.saveAuthNumber(authNumber);
//
//      // 현재 시간 저장
//      long currentTime = System.currentTimeMillis();
//
//      // session.setAttribute("authNumber", authNumber);
//      // session.setAttribute("authNumberCreateTime", currentTime);
//
//      List<Map<String, String>> messages = new ArrayList<>();
//      Map<String, String> message = new HashMap<>();
//      message.put("to", phoneDataDto.getPhoneNumber());
//      messages.add(message);
//      requestMap.put("messages", messages);
//
//      Map<String, Object> resultMap = null;
//
//      Gson gson = new Gson();
//      String json = gson.toJson(requestMap);
//      String uri = "/sms/v2/services/" + serviceId + "/messages";
//      resultMap = sendPushInfo(uri, "POST", json);
//
//      return resultMap;
//   }
}