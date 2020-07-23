package com.shaun.useraccountauthentication.springsecurityresourceserver.web.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
public class UserResource {

    @GetMapping("viewCars")
    @PreAuthorize("hasAuthority('ROLE_GROUP_WRITE')")
    public Set<String> viewCars() {

        return cars;
    }

    static Set<String> cars = new HashSet<>();

    static {
        cars.add("Toyota");
        cars.add("Benz");
    }
}
