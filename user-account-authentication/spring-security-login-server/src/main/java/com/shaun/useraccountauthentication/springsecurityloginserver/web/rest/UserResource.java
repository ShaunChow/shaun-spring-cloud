package com.shaun.useraccountauthentication.springsecurityloginserver.web.rest;


import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class UserResource {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public UserResource(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
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
