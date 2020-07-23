package com.shaun.useraccountauthentication.springsecurityauthorizationserver.config.oauth2.tokengranter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class SocialOauth2TokenGranter extends AbstractTokenGranter {
    private static final String GRANT_TYPE = "oauth2";

    private RestTemplate restTemplate;

    public SocialOauth2TokenGranter(
            AuthorizationServerTokenServices tokenServices,
            ClientDetailsService clientDetailsService,
            OAuth2RequestFactory requestFactory,
            RestTemplate restTemplate) {
        this(tokenServices, clientDetailsService, requestFactory, "oauth2");
        this.restTemplate = restTemplate;
    }

    protected SocialOauth2TokenGranter(
            AuthorizationServerTokenServices tokenServices,
            ClientDetailsService clientDetailsService,
            OAuth2RequestFactory requestFactory,
            String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
    }

    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap(tokenRequest.getRequestParameters());

        String id = parameters.get("id");
        String name = parameters.get("name");
        String userInfoEndpointUri = parameters.get("user-info-endpoint-uri");
        String authorizedClientRegistrationId = parameters.get("authorized-client-registration-id");
        String token = parameters.get("token");

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            HttpEntity<Map> request = new HttpEntity<>(null, headers);
            String response = restTemplate.exchange(userInfoEndpointUri, HttpMethod.GET, request, String.class).getBody();
            Map<String, Object> result = new ObjectMapper().readValue(response, Map.class);
        } catch (Exception e) {
            throw new InvalidGrantException("Could not authenticate oauth2 :" + e.getMessage());
        }

        Authentication userAuth = new UsernamePasswordAuthenticationToken(
                name,
                "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_OAUTH2_READ")));
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);

        if (userAuth != null && userAuth.isAuthenticated()) {
            OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(storedOAuth2Request, userAuth);
        } else {
            throw new InvalidGrantException("Could not authenticate oauth2 ");
        }
    }
}