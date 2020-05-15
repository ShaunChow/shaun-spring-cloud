package com.shaun.useraccountauthentication.springsecurityloginserver.web.rest;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@RestController
public class UserResource {

    @Value("${spring.security.oauth2.authorization.token-url}")
    private String oauth2TokenUrl;

    @Value("${spring.security.oauth2.authorization.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.authorization.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.authorization.grant-type}")
    private String grantType;

    private final OAuth2AuthorizedClientService authorizedClientService;

    private final RestTemplate restTemplate;

    public UserResource(OAuth2AuthorizedClientService authorizedClientService, RestTemplate restTemplate) {
        this.authorizedClientService = authorizedClientService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/user")
    public Map<String, Object> user(
            OAuth2AuthenticationToken oAuth2AuthenticationToken) {

        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(),
                        oAuth2AuthenticationToken.getName());

        String tokenValue = client
                .getAccessToken().getTokenValue();

        String userInfoEndpointUri = client.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUri();

        return Collections.singletonMap("name",
                "{"
                        + "id:" + oAuth2AuthenticationToken.getPrincipal().getName() + ","
                        + "name:" + oAuth2AuthenticationToken.getPrincipal().getAttribute("name") + ","
                        + "token:" + tokenValue + ","
                        + "user-info-endpoint-uri:" + userInfoEndpointUri + ","
                        + "authorized-client-registration-id:" + oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
                        + "}"
        );
    }
}
