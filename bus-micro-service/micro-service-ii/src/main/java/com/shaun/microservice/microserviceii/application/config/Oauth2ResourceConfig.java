package com.shaun.microservice.microserviceii.application.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@EnableResourceServer
@Configuration
public class Oauth2ResourceConfig extends ResourceServerConfigurerAdapter {

    private final ResourceServerProperties resource;

    public Oauth2ResourceConfig(ResourceServerProperties resource) {
        this.resource = resource;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {

        resources.resourceId(this.resource.getResourceId());

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
