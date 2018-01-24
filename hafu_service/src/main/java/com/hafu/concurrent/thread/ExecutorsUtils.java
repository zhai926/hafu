package com.hafu.concurrent.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ExecutorsUtils {

    public static ExecutorService newFixedBlockingThreadPool(int nThreads) {
        return newFixedBlockingThreadPool(nThreads, nThreads);
    }

    public static ExecutorService newFixedBlockingThreadPool(int nThreads, int maximumQueueSize) {
        return newFixedBlockingThreadPool(nThreads, maximumQueueSize, Long.MAX_VALUE);
    }

    public static ExecutorService newFixedBlockingThreadPool(int nThreads, int maximumQueueSize, long callerWaitsTimeout) {
        return newFixedBlockingThreadPool(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, maximumQueueSize, callerWaitsTimeout, new ExecutorThreadFactory());
    }

    public static ExecutorService newFixedBlockingThreadPool(int nThreads, int maximumQueueSize, long callerWaitsTimeout, ThreadFactory threadFactory) {
        return newFixedBlockingThreadPool(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, maximumQueueSize, callerWaitsTimeout, threadFactory);
    }

    public static ExecutorService newFixedBlockingThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, int maximumQueueSize, long callerWaitsTimeout,
            ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, new LinkedBlockingQueue<Runnable>(maximumQueueSize), threadFactory, new CallerWaitsPolicy(callerWaitsTimeout,
                TimeUnit.MILLISECONDS));
    }
}
