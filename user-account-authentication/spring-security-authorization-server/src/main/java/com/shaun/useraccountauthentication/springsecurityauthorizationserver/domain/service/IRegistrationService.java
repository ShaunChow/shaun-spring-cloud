package com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain.service;

import com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain.User;

import java.util.Map;

public interface IRegistrationService {

    Map<String,String> registration(
            String userId
            ,String providerId
            ,String providerUserId
            ,String profileUrl
            ,String accessToken
    );

    User findByUserName(String userName);

    void setDefaultAuthority(String userName);
}
