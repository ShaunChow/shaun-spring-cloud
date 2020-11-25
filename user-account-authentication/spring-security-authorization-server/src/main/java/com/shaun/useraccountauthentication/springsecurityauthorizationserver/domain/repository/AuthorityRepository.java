package com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain.repository;

import com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain.Authority;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthorityRepository {

    @Insert("insert into authorities(username,authority) values " +
            "(#{username},#{authority})")
    int insert(Authority authority);
}
