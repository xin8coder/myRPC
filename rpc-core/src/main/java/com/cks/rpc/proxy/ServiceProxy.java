package com.cks.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.cks.rpc.RpcApplication;
import com.cks.rpc.config.RpcConfig;
import com.cks.rpc.constant.RpcConstant;
import com.cks.rpc.model.RpcRequest;
import com.cks.rpc.model.RpcResponse;
import com.cks.rpc.model.ServiceMetaInfo;
import com.cks.rpc.registry.Registry;
import com.cks.rpc.registry.RegistryFactory;
import com.cks.rpc.serializer.JdkSerializer;
import com.cks.rpc.serializer.Serializer;
import com.cks.rpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 发送请求

            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);

            serviceMetaInfo.setServiceVersion((RpcConstant.DEFAULT_SERVICE_VERSION));
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());

            if (CollUtil.isEmpty((serviceMetaInfoList))){
                throw new RuntimeException("无服务");
            }

            ServiceMetaInfo selected = serviceMetaInfoList.get(0);

            try (HttpResponse httpResponse = HttpRequest.post(selected.getServiceAddress())
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                // 反序列化
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}