package com.shaun.useraccountauthentication.springsecurityloginserver.web.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserResource {

    protected final Log logger = LogFactory.getLog(this.getClass());

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
            @CookieValue(value = "redirect_url", required = false) String redirectUrl
            , OAuth2AuthenticationToken oAuth2AuthenticationToken
            , HttpSession session
            , HttpServletResponse resp
    ) {
        Map<String, Object> result = new HashMap<>();

        if (null != session.getAttribute("OAUTH2_REFRESH_TOKEN")) {
            result.putIfAbsent("name", session.getAttribute("OAUTH2_REFRESH_TOKEN").toString());
            result.putIfAbsent("refresh_token", session.getAttribute("OAUTH2_REFRESH_TOKEN").toString());

            if (!StringUtils.isEmpty(redirectUrl)) {
                result.putIfAbsent("redirect_url", redirectUrl + "token=" + session.getAttribute("OAUTH2_REFRESH_TOKEN").toString());
                deleteCookie(resp, "redirect_url");
            }
            return result;
        }

        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(),
                        oAuth2AuthenticationToken.getName());

        String tokenValue = client
                .getAccessToken().getTokenValue();

        String userInfoEndpointUri = client.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUri();

        StringBuilder body = new StringBuilder();
        body.append("client_id=" + clientId);
        body.append("&client_secret=" + clientSecret);
        body.append("&grant_type=" + grantType);
        body.append("&id=" + oAuth2AuthenticationToken.getPrincipal().getName());
        body.append("&name=" + oAuth2AuthenticationToken.getPrincipal().getAttribute("name"));
        body.append("&user-info-endpoint-uri=" + userInfoEndpointUri);
        body.append("&authorized-client-registration-id=" + oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
        body.append("&token=" + tokenValue);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap();
        headers.add(HttpHeaders.CONTENT_TYPE, (MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);
        try {
            String response = restTemplate.postForObject(oauth2TokenUrl, request, String.class);
            result = new ObjectMapper().readValue(response, Map.class);

            session.setAttribute("OAUTH2_REFRESH_TOKEN", result.get("refresh_token").toString());

            result.putIfAbsent("name", result.get("refresh_token").toString());
            if (!StringUtils.isEmpty(redirectUrl)) {
                result.putIfAbsent("redirect_url", redirectUrl + "token=" + session.getAttribute("OAUTH2_REFRESH_TOKEN").toString());
                deleteCookie(resp, "redirect_url");
            }
        } catch (Exception e) {

        }
        return result;
    }


    private void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie_redirectUrl = new Cookie(name, null);
        cookie_redirectUrl.setMaxAge(0);
        response.addCookie(cookie_redirectUrl);
    }
}
