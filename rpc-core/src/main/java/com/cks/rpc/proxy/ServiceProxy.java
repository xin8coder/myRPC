package com.cks.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import com.cks.rpc.RpcApplication;
import com.cks.rpc.config.RpcConfig;
import com.cks.rpc.constant.RpcConstant;
import com.cks.rpc.fault.retry.RetryStrategy;
import com.cks.rpc.fault.retry.RetryStrategyFactory;
import com.cks.rpc.fault.tolerant.TolerantStrategy;
import com.cks.rpc.fault.tolerant.TolerantStrategyFactory;
import com.cks.rpc.loadbalancer.LoadBalancer;
import com.cks.rpc.loadbalancer.LoadBalancerFactory;
import com.cks.rpc.model.RpcRequest;
import com.cks.rpc.model.RpcResponse;
import com.cks.rpc.model.ServiceMetaInfo;
import com.cks.rpc.registry.Registry;
import com.cks.rpc.registry.RegistryFactory;
import com.cks.rpc.server.tcp.VertxTcpClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 序列化器
        //final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        // 从注册中心获取服务提供者请求地址
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if (CollUtil.isEmpty(serviceMetaInfoList)) {
            throw new RuntimeException("暂无服务地址");
        }

        //负载均衡
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName",rpcRequest.getMethodName());
        ServiceMetaInfo selected =loadBalancer.select(requestParams, serviceMetaInfoList);
        RpcResponse rpcResponse;
        try {
            //rpc请求
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            rpcResponse = retryStrategy.doRetry(() -> VertxTcpClient.doRequest(rpcRequest, selected));
        }catch (Exception e){
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
            rpcResponse = tolerantStrategy.doTolerant(null, e);
        }
        return rpcResponse.getData();
    }
}