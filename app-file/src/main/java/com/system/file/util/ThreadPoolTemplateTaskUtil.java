package com.system.file.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTemplateTaskUtil {
    private int corePoolSize = 5;
    private int maximumPoolSize = 10;
    private long keepAliveTime = 1;
    private ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(128);

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
            keepAliveTime, TimeUnit.MINUTES, workQueue,
            new ThreadFactoryBuilder().setNameFormat("ThreadPool-%d").build());

    private static class Holder {
        private static final ThreadPoolTemplateTaskUtil INSTANCE = new ThreadPoolTemplateTaskUtil();
    }

    public static ThreadPoolTemplateTaskUtil getInstance() {
        return Holder.INSTANCE;
    }

    public void startTask(Runnable task) {
        executor.execute(task);
    }

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }
}

