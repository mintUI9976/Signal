package com.zyonicsoftware.minereaper.signal.server;

import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.schedule.Schedules;
import com.zyonicsoftware.minereaper.signal.caller.SignalCallRegistry;
import com.zyonicsoftware.minereaper.signal.caller.SignalCaller;
import com.zyonicsoftware.minereaper.signal.client.Client;
import com.zyonicsoftware.minereaper.signal.exception.SignalException;
import com.zyonicsoftware.minereaper.signal.inspector.IPV4AddressInspector;
import com.zyonicsoftware.minereaper.signal.packet.Packet;
import com.zyonicsoftware.minereaper.signal.packet.ahead.UpdateUUIDPacket;
import com.zyonicsoftware.minereaper.signal.signal.SignalProvider;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ServerSocketAcceptingThread {

    private final ServerSocket serverSocket;
    private final List<Client> clients = new ArrayList<>();
    private final Class<? extends SignalCaller> signalCaller;
    private final Scheduler scheduler;
    private final String jobName = "client_acceptor";
    private final long scheduleDelay;

    public ServerSocketAcceptingThread(final ServerSocket serverSocket, final Scheduler scheduler, final long scheduleDelay) {
        this.serverSocket = serverSocket;
        this.signalCaller = SignalCallRegistry.getReferenceCaller();
        this.scheduler = scheduler;
        this.scheduleDelay = scheduleDelay;
    }

    public void run() {
        this.scheduler.schedule(this.jobName, () -> {
            try {
                if (this.serverSocket.isClosed()) {
                    this.interrupt();
                    return;
                }
                final Socket socket = this.serverSocket.accept();
                if (IPV4AddressInspector.getAcceptedIPAddresses().contains(socket.getInetAddress().getHostAddress())) {
                    final Client client = new Client(socket, this.signalCaller, this.scheduler, this.scheduleDelay);
                    client.connect();
                    this.clients.add(client);
                    this.signalCaller.getDeclaredConstructor(String.class).newInstance(this.toString()).acceptSocketConnectionMessage(SignalProvider.getSignalProvider().getAcceptSocketConnectionMessage());
                    //update connectionUUID on client side
                    final UpdateUUIDPacket updateUUIDPacket = new UpdateUUIDPacket(client.getConnectionUUID().get());
                    client.send(updateUUIDPacket);
                } else {
                    this.signalCaller.getDeclaredConstructor(String.class).newInstance(this.toString()).unAcceptedSocketConnectionMessage(SignalProvider.getSignalProvider().getUnAcceptSocketConnectionMessage());
                    socket.close();
                }

            } catch (final IOException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException exception) {
                throw new SignalException(exception);
            }
        }, Schedules.fixedDelaySchedule(Duration.ofMillis(1)));

    }

    public void interrupt() {
        Thread.currentThread().interrupt();
        this.scheduler.cancel(this.jobName).thenAccept(job -> {
            try {
                this.signalCaller.getDeclaredConstructor(String.class).newInstance(this.toString()).canceledJob(SignalProvider.getSignalProvider().getCanceledJobMessage().replaceAll("%job%", job.name()));
            } catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
                throw new SignalException(SignalProvider.getSignalProvider().getCanceledJobThrowsAnException(), exception);
            }
        });
    }

    public void sendToClient(final Packet packet, final UUID uuid) {
        //send to client
        this.clients.stream().filter(client -> client.getConnectionUUID().get().equals(uuid)).forEach(client -> client.send(packet));
    }


    public void sendToAllClients(final Packet packet) {
        //send to all clients
        this.clients.forEach(client -> client.send(packet));
    }

    public void disconnectClient(final UUID uuid) {
        //disconnect client
        for (final Iterator<Client> clientIterator = ServerSocketAcceptingThread.this.clients.listIterator(); clientIterator.hasNext(); ) {
            final Client client = clientIterator.next();
            if (client.getConnectionUUID().get().equals(uuid)) {
                try {
                    this.signalCaller.getDeclaredConstructor(String.class).newInstance(this.toString()).disconnectClientMessage(SignalProvider.getSignalProvider().getDisconnectClient().replace("%client%", client.getConnectionUUID().get().toString()));
                    client.disconnect();
                } catch (final IOException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException exception) {
                    throw new SignalException(exception);
                }
                clientIterator.remove();
            }
        }
    }

    public void disconnectAllClients() {
        //disconnect all clients
        try {
            this.signalCaller.getDeclaredConstructor(String.class).newInstance(this.toString()).disconnectAllClientMessage(SignalProvider.getSignalProvider().getDisconnectAllClients());
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
            throw new SignalException(exception);
        }
        this.clients.forEach(client -> {
            try {
                if (client != null) {
                    client.disconnect();
                }
            } catch (final IOException exception) {
                throw new SignalException(exception);
            }
        });
        this.clients.clear();
    }

}
