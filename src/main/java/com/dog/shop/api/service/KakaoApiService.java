package com.dog.shop.api.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class KakaoApiService {

    private final String API_KEY = "4a99aeb64cf05a81c6ed6e003687f1a6";
    private final String ORIGIN_X = "126.830205192607";
    private final String ORIGIN_Y = "37.5747859514344";

    public String calculateDistance(String address) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "KakaoAK " + API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://dapi.kakao.com/v2/local/search/address.json?query=" + address,
                HttpMethod.GET,
                entity,
                String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());
        JSONArray documents = jsonObject.getJSONArray("documents");
        JSONObject location = documents.getJSONObject(0).getJSONObject("address");

        String x = location.getString("x");
        String y = location.getString("y");

        // Construct the body for the second API call
        String body = "{ \"origin\": { \"x\": \"" + ORIGIN_X + "\", \"y\": \"" + ORIGIN_Y + "\" }, " +
                "\"destinations\": [ { \"x\": \"" + x + "\", \"y\": \"" + y + "\", \"key\": \"0\" } ], " +
                "\"radius\": 10000 }";

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> responseDirection = restTemplate.exchange(
                "https://apis-navi.kakaomobility.com/v1/destinations/directions",
                HttpMethod.POST,
                requestEntity,
                String.class);

          JSONObject responseJson = new JSONObject(responseDirection.getBody());
          JSONArray routes = responseJson.getJSONArray("routes");
          JSONObject summary = routes.getJSONObject(0).getJSONObject("summary");
          int distance = summary.getInt("distance");

          return "Distance: " + distance + " meter";
//        return responseDirection.getBody();
    }
}

