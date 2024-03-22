package com.cks.rpc.config;

//import com.cks.rpc.registry.RegistryKeys;
import com.cks.rpc.registry.RegistryKeys;
import lombok.Data;

/**
 * RPC 框架注册中心配置
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @learn <a href="https://codefather.cn">鱼皮的编程宝典</a>
 * @from <a href="https://yupi.icu">编程导航学习圈</a>
 */
@Data
public class RegistryConfig {

    /**
     * 注册中心类别
     */
    private String registry = RegistryKeys.ZOOKEEPER;

    /**
     * 注册中心地址
     */
    private String address = "localhost:2181";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间（单位毫秒）
     */
    private Long timeout = 10000L;
}
