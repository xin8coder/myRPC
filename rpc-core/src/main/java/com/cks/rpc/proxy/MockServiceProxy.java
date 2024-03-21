package com.cks.rpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class MockServiceProxy implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> methodReturnType = method.getReturnType();
        log.info("mock invoke{}",method.getName());
        return getDeaultObject(methodReturnType);
    }

    private Object getDeaultObject(Class<?> type) {
        if(type.isPrimitive()){
            if(type==boolean.class){
                return false;
            } else if (type==short.class){
                return (short) 0;
            } else if (type==int.class){
                return  0;
            } else if (type==long.class){
                return 0L;
            }
        }
        return null;
    }
}
