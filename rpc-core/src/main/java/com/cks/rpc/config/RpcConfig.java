package com.cks.rpc.config;

import com.cks.rpc.fault.retry.RetryStrategyKeys;
import com.cks.rpc.fault.tolerant.TolerantStrategyKeys;
import com.cks.rpc.loadbalancer.LoadBalancerKeys;
import com.cks.rpc.serializer.SerializerKeys;
import lombok.Data;

@Data
public class RpcConfig {

    private String name = "rpc";

    private String version = "1.0";

    private String serverHost = "localhost";

    private Integer serverPort = 8088;

    private boolean mock = false;

    private String serializer = SerializerKeys.JSON;

    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    private String retryStrategy = RetryStrategyKeys.NO;

    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;

    private RegistryConfig registryConfig = new RegistryConfig();

}
