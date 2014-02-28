
package com.android.splus.sdk.utils.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {
    private ExecutorService service;

    private static byte[] lock = new byte[0];

    private static ThreadPoolManager manager;

    private ThreadPoolManager() {
        int num = Runtime.getRuntime().availableProcessors();
        service = Executors.newFixedThreadPool(num * 4);
    }

    public static ThreadPoolManager getInstance() {
        if (manager == null) {
            synchronized (lock) {
                if (manager == null) {

                    manager = new ThreadPoolManager();
                }
            }

        }
        return manager;
    }

    public void addTask(Runnable runnable) {
        service.execute(runnable);
    }
}
