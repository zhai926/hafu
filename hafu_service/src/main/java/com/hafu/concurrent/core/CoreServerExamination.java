package com.hafu.concurrent.core;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hafu.concurrent.handler.ThreadHandlerExamianation;
import com.hafu.concurrent.thread.ExecutorThreadFactory;

public class CoreServerExamination {

    private static Logger logger = LoggerFactory.getLogger(CoreServerExamination.class);

    private static ExecutorService executor = null;

    private ThreadHandlerExamianation handler = null;

    public ThreadHandlerExamianation getHandler() {
        return handler;
    }

    public void setHandler(ThreadHandlerExamianation handler) {
        this.handler = handler;
    }
    
    private long stoptimeout = 10000; //millisecond

    public void setStoptimeout(long stoptimeout) {
        this.stoptimeout = stoptimeout;
    }

    public CoreServerExamination() {
    }

    public void start() {
        try {
            logger.info("Start Thread");
            if (executor == null) {
                executor = Executors.newCachedThreadPool(new ExecutorThreadFactory("core"));
            }
            control();
            logger.info("Start Thread Finished");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void control() {
        executor.submit(handler);
    }

    public void stop() {
        try {
            logger.info("Stop Thread");
            handler.shutdown();
            if (executor != null) {
                executor.shutdown();
                try {
                    executor.awaitTermination(stoptimeout, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
                List<Runnable> lrs = executor.shutdownNow();
                for (Runnable r : lrs) {
                    logger.info("shutdownNow Stop Runnable {}", r.toString());
                }
            }
            logger.info("Stop Threads Finished");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
