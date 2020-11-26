package com.shaun.useraccountauthentication.springsecurityauthorizationserver.config.oauth2;

import com.shaun.useraccountauthentication.springsecurityauthorizationserver.config.oauth2.tokengranter.social.SocialOauth2TokenGranter;
import com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain.service.IRegistrationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;

@EnableAuthorizationServer
@Configuration
public class Oauth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    private final DataSource dataSource;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    private final IRegistrationService registrationService;
    private final RestTemplate restTemplate;

    public Oauth2AuthorizationConfig(
            DataSource dataSource,
            BCryptPasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            @Qualifier("org.springframework.security.userDetailsService") UserDetailsService userDetailsService,
            RestTemplate restTemplate,
            IRegistrationService registrationService) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.restTemplate = restTemplate;
        this.registrationService = registrationService;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
        // This will enable /oauth/check_token access
        security.checkTokenAccess("isAuthenticated()");

        // This will enable /oauth/token_key access
        security.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')");

        // BCryptPasswordEncoder() is used for oauth_client_details.client_secret
        security.passwordEncoder(passwordEncoder);

        // AllowFormAuthenticationForClients
        security.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

        endpoints.approvalStore(approvalStore())                        // oauth_approvals
                .authorizationCodeServices(authorizationCodeServices()) // oauth_code
                .tokenStore(tokenStore());                              // oauth_access_token & oauth_refresh_token

        endpoints.authenticationManager(authenticationManager);
        endpoints.userDetailsService(userDetailsService);
        endpoints.tokenGranter(tokenGranter(endpoints, restTemplate));

        // Allow HttpMethod.GET
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }

    private TokenGranter tokenGranter(
            AuthorizationServerEndpointsConfigurer endpoints,
            RestTemplate restTemplate) {

        ArrayList<TokenGranter> tokenGranters = new ArrayList<>();
        tokenGranters.add(endpoints.getTokenGranter());
        tokenGranters.add(new SocialOauth2TokenGranter(
                registrationService,
                authenticationManager,
                endpoints.getTokenServices(),
                endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(),
                restTemplate
        ));
        return new CompositeTokenGranter(tokenGranters);
    }

}