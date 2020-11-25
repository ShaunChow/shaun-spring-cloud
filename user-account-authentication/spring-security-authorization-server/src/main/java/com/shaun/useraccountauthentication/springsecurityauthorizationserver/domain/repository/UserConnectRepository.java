package com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain.repository;

import com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain.UserConnect;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface UserConnectRepository {

    @Select("select * from user_connection " +
            "where providerId=#{providerId} and providerUserId=#{providerUserId}")
    Optional<UserConnect> findUserConnectByProvider(String providerId, String providerUserId);

    @Insert("insert into user_connection(userId,providerId,providerUserId,rank,accessToken) values " +
            "(#{userId},#{providerId},#{providerUserId},0,#{accessToken})")
    int insert(UserConnect userConnect);
}
