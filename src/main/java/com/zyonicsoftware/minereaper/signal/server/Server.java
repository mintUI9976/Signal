/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.server;

import com.zyonicsoftware.minereaper.enums.EugeneFactoryPriority;
import com.zyonicsoftware.minereaper.redeugene.RedEugene;
import com.zyonicsoftware.minereaper.signal.allocator.Allocation;
import com.zyonicsoftware.minereaper.signal.allocator.Allocator;
import com.zyonicsoftware.minereaper.signal.caller.SignalCallRegistry;
import com.zyonicsoftware.minereaper.signal.caller.SignalCaller;
import com.zyonicsoftware.minereaper.signal.connection.Connection;
import com.zyonicsoftware.minereaper.signal.exception.SignalException;
import com.zyonicsoftware.minereaper.signal.inspector.IPV4AddressInspector;
import com.zyonicsoftware.minereaper.signal.packet.Packet;
import com.zyonicsoftware.minereaper.signal.scheduler.RedEugeneScheduler;
import com.zyonicsoftware.minereaper.signal.signal.SignalProvider;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Server extends Connection {

    private final int port;
    private ServerSocket serverSocket;
    private ServerSocketAcceptingThread serverSocketAcceptingThread;
    private final long scheduleDelay;
    private final int timeout;

    /**
     * @param port                to bound the ServerSocket on your own port.
     * @param signalCaller        create your own event caller to receive message from Signal ( e.g signal
     *                            has detect the package is to large or an client has been disconnected or else)
     * @param acceptedIpAddresses only allowed ip addresses can connect
     * @param scheduleDelay       this is the delay how long the input and output stream wait before receive
     *                            or send packets ( The lower the delay is the more cpu power the api will consume) The best
     *                            delay I have tried is 60ms.
     * @param coreSize            How many threads your using for input and output threads
     * @param timeout             set the timeout, how long the server wait before her timeout a client
     */
    public Server(
            final int port,
            @NotNull final Class<? extends SignalCaller> signalCaller,
            @NotNull final String[] acceptedIpAddresses,
            final long scheduleDelay,
            final int coreSize,
            final int timeout) {
        this.port = port;
        this.scheduleDelay = scheduleDelay;
        this.timeout = timeout;
        SignalCallRegistry.registerReferenceCaller(signalCaller);
        RedEugeneScheduler.setRedEugene(
                new RedEugene("SignalServerPool", coreSize, false, EugeneFactoryPriority.NORM));
        IPV4AddressInspector.addIpv4Addresses(Arrays.asList(acceptedIpAddresses));
    }

    /**
     * @param port                to bound the ServerSocket on your own port.
     * @param signalCaller        create your own event caller to receive message from Signal ( e.g signal
     *                            has detect the package is to large or an client has been disconnected or else)
     * @param acceptedIpAddresses only allowed ip addresses can connect
     * @param scheduleDelay       this is the delay how long the input and output stream wait before receive
     *                            or send packets ( The lower the delay is the more cpu power the api will consume) The best
     *                            delay I have tried is 60ms.
     * @param redEugene           is bind an other thread pool on this reference
     * @param timeout             set the timeout, how long the server wait before her timeout a client
     */
    public Server(
            final int port,
            @NotNull final Class<? extends SignalCaller> signalCaller,
            @NotNull final String[] acceptedIpAddresses,
            final long scheduleDelay,
            final RedEugene redEugene,
            final int timeout) {
        this.port = port;
        this.scheduleDelay = scheduleDelay;
        this.timeout = timeout;
        SignalCallRegistry.registerReferenceCaller(signalCaller);
        RedEugeneScheduler.setRedEugene(redEugene);
        IPV4AddressInspector.addIpv4Addresses(Arrays.asList(acceptedIpAddresses));
        if (timeout <= 10000) {
            throw new SignalException(SignalProvider.getSignalProvider().getTimeoutThrowsAnException());
        }
    }

    @Override
    public void connect() throws IOException {
        // check if serverSocket is initialised
        if (this.serverSocket == null) {
            // initialise serverSocket
            this.serverSocket = new ServerSocket(this.port);
            // this.serverSocket.setSoTimeout(this.timeout);
        }
        // start accepting clients
        this.serverSocketAcceptingThread =
                new ServerSocketAcceptingThread(
                        "ServerSocketAcceptingThread",
                        TimeUnit.MILLISECONDS,
                        1,
                        this.serverSocket,
                        this.scheduleDelay,
                        this.timeout);
        RedEugeneScheduler.getRedEugeneIntroduction()
                .scheduleWithoutDelay(this.serverSocketAcceptingThread);
        Allocator.setAllocation(Allocation.CLIENT_FROM_SERVER_SIDE);
    }

    @Override
    public void disconnect() throws IOException {
        // disconnect all clients
        this.disconnectAllClients();
        // destroy server accepting thread
        this.serverSocketAcceptingThread.interrupt();
        // check if serverSocket is closed
        if (!this.serverSocket.isClosed()) {
            // disconnect serverSocket
            this.serverSocket.close();
        }
        try {
            RedEugeneScheduler.getRedEugeneIntroduction().shutdownPool(1);
        } catch (final InterruptedException exception) {
            throw new SignalException(exception);
        }
    }

    /**
     * @return your custom timeout value
     * @apiNote your custom value must be above 10000ms
     */
    public int getTimeout() {
        return this.timeout;
    }

    /**
     * @return your custom port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * @param packet find the packet
     * @param uuid   specify the client send to client
     */
    public void sendToClient(final Packet packet, final UUID uuid) {
        this.serverSocketAcceptingThread.sendToClient(packet, uuid);
    }

    /**
     * @param packet find the packet send to all clients
     */
    public void sendToAllClients(final Packet packet) {
        this.serverSocketAcceptingThread.sendToAllClients(packet);
    }

    /**
     * @param packet find the packet
     * @param uuid   specify the client send to client
     * @return future
     */
    public CompletableFuture<Void> sendToClientAsync(final Packet packet, final UUID uuid) {
        return CompletableFuture.runAsync(
                () -> this.serverSocketAcceptingThread.sendToClient(packet, uuid));
    }

    /**
     * @param packet find the packet send to all clients
     * @return future
     */
    public CompletableFuture<Void> sendToAllClientsAsync(final Packet packet) {
        return CompletableFuture.runAsync(
                () -> this.serverSocketAcceptingThread.sendToAllClients(packet));
    }

    /**
     * @param uuid specify the client
     */
    public void disconnectClient(final UUID uuid) {
        this.serverSocketAcceptingThread.disconnectClient(uuid);
    }

    /**
     * all clients will be closed
     */
    public void disconnectAllClients() {
        this.serverSocketAcceptingThread.disconnectAllClients();
    }
}
