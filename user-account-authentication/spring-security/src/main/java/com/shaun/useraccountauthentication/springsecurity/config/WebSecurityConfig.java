package com.shaun.useraccountauthentication.springsecurity.config;


import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY =
            "select g.id, g.group_name, ga.authority " +
                    "from groups g, group_members gm, group_authorities ga " +
                    "where gm.username = ? " +
                    "and g.id = ga.group_id " +
                    "and g.id = gm.group_id";

    private final DataSource dataSource;

    private final BCryptPasswordEncoder passwordEncoder;

    public WebSecurityConfig(DataSource dataSource, BCryptPasswordEncoder passwordEncoder) {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder)
                .withUser(
                        User.withUsername("admin").password(passwordEncoder.encode("admin")).roles("ADMIN")
                )   // default Super User
                .groupAuthoritiesByUsername(DEF_GROUP_AUTHORITIES_BY_USERNAME_QUERY);  // enable groups
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { // @formatter:off
        http.requestMatchers()
                .antMatchers("/login", "/oauth/authorize")
                .and().authorizeRequests().anyRequest().authenticated()
                .and().formLogin().permitAll()
                .and().csrf().disable();
    } // @formatter:on


}
