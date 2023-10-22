package com.dog.shop.config;

import com.dog.shop.errorcode.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        ErrorCode errorCode = (ErrorCode) request.getAttribute("exception");

        if(errorCode == null) {
            setResponse(response, ErrorCode.NON_LOGIN);
        } else if (errorCode.equals(ErrorCode.EXPIRED_TOKEN)){
            setResponseReissue(response, errorCode);
        } else if(errorCode.equals(ErrorCode.WRONG_TYPE_SIGNATURE)) {
            setResponse(response, errorCode);
        } else if(errorCode.equals(ErrorCode.WRONG_TOKEN)) {
            setResponse(response, errorCode);
        } else if(errorCode.equals(ErrorCode.WRONG_ID_TOKEN)) {
            setResponse(response, errorCode);
        }


    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject errorObject = new JSONObject();
        errorObject.put("code", errorCode.getCode());
        errorObject.put("description", errorCode.getDescription());

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", false);
        jsonResponse.put("message", errorCode.getDescription());
        jsonResponse.put("data", JSONObject.NULL); // no data in case of an error
        jsonResponse.put("error", errorObject);

        response.getWriter().println(jsonResponse.toString());
    }

    private void setResponseReissue(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION);

        JSONObject errorObject = new JSONObject();
        errorObject.put("code", errorCode.getCode());
        errorObject.put("description", errorCode.getDescription());


        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", false);
        jsonResponse.put("message", errorCode.getDescription());
        jsonResponse.put("data", JSONObject.NULL); // no data in case of an error
        jsonResponse.put("error", errorObject);

        response.getWriter().println(jsonResponse.toString());
    }

}
