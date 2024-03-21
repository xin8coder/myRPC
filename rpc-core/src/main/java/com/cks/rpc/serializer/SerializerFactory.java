package com.cks.rpc.serializer;

import java.util.HashMap;
import java.util.Map;

public class SerializerFactory {

    public static final Map<String,Serializer> KEY_SERIALIZER_MAP = new HashMap<>(){{
       put(SerializerKeys.JDK,new JdkSerializer());
        put(SerializerKeys.JSON,new JsonSerializer());
        put(SerializerKeys.KRYO,new KryoSerializer());
        put(SerializerKeys.HESSIAN,new HessianSerializer());
    }};

    private static final Serializer DEFAULT_SERIALIZER = KEY_SERIALIZER_MAP.get("jdk");

    public static Serializer getInstance(String key){
        return KEY_SERIALIZER_MAP.getOrDefault(key,DEFAULT_SERIALIZER);
    }
}
