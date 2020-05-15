package com.shaun.useraccountauthentication.springsecurityauthorizationserver.config.oauth2;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.client.token.JdbcClientTokenServices;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import javax.sql.DataSource;
import java.util.Arrays;

@EnableOAuth2Client
@Configuration
public class Oauth2ClientConfig {


    private final DataSource dataSource;

    public Oauth2ClientConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public ClientTokenServices clientTokenServices() {
        return new JdbcClientTokenServices(dataSource);
    }

    @ConfigurationProperties(prefix = "spring.security.oauth2.user-center")
    @Bean
    public OAuth2ProtectedResourceDetails userCenterClientCredsDetails() {
        return new ClientCredentialsResourceDetails();
    }

    @Bean("user-center-client-creds-restemplate")
    public OAuth2RestTemplate userCenterClientCredsRestTemplate(
            OAuth2ProtectedResourceDetails resourceDetails,
            ClientTokenServices clientTokenServices) {

        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails);

        AccessTokenProviderChain accessTokenProviderChain
                = new AccessTokenProviderChain(
                Arrays.asList(
                        new AuthorizationCodeAccessTokenProvider(),
                        new ImplicitAccessTokenProvider(),
                        new ResourceOwnerPasswordAccessTokenProvider(),
                        new ClientCredentialsAccessTokenProvider()
                )
        );
        accessTokenProviderChain.setClientTokenServices(clientTokenServices);

        oAuth2RestTemplate.setAccessTokenProvider(accessTokenProviderChain);
        return oAuth2RestTemplate;
    }

}