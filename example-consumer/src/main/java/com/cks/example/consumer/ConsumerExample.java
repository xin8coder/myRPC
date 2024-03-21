package com.cks.example.consumer;

import com.cks.example.common.model.User;
import com.cks.example.common.service.UserService;
import com.cks.rpc.config.RpcConfig;
import com.cks.rpc.proxy.ServiceProxyFactory;
import com.cks.rpc.utils.ConfigUtils;

public class ConsumerExample {
    public static void main(String[] args){

        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("cks");
        User newUser = userService.getUser(user);

        if(newUser!=null){
            System.out.println(newUser.getName());
        }else{
            System.out.println("user==null");
        }

        long number = userService.getNumber();
        System.out.println(number);

    }

}
