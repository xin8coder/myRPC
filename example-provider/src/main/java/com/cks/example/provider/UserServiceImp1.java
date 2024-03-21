package com.cks.example.provider;

import com.cks.example.common.model.User;
import com.cks.example.common.service.UserService;

public class UserServiceImp1 implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("用户名："+user.getName());
        return user;
    }
}
