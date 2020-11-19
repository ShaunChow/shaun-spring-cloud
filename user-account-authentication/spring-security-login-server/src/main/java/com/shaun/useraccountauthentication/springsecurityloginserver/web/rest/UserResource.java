package com.shaun.useraccountauthentication.springsecurityloginserver.web.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
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
    ) throws JsonProcessingException {

        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(),
                        oAuth2AuthenticationToken.getName());

        String tokenValue = client
                .getAccessToken().getTokenValue();

        StringBuilder body = new StringBuilder();

        if (null != session.getAttribute("OAUTH2_REFRESH_TOKEN")) {
            body.append("client_id=" + clientId);
            body.append("&client_secret=" + clientSecret);
            body.append("&grant_type=refresh_token");
            body.append("&refresh_token=" + session.getAttribute("OAUTH2_REFRESH_TOKEN").toString());
            body.append("&token=" + tokenValue);
            return validate(redirectUrl, body, session, resp);
        }

        String userInfoEndpointUri = client.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUri();

        body.append("client_id=" + clientId);
        body.append("&client_secret=" + clientSecret);
        body.append("&grant_type=" + grantType);
        body.append("&id=" + oAuth2AuthenticationToken.getPrincipal().getName());
        body.append("&name=" + oAuth2AuthenticationToken.getPrincipal().getAttribute("name"));
        body.append("&user-info-endpoint-uri=" + userInfoEndpointUri);
        body.append("&authorized-client-registration-id=" + oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
        body.append("&token=" + tokenValue);
        return validate(redirectUrl, body, session, resp);
    }


    private Map<String, Object> validate(
            String redirectUrl
            , StringBuilder body
            , HttpSession session
            , HttpServletResponse resp
    ) throws JsonProcessingException {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap();
        headers.add(HttpHeaders.CONTENT_TYPE, (MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);

        String response = restTemplate.postForObject(oauth2TokenUrl, request, String.class);
        Map<String, Object> result = new ObjectMapper().readValue(response, Map.class);

        session.setAttribute("OAUTH2_REFRESH_TOKEN", result.get("refresh_token").toString());

        if (!StringUtils.isEmpty(redirectUrl)) {
            result.putIfAbsent(
                    "redirect_url"
                    , redirectUrl
                            + "token=" + result.get("access_token").toString()
                            + "&expires_in=" + result.get("expires_in").toString()
            );
            deleteCookie(resp, "redirect_url");
        }
        return result;
    }

    private void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie_redirectUrl = new Cookie(name, null);
        cookie_redirectUrl.setMaxAge(0);
        response.addCookie(cookie_redirectUrl);
    }
}
