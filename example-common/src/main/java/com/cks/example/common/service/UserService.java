package com.cks.example.common.service;

import com.cks.example.common.model.User;

public interface UserService {
    User getUser(User user);

    default short getNumber(){
        return 1;
    }
}
