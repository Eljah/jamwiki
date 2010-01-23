package com.itmatter.jamwiki.crawler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 *
 * @author dfisla
 */
public class BulkLoaderPoolExecutor {

    private static Logger logger = Logger.getLogger(BulkLoaderPoolExecutor.class.getName());
    private long keepAliveTime = 10;
    private ThreadPoolExecutor threadPool = null;
    private final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(50);

    public BulkLoaderPoolExecutor(int poolSize, int maxPoolSize) {
        threadPool = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);
    }

    public void runTask(Runnable task) {
        // System.out.println("Task count.."+threadPool.getTaskCount() );
        // System.out.println("Queue Size before assigning the
        // task.."+queue.size() );
        threadPool.execute(task);
        // System.out.println("Queue Size after assigning the
        // task.."+queue.size() );
        // System.out.println("Pool Size after assigning the
        // task.."+threadPool.getActiveCount() );
        // System.out.println("Task count.."+threadPool.getTaskCount() );
        // System.out.println("Task count.." + queue.size());

    }

    public void shutDown() {
        threadPool.shutdown();
    }
}

