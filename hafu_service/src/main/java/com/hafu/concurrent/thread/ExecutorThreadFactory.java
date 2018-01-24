package com.hafu.concurrent.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutorThreadFactory implements ThreadFactory {

        private Logger logger = LoggerFactory.getLogger(ExecutorThreadFactory.class);

        private static final AtomicInteger poolNumber = new AtomicInteger(1);

        private final AtomicInteger threadNumber = new AtomicInteger(1);

        private final ThreadGroup threadGroup;

        private final String name;

        private final int priority;

        private final boolean daemon;

        public ExecutorThreadFactory() {
            this(null);
        }

        public ExecutorThreadFactory(String name) {
            this(name, Thread.currentThread().getThreadGroup());
        }

        public ExecutorThreadFactory(String name, ThreadGroup group) {
            this(name, group, Thread.NORM_PRIORITY, false);
        }

        public ExecutorThreadFactory(String name, ThreadGroup group, int priority, boolean daemon) {
            this.name = "P{" + poolNumber.getAndIncrement() + '}' + ((name != null) ? name : '?');
            this.threadGroup = group;
            this.priority = priority;
            this.daemon = daemon;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(threadGroup, r, name + "-T{" + threadNumber.getAndIncrement() + '}');
            t.setPriority(priority);
            t.setDaemon(daemon);
            logger.debug("New thread {} created", t.getName());
            return t;
    }

}
