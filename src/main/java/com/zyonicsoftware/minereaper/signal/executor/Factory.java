package com.zyonicsoftware.minereaper.signal.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Factory {

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

    public static ScheduledExecutorService getScheduledExecutorService() {
        return Factory.scheduledExecutorService;
    }
}
