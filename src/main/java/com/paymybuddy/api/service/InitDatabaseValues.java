
package com.paymybuddy.api.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InitDatabaseValues {

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() { userService.addManyUser(); }

}
