package com.cks.example.provider;


import com.cks.example.common.service.UserService;
import com.cks.rpc.RpcApplication;
import com.cks.rpc.config.RegistryConfig;
import com.cks.rpc.config.RpcConfig;
import com.cks.rpc.model.ServiceMetaInfo;
import com.cks.rpc.registry.LocalRegistry;
import com.cks.rpc.registry.Registry;
import com.cks.rpc.registry.RegistryFactory;
import com.cks.rpc.server.HttpServer;
import com.cks.rpc.server.VertxHttpServer;
import com.cks.rpc.server.tcp.VertxTcpServer;
import io.vertx.core.Vertx;

import java.util.ArrayList;

/**
 * 服务提供者示例
 *
 */
public class ProviderExample {

    public static void main(String[] args) {
        // 要注册的服务
        RpcApplication.init();

        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImp1.class);

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());

        try{
            registry.register(serviceMetaInfo);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(RpcApplication.getRpcConfig().getServerPort());

    }
}
