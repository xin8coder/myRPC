package com.cks.example.consumer;

import com.cks.example.common.model.User;
import com.cks.example.common.service.UserService;
import com.cks.rpc.proxy.ServiceProxyFactory;

public class EasyConsumerExample {
    public static void  main(String[] args){

        //UserService userService = null;
        UserService userService= new UserServiceProxy();

        //UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("haaha");
        User newUser = userService.getUser(user);
        if(newUser !=null){
            System.out.println(newUser.getName());
        }else{
            System.out.println("user==null");
        }
    }
}
