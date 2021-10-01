package com.deviget.service;

import org.springframework.stereotype.Service;

@Service
public class UserAuthService {

    private void BlockUser(){
        System.out.println("UserAuthService");
    }

    private void updateUserFailedRetryCount(){}
}
