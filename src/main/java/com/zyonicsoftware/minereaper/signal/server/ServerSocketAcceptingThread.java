/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.server;

import com.zyonicsoftware.minereaper.runnable.RedEugeneSchedulerRunnable;
import com.zyonicsoftware.minereaper.signal.caller.SignalCallRegistry;
import com.zyonicsoftware.minereaper.signal.client.Client;
import com.zyonicsoftware.minereaper.signal.exception.SignalException;
import com.zyonicsoftware.minereaper.signal.inspector.IPV4AddressInspector;
import com.zyonicsoftware.minereaper.signal.packet.Packet;
import com.zyonicsoftware.minereaper.signal.packet.ahead.UpdateUUIDPacket;
import com.zyonicsoftware.minereaper.signal.scheduler.RedEugeneScheduler;
import com.zyonicsoftware.minereaper.signal.signal.SignalProvider;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ServerSocketAcceptingThread extends RedEugeneSchedulerRunnable {

  private final ServerSocket serverSocket;
  private final List<Client> clients = new ArrayList<>();
  private final long scheduleDelay;

  public ServerSocketAcceptingThread(
      @NotNull final String eugeneJobName,
      @NotNull final TimeUnit timeUnit,
      final long period,
      final ServerSocket serverSocket,
      final long scheduleDelay) {
    super(eugeneJobName, timeUnit, period);
    this.serverSocket = serverSocket;
    this.scheduleDelay = scheduleDelay;
  }

  @Override
  public void run() {
    super.run();
    try {
      if (this.serverSocket.isClosed()) {
        this.interrupt();
        return;
      }
      final Socket socket = this.serverSocket.accept();
      if (IPV4AddressInspector.getAcceptedIPAddresses()
          .contains(socket.getInetAddress().getHostAddress())) {
        final Client client = new Client(socket, this.scheduleDelay);
        client.connect();
        this.clients.add(client);
        SignalCallRegistry.getReferenceCaller()
            .getDeclaredConstructor(String.class)
            .newInstance(this.toString())
            .acceptSocketConnectionMessage(
                SignalProvider.getSignalProvider().getAcceptSocketConnectionMessage());
        // update connectionUUID on client side
        final UpdateUUIDPacket updateUUIDPacket =
            new UpdateUUIDPacket(client.getConnectionUUID().get());
        client.send(updateUUIDPacket);
      } else {
        SignalCallRegistry.getReferenceCaller()
            .getDeclaredConstructor(String.class)
            .newInstance(this.toString())
            .unAcceptedSocketConnectionMessage(
                SignalProvider.getSignalProvider().getUnAcceptSocketConnectionMessage());
        socket.close();
      }

    } catch (final IOException
        | InstantiationException
        | InvocationTargetException
        | NoSuchMethodException
        | IllegalAccessException exception) {
      throw new SignalException(exception);
    }
  }

  public void interrupt() {
    final String jobName = this.getEugeneJob().getName();
    if (RedEugeneScheduler.getRedEugeneIntroduction().forceCancelEugeneJob(jobName)) {
      try {
        SignalCallRegistry.getReferenceCaller()
            .getDeclaredConstructor(String.class)
            .newInstance(this.toString())
            .canceledJob(
                SignalProvider.getSignalProvider()
                    .getCanceledJobMessage()
                    .replaceAll("%job%", jobName));
      } catch (final InstantiationException
          | IllegalAccessException
          | InvocationTargetException
          | NoSuchMethodException exception) {
        throw new SignalException(exception);
      }
    }
  }

  public void sendToClient(final Packet packet, final UUID uuid) {
    // send to client
    this.clients.stream()
        .filter(client -> client.getConnectionUUID().get().equals(uuid))
        .forEach(client -> client.send(packet));
  }

  public void sendToAllClients(final Packet packet) {
    // send to all clients
    this.clients.forEach(client -> client.send(packet));
  }

  public void disconnectClient(final UUID uuid) {
    // disconnect client
    for (final Iterator<Client> clientIterator =
            ServerSocketAcceptingThread.this.clients.listIterator();
        clientIterator.hasNext(); ) {
      final Client client = clientIterator.next();
      if (client != null) {
        if (client.getConnectionUUID().get().equals(uuid)) {
          try {
            SignalCallRegistry.getReferenceCaller()
                .getDeclaredConstructor(String.class)
                .newInstance(this.toString())
                .disconnectClientMessage(
                    SignalProvider.getSignalProvider()
                        .getDisconnectClient()
                        .replace("%client%", client.getConnectionUUID().get().toString()));
            client.disconnect();
          } catch (final IOException
              | NoSuchMethodException
              | IllegalAccessException
              | InstantiationException
              | InvocationTargetException exception) {
            throw new SignalException(exception);
          }
          clientIterator.remove();
        }
      }
    }
  }

  public void disconnectAllClients() {
    // disconnect all clients
    try {
      SignalCallRegistry.getReferenceCaller()
          .getDeclaredConstructor(String.class)
          .newInstance(this.toString())
          .disconnectAllClientMessage(SignalProvider.getSignalProvider().getDisconnectAllClients());
    } catch (final InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException exception) {
      throw new SignalException(exception);
    }
    this.clients.forEach(client -> this.disconnectClient(client.getConnectionUUID().get()));
    // this.clients.clear();
  }
}
