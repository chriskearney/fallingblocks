package com.comandante.game.board;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DelayFire {

    private long FIRE_DELAY = 500;

    private final Cache<Integer, Runnable> runnableCache = CacheBuilder.newBuilder()
            .expireAfterWrite(FIRE_DELAY, TimeUnit.MILLISECONDS)
            .maximumSize(1)
            .removalListener(new ResizeRemovalListener())
            .build();

    public DelayFire() {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleWithFixedDelay(
                new Runnable() {
                    public void run() {
                        runnableCache.cleanUp();
                    }
                }, 0, 50, TimeUnit.MILLISECONDS);
    }

    private class ResizeRemovalListener implements RemovalListener<Integer, Runnable> {
        @Override
        public void onRemoval(RemovalNotification<Integer, Runnable> removal) {
            if (!removal.getCause().equals(RemovalCause.EXPIRED)) {
                return;
            }
            Runnable runnable = removal.getValue();
            if (runnable == null) return;
            try {
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void add(Integer id, Runnable runnable) {
        runnableCache.put(id, runnable);
    }
}
