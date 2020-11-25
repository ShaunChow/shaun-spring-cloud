package com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain.repository;

import com.shaun.useraccountauthentication.springsecurityauthorizationserver.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface UserRepository {
    @Insert("insert into users(username,password) values " +
            "(#{username},#{password})")
    int insert(User user);

    @Select("select * from users " +
            "where username=#{username}")
    Optional<User> findByUserName(String username);
}
