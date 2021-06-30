/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.keepalive;

import com.zyonicsoftware.minereaper.runnable.RedEugeneSchedulerRunnable;
import com.zyonicsoftware.minereaper.signal.caller.SignalCallRegistry;
import com.zyonicsoftware.minereaper.signal.client.Client;
import com.zyonicsoftware.minereaper.signal.exception.SignalException;
import com.zyonicsoftware.minereaper.signal.packet.ahead.KeepAlivePacket;
import com.zyonicsoftware.minereaper.signal.scheduler.RedEugeneScheduler;
import com.zyonicsoftware.minereaper.signal.signal.SignalProvider;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

/**
 * @author Niklas Griese
 * @see java.lang.Runnable
 * @see com.zyonicsoftware.minereaper.runnable.RedEugeneSchedulerRunnable
 */
public class KeepAliveThread extends RedEugeneSchedulerRunnable {

  /** init client */
  private final Client client;

  public KeepAliveThread(
      @NotNull final String eugeneJobName,
      @NotNull final TimeUnit timeUnit,
      final long period,
      final Client client) {
    super(eugeneJobName, timeUnit, period);
    this.client = client;
  }

  /** call at specific timeout the "keep a live" packet */
  @Override
  public void run() {
    super.run();
    this.client.send(new KeepAlivePacket());
  }

  /** destroy the runner at the client disconnect */
  public void interrupt() {
    final String jobName = this.getEugeneJob().getName();
    if (RedEugeneScheduler.getRedEugeneIntroduction().forceCancelEugeneJob(jobName)) {
      try {
        SignalCallRegistry.getReferenceCaller()
            .getDeclaredConstructor(String.class)
            .newInstance(this.toString())
            .canceledJobMessage(
                SignalProvider.getSignalProvider()
                    .getCanceledJobMessage()
                    .replaceAll("%job%", jobName));
      } catch (final InstantiationException
          | IllegalAccessException
          | InvocationTargetException
          | NoSuchMethodException exception) {
        throw new SignalException(
            SignalProvider.getSignalProvider().getCanceledJobThrowsAnException(), exception);
      }
    }
  }
}
