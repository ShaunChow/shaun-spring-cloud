package com.shaun.useraccountauthentication.springsecurityauthorizationserver.config.oauth2.tokengranter;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class GithubLoginTokenGranter extends AbstractTokenGranter {
    private static final String GRANT_TYPE = "github_login";

    public GithubLoginTokenGranter(
            AuthorizationServerTokenServices tokenServices,
            ClientDetailsService clientDetailsService,
            OAuth2RequestFactory requestFactory) {
        this(tokenServices, clientDetailsService, requestFactory, "github_login");
    }

    protected GithubLoginTokenGranter(
            AuthorizationServerTokenServices tokenServices,
            ClientDetailsService clientDetailsService,
            OAuth2RequestFactory requestFactory,
            String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
    }

    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap(tokenRequest.getRequestParameters());

        String registrationkey = (String) parameters.get("registration_key");
        parameters.remove("registration_key");

        Authentication userAuth = new UsernamePasswordAuthenticationToken(
                "admin",
                "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_GITHUB_READ")));
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);


        if (userAuth != null && userAuth.isAuthenticated()) {
            OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
            return new OAuth2Authentication(storedOAuth2Request, userAuth);
        } else {
            throw new InvalidGrantException("Could not authenticate github_login ");
        }
    }
}