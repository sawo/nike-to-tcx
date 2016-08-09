package hu.greencode.nike2tcx.service;

import hu.greencode.nike2tcx.model.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AccessTokenHunter {

    @Autowired
    private RestTemplate restTemplate = new RestTemplate();

    public AccessToken hunt() {
        RestTemplate restTemplate = new RestTemplate();
        String request = "username=sanyi%40inlineskate.hu&password=Fikalacika6";
//        restTemplate.
//        return restTemplate.postForObject("https://developer.nike.com/services/login", request, AccessToken.class);
        return null;
    }
}
