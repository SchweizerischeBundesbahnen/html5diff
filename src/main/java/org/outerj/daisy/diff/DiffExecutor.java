package org.outerj.daisy.diff;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A project-level executor with multiple threads that can be used to increase concurrency.
 */
public class DiffExecutor {

    private static final int THREAD_COUNT = Math.max(2, Runtime.getRuntime().availableProcessors());

    private static final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT, new ThreadFactory() {
        private final AtomicInteger counter = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "DaisyDiff-Worker-" + counter.getAndIncrement());
            t.setDaemon(true);
            return t;
        }
    });

    /**
     * @return the project-level executor service.
     */
    public static ExecutorService getExecutor() {
        return executor;
    }
}
