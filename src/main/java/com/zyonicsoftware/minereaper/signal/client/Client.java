/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.client;

import com.zyonicsoftware.minereaper.enums.EugeneFactoryPriority;
import com.zyonicsoftware.minereaper.redeugene.RedEugene;
import com.zyonicsoftware.minereaper.signal.caller.SignalCallRegistry;
import com.zyonicsoftware.minereaper.signal.caller.SignalCaller;
import com.zyonicsoftware.minereaper.signal.connection.Connection;
import com.zyonicsoftware.minereaper.signal.incoming.InputStreamThread;
import com.zyonicsoftware.minereaper.signal.outgoing.OutputStreamThread;
import com.zyonicsoftware.minereaper.signal.packet.Packet;
import com.zyonicsoftware.minereaper.signal.scheduler.RedEugeneScheduler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Client extends Connection {

  private String hostname;
  private int port;
  private Socket socket;
  private InputStreamThread inputStreamThread;
  private OutputStreamThread outputStreamThread;
  private final long scheduleDelay;

  public String getHostname() {
    return this.hostname;
  }

  public int getPort() {
    return this.port;
  }

  public Socket getSocket() {
    return this.socket;
  }

  public long getScheduleDelay() {
    return this.scheduleDelay;
  }

  /**
   * @param hostname to bound on right hostname.
   * @param port to bound the Socket on the right server port.
   * @param signalCaller create your own event caller to receive message from Signal ( e.g signal
   *     has detect the package is to large or an client has been disconnected or else)
   * @param scheduleDelay this is the delay how long the input and output stream wait before receive
   *     or send packets ( The lower the delay is the more cpu power the api will consume) The best
   *     delay I have tried is 60ms.
   */
  public Client(
      @NotNull final String hostname,
      final int port,
      @NotNull final Class<? extends SignalCaller> signalCaller,
      final long scheduleDelay) {
    this.hostname = hostname;
    this.port = port;
    RedEugeneScheduler.setRedEugene(
        new RedEugene("SignalClientPool", 3, false, EugeneFactoryPriority.NORM));
    SignalCallRegistry.registerReferenceCaller(signalCaller);
    this.scheduleDelay = scheduleDelay;
  }

  public Client(@NotNull final Socket socket, final long scheduleDelay) {
    this.socket = socket;
    this.scheduleDelay = scheduleDelay;
  }

  @Override
  public void connect() throws IOException {
    // check if socket is initialised
    if (this.socket == null) {
      // initialise socket
      this.socket = new Socket(this.hostname, this.port);
    }
    // start reading and writing
    final UUID random = UUID.randomUUID();
    this.inputStreamThread =
        new InputStreamThread(
            "InputStreamThread-" + random, TimeUnit.MILLISECONDS, this.scheduleDelay, this);
    RedEugeneScheduler.getRedEugeneIntroduction().scheduleWithoutDelay(this.inputStreamThread);
    this.outputStreamThread =
        new OutputStreamThread(
            "OutputStreamThread-" + random, TimeUnit.MILLISECONDS, this.scheduleDelay, this);
    RedEugeneScheduler.getRedEugeneIntroduction().scheduleWithoutDelay(this.outputStreamThread);
  }

  @Override
  public void disconnect() throws IOException {
    // interrupt reading and writing
    this.inputStreamThread.interrupt();
    this.outputStreamThread.interrupt();

    // check if socket is closed
    if (!this.socket.isClosed()) {
      // closed socket
      this.socket.close();
    }
  }

  public void send(final Packet packet) {
    this.outputStreamThread.send(packet);
  }
}
