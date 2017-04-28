package com.echarging.util;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
@Component
@Aspect
public class LoggerAopUtil {
	
	private Logger log = Logger.getLogger(this.getClass());

    private long beginTime = 0;
    @Before("execution(* *com.echarging.service..*.*(..))")
    public void doBeforeMethod(){
        beginTime = System.currentTimeMillis();
    }

    /**
     * 触发调用方法
     */
    @Around("execution(* *com.echarging.service..*.*(..))")
    public Object doCallMethod(ProceedingJoinPoint  pjp){
        String method = pjp.getSignature().getName();//获取连接点的方法签名对象
        Object args [] = pjp.getArgs();//获取连接点方法运行时的入参列表
        StringBuffer buffer = new StringBuffer();
        buffer.append(pjp.getSignature().getDeclaringTypeName()).append(":method=").append(method).append(";param:[");
        Method [] mt = null;
        try {
            mt = Class.forName(pjp.getSignature().getDeclaringTypeName()).getDeclaredMethods();
            for(int i = 0;i<mt.length;i++){
                if(mt[i].getName().equals(method)){
                    mt[0] = mt[i];
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String [] params = new LocalVariableTableParameterNameDiscoverer().getParameterNames(mt[0]);
        if(params != null){
            for (int i = 0; i < params.length; i++) {
                if(i == params.length -1){
                    buffer.append(String.valueOf(args[i]).toString());
                }else{
                    buffer.append(params[i]).append("=").append(String.valueOf(args[i])).append(",");
                }
            }
        }
        //方法返回值
        Object result = "";
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        buffer.append("];result=").append(result).append(";");
        buffer.append("timeMillis=").append(System.currentTimeMillis()-beginTime);
        log.info(buffer.toString());
        return result;
    }

}
