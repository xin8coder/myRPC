package com.cks.rpc;

import com.cks.rpc.config.RegistryConfig;
import com.cks.rpc.registry.Registry;
import com.cks.rpc.registry.RegistryFactory;
import lombok.extern.slf4j.Slf4j;
import com.cks.rpc.config.RpcConfig;
import com.cks.rpc.constant.RpcConstant;
import com.cks.rpc.utils.ConfigUtils;

@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;

        log.info("rpc----init!,config={}",newRpcConfig.toString());

        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();

        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

        registry.init(registryConfig);
        log.info("registry inti,config = {}",registryConfig);

        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));

    }

    public static void init(){
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch (Exception e){
            System.out.println(e);
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    public static RpcConfig getRpcConfig(){
        if(rpcConfig==null){
            synchronized (RpcApplication.class){
                if(rpcConfig==null){
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
