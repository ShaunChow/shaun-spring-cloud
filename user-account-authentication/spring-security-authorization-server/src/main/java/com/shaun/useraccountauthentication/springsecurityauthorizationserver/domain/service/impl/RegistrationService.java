package com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain.service.impl;

import com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain.Authority;
import com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain.User;
import com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain.UserConnect;
import com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain.repository.AuthorityRepository;
import com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain.repository.UserConnectRepository;
import com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain.repository.UserRepository;
import com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain.service.IRegistrationService;
import com.shaun.useraccountauthentication.springsecurityauthorizationserver.util.PassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class RegistrationService implements IRegistrationService {

    @Autowired
    UserConnectRepository userConnectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Map<String, String> registration(
            String userId
            , String providerId
            , String providerUserId
            , String profileUrl
            , String accessToken) {

        Map<String, String> result = new HashMap<>();

        Optional<UserConnect> userConnect
                = userConnectRepository.findUserConnectByProvider(
                providerId, providerUserId);

        if (userConnect.isPresent()) {
            result.putIfAbsent("name", userConnect.get().getUserId());
            return result;
        }

        userId = findAvailableUserName(userId);

        User user = new User();
        user.setUsername(userId);
        user.setPassword(bCryptPasswordEncoder.encode(PassUtil.generatePass(6)));
        userRepository.insert(user);

        setDefaultAuthority(userId);

        UserConnect newUserConnect = new UserConnect();
        newUserConnect.setUserId(userId);
        newUserConnect.setProviderId(providerId);
        newUserConnect.setProviderUserId(providerUserId);
        newUserConnect.setAccessToken("");
        userConnectRepository.insert(newUserConnect);

        result.putIfAbsent("name", newUserConnect.getUserId());
        return result;
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }

    @Override
    public void setDefaultAuthority(String userName) {
        Authority authority = new Authority();
        authority.setUsername(userName);
        authority.setAuthority("ROLE_USER");
        authorityRepository.insert(authority);
    }

    private String findAvailableUserName(String userName_prefix) {

        userName_prefix = userName_prefix.replace(" ", "_");

        User account = this.findByUserName(userName_prefix);
        if (account == null) {
            return userName_prefix;
        }
        int i = 0;
        while (true) {
            String userName = userName_prefix + "_" + i++;
            account = this.findByUserName(userName);
            if (account == null) {
                return userName;
            }
        }
    }
}
