package com.shaun.useraccountauthentication.springsecurityauthorizationserver.config.oauth2.tokengranter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain.service.IRegistrationService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

public class SocialOauth2TokenGranter extends AbstractTokenGranter {
    private static final String GRANT_TYPE = "oauth2";

    private RestTemplate restTemplate;

    private IRegistrationService registrationService;

    private AuthenticationManager authenticationManager;

    public SocialOauth2TokenGranter(
            IRegistrationService registrationService,
            AuthenticationManager authenticationManager,
            AuthorizationServerTokenServices tokenServices,
            ClientDetailsService clientDetailsService,
            OAuth2RequestFactory requestFactory,
            RestTemplate restTemplate) {
        this(tokenServices, clientDetailsService, requestFactory, "oauth2");
        this.restTemplate = restTemplate;
        this.registrationService = registrationService;
        this.authenticationManager = authenticationManager;
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

            logger.info(result);

            Map<String, String> registration = registrationService.registration(
                    name
                    , authorizedClientRegistrationId
                    , id
                    , userInfoEndpointUri
                    , token
            );
            name = registration.get("name");
        } catch (Exception e) {
            logger.error(e);
            throw new InvalidGrantException("Could not authenticate oauth2 :" + e.getMessage());
        }


        Authentication userAuth = new UsernamePasswordAuthenticationToken(name, "admin");
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException ase) {
            //covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            throw new InvalidGrantException(ase.getMessage());
        } catch (BadCredentialsException e) {
            // If the username/password are wrong the spec says we should send 400/invalid grant
            throw new InvalidGrantException(e.getMessage());
        } catch (UsernameNotFoundException e) {
            // If the user is not found, report a generic error message
            throw new InvalidGrantException(e.getMessage());
        }
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate user: " + name);
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}