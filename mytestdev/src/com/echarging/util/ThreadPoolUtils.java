package com.echarging.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtils {
	
	 //核心线程池大小
    public static final int       CORE_THREAD_NUM     = 50;
    //最大线程数
    public static final int       MAX_THREAD_NUM      = 100;
    //队列容量，默认和最小线程池一样大
    public static final int       QUEUE_CAPACITY      = 50;
    //线程最大等待时间
    public static final long      KEPP_ALIVE_TIME     = 10000L;
    
    public static final ExecutorService pool = new ThreadPoolExecutor(CORE_THREAD_NUM,MAX_THREAD_NUM,KEPP_ALIVE_TIME,TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(QUEUE_CAPACITY));;

}
