package com.zyonicsoftware.minereaper.signal.executer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class JobExecutor {

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

    public static ScheduledExecutorService getScheduledExecutorService() {
        return JobExecutor.scheduledExecutorService;
    }
}
