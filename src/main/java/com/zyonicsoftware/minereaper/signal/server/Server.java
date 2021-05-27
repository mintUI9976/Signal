package com.zyonicsoftware.minereaper.signal.server;

import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.SchedulerConfig;
import com.zyonicsoftware.minereaper.signal.caller.SignalCallRegistry;
import com.zyonicsoftware.minereaper.signal.caller.SignalCaller;
import com.zyonicsoftware.minereaper.signal.connection.Connection;
import com.zyonicsoftware.minereaper.signal.inspector.IPV4AddressInspector;
import com.zyonicsoftware.minereaper.signal.packet.Packet;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Server extends Connection {

    private final int port;
    private ServerSocket serverSocket;
    private ServerSocketAcceptingThread serverSocketAcceptingThread;
    private final Scheduler scheduler;
    private final long scheduleDelay;


    /**
     * @param port                to bound the ServerSocket on your own port.
     * @param signalCaller        create your own event caller to receive message from Signal ( e.g signal has detect the package is to large or an client has been disconnected or else)
     * @param acceptedIpAddresses only allowed ip addresses can connect
     * @param scheduler           you can create your own scheduler with Wisp technology / check out https://github.com/Coreoz/Wisp
     * @param scheduleDelay       this is the delay how long the input and output stream wait before receive or send packets ( The lower the delay is the more cpu power the api will consume)
     *                            The best delay I have tried is 60ms.
     */

    public Server(final int port, @NotNull final Class<? extends SignalCaller> signalCaller, @NotNull final String[] acceptedIpAddresses, @NotNull final Scheduler scheduler, final long scheduleDelay) {
        this.port = port;
        this.scheduler = scheduler;
        this.scheduleDelay = scheduleDelay;
        SignalCallRegistry.registerReferenceCaller(signalCaller);
        IPV4AddressInspector.addIpv4Addresses(Arrays.asList(acceptedIpAddresses));
    }

    /**
     * @param port                to bound the ServerSocket on your own port.
     * @param signalCaller        create your own event caller to receive message from Signal ( e.g signal has detect the package is to large or an client has been disconnected or else)
     * @param acceptedIpAddresses only allowed ip addresses can connect
     * @param minThreads          this set the minimal usable threads for Wisp scheduler /  check out https://github.com/Coreoz/Wisp
     * @param maxThreads          this set the maximal usable threads for Wisp scheduler  / check out https://github.com/Coreoz/Wisp
     * @param scheduleDelay       this is the delay how long the input and output stream wait before receive or send packets ( The lower the delay is the more cpu power the api will consume)
     *                            The best delay I have tried is 60ms.
     */


    public Server(final int port, @NotNull final Class<? extends SignalCaller> signalCaller, @NotNull final String[] acceptedIpAddresses, final int minThreads, final int maxThreads, final long scheduleDelay) {
        this.port = port;
        SignalCallRegistry.registerReferenceCaller(signalCaller);
        this.scheduler = new Scheduler(SchedulerConfig.builder().minThreads(minThreads).maxThreads(maxThreads).build());
        this.scheduleDelay = scheduleDelay;
        IPV4AddressInspector.addIpv4Addresses(Arrays.asList(acceptedIpAddresses));
    }

    @Override
    public void connect() throws IOException {
        //check if serverSocket is initialised
        if (this.serverSocket == null) {
            //initialise serverSocket
            this.serverSocket = new ServerSocket(this.port);
        }
        //start accepting clients
        this.serverSocketAcceptingThread = new ServerSocketAcceptingThread(this.serverSocket, this.scheduler, this.scheduleDelay);
        this.serverSocketAcceptingThread.run();
    }

    @Override
    public void disconnect() throws IOException {
        //disconnect all clients
        this.disconnectAllClients();
        // destroy server accepting thread
        this.serverSocketAcceptingThread.interrupt();
        //check if serverSocket is closed
        if (!this.serverSocket.isClosed()) {
            //disconnect serverSocket
            this.serverSocket.close();
        }
    }

    public int getPort() {
        return this.port;
    }


    public void sendToClient(final Packet packet, final UUID uuid) {
        //send to client
        this.serverSocketAcceptingThread.sendToClient(packet, uuid);
    }

    public void sendToAllClients(final Packet packet) {
        //send to all clients
        this.serverSocketAcceptingThread.sendToAllClients(packet);
    }

    public CompletableFuture<Void> sendToClientAsync(final Packet packet, final UUID uuid) {
        //send to client
        return CompletableFuture.runAsync(() -> this.serverSocketAcceptingThread.sendToClient(packet, uuid));
    }

    public CompletableFuture<Void> sendToAllClientsAsync(final Packet packet) {
        //send to all clients
        return CompletableFuture.runAsync(() -> this.serverSocketAcceptingThread.sendToAllClients(packet));
    }

    public void disconnectClient(final UUID uuid) {
        //disconnect client
        this.serverSocketAcceptingThread.disconnectClient(uuid);
    }

    public Scheduler getScheduler() {
        return this.scheduler;
    }

    public void disconnectAllClients() {
        //disconnect all clients
        this.serverSocketAcceptingThread.disconnectAllClients();
    }
}
