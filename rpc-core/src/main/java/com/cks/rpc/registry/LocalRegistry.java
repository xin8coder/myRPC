package com.cks.rpc.registry;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalRegistry {
    private static final Map<String,Class<?>> map = new ConcurrentHashMap<>();

    public static void register(String serviceName, Class<?> imp1Class){
        map.put(serviceName,imp1Class);
    }

    public static Class<?> get(String serviceName){
        return map.get(serviceName);
    }

    public static void remove(String serviceName){
        map.remove(serviceName);
    }
}
