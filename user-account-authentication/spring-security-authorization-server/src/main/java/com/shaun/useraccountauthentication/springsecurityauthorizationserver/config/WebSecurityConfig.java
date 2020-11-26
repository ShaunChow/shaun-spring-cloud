package com.shaun.useraccountauthentication.springsecurityauthorizationserver.config;


import com.shaun.useraccountauthentication.springsecurityauthorizationserver.config.oauth2.tokengranter.social.SocialOauth2AuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final BCryptPasswordEncoder passwordEncoder;

    public WebSecurityConfig(DataSource dataSource, BCryptPasswordEncoder passwordEncoder) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> cfg
                = auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder)
                .withUser(
                        User.withUsername("admin").password(passwordEncoder.encode("admin")).roles("ADMIN")
                );   // default Super User

        cfg.getUserDetailsService().setEnableGroups(true);  // enable groups
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().frameOptions().sameOrigin();

        http.cors().and().csrf().disable();

        http.requestMatchers()
                .antMatchers("/login", "/oauth/authorize")
                .and().authorizeRequests().anyRequest().authenticated()
                .and().formLogin().permitAll();

        SocialOauth2AuthenticationProvider socialOauth2AuthenticationProvider = new SocialOauth2AuthenticationProvider();
        socialOauth2AuthenticationProvider.setUserDetailsService(super.userDetailsServiceBean());
        http.authenticationProvider(socialOauth2AuthenticationProvider);
    }

    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    @Bean(BeanIds.USER_DETAILS_SERVICE)
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", config);
        return corsConfigurationSource;
    }
}
