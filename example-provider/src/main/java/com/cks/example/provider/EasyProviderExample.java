package com.cks.example.provider;

import com.cks.example.common.service.UserService;
import com.cks.rpc.RpcApplication;
import com.cks.rpc.registry.LocalRegistry;
import com.cks.rpc.server.HttpServer;
import com.cks.rpc.server.VertxHttpServer;

public class EasyProviderExample {
    public static void main(String[] args){
        //
        //RpcApplication.init();

        LocalRegistry.register(UserService.class.getName(), UserServiceImp1.class);

        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
