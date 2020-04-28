package com.shaun.useraccountauthentication.springsecurityresourceserver.config.oauth2;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@EnableResourceServer
@Configuration
public class Oauth2ResourceConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("product_api");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.headers().frameOptions().sameOrigin();

        http.cors().and().csrf().disable();

        ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) http.authorizeRequests()
                .antMatchers("/h2-console/*").permitAll()
                .anyRequest()
        ).authenticated();
    }
}
