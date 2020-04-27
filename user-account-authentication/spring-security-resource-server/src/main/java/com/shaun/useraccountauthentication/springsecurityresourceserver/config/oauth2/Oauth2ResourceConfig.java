package com.shaun.useraccountauthentication.springsecurityresourceserver.config.oauth2;

import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@EnableResourceServer
public class Oauth2ResourceConfig extends ResourceServerConfigurerAdapter {
}
