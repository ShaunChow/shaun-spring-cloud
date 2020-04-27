package com.shaun.useraccountauthentication.springsecurityresourceserver.config;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * Allows for @PreAuthorize annotation processing.
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class GlobalSecurityConfig extends GlobalMethodSecurityConfiguration {

}
