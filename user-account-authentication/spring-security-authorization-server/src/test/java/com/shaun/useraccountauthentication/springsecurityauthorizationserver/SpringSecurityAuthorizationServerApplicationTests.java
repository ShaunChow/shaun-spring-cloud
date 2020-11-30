package com.shaun.useraccountauthentication.springsecurityauthorizationserver;

import com.shaun.useraccountauthentication.springsecurityauthorizationserver.util.PassUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
class SpringSecurityAuthorizationServerApplicationTests {

    @Test
    void randomPassword() {
        int turn = 100;
        while (turn>0) {
            System.out.println(PassUtil.generatePass(6));
            turn--;
        }
    }

}
