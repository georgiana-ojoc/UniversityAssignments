package gomoku.communication;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class Communication {
    private final static String restApiUrl = "http://localhost:8080/api/v1/";

    public static ResponseEntity<String> post(String url, String body, String jwt) {
        return request(url, HttpMethod.POST, body, jwt);
    }

    public static ResponseEntity<String> put(String url, String body, String jwt) {
        return request(url, HttpMethod.PUT, body, jwt);
    }

    public static ResponseEntity<String> request(String url, HttpMethod httpMethod, String body, String jwt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (jwt != null) {
            httpHeaders.set("Authorization", "Bearer " + jwt);
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(body, httpHeaders);
        return restTemplate.exchange(restApiUrl + url, httpMethod, httpEntity, String.class);
    }
}
