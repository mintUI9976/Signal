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
import com.zyonicsoftware.minereaper.signal.allocator.Allocation;
import com.zyonicsoftware.minereaper.signal.allocator.Allocator;
import com.zyonicsoftware.minereaper.signal.caller.SignalCallRegistry;
import com.zyonicsoftware.minereaper.signal.caller.SignalCaller;
import com.zyonicsoftware.minereaper.signal.connection.Connection;
import com.zyonicsoftware.minereaper.signal.exception.SignalException;
import com.zyonicsoftware.minereaper.signal.incoming.InputStreamThread;
import com.zyonicsoftware.minereaper.signal.keepalive.KeepAliveThread;
import com.zyonicsoftware.minereaper.signal.outgoing.OutputStreamThread;
import com.zyonicsoftware.minereaper.signal.packet.Packet;
import com.zyonicsoftware.minereaper.signal.packet.ahead.ClientDisconnectPacket;
import com.zyonicsoftware.minereaper.signal.scheduler.RedEugeneScheduler;
import com.zyonicsoftware.minereaper.signal.signal.SignalProvider;
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
    private KeepAliveThread keepAliveThread;
    private final long scheduleDelay;
    private final int timeout;
    private long incomingPackets;
    private long outgoingPackets;
    private boolean disconnected;
    private boolean connected;
    private boolean performSendAsync;

    public long getIncomingPackets() {
        return this.incomingPackets;
    }

    public void setIncomingPackets(final long incomingPackets) {
        this.incomingPackets = incomingPackets;
    }

    public long getOutgoingPackets() {
        return this.outgoingPackets;
    }

    public void setOutgoingPackets(final long outgoingPackets) {
        this.outgoingPackets = outgoingPackets;
    }

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
     * @param hostname      to bound on right hostname.
     * @param port          to bound the Socket on the right server port.
     * @param signalCaller  create your own event caller to receive message from Signal ( e.g signal
     *                      has detect the package is to large or an client has been disconnected or else)
     * @param scheduleDelay this is the delay how long the input and output stream wait before receive
     *                      or send packets ( The lower the delay is the more cpu power the api will consume) The best
     *                      delay I have tried is 60ms.
     * @param timeout       set the timeout, how long the client have time before timeout
     * @apiNote your custom value must be above 10000ms
     */
    public Client(
            @NotNull final String hostname,
            final int port,
            @NotNull final Class<? extends SignalCaller> signalCaller,
            final long scheduleDelay,
            final boolean performSendAsync,
            final int timeout) {
        this.hostname = hostname;
        this.port = port;
        RedEugeneScheduler.setRedEugene(
                new RedEugene("SignalClientPool", 3, false, EugeneFactoryPriority.NORM));
        SignalCallRegistry.registerReferenceCaller(signalCaller);
        this.scheduleDelay = scheduleDelay;
        this.performSendAsync = performSendAsync;
        this.timeout = timeout;
        Allocator.setAllocation(Allocation.CLIENT_SIDE);
        if (timeout <= 10000) {
            throw new SignalException(SignalProvider.getSignalProvider().getTimeoutThrowsAnException());
        }
    }

    /**
     * @param hostname      to bound on right hostname.
     * @param port          to bound the Socket on the right server port.
     * @param signalCaller  create your own event caller to receive message from Signal ( e.g signal
     *                      has detect the package is to large or an client has been disconnected or else)
     * @param scheduleDelay this is the delay how long the input and output stream wait before receive
     *                      or send packets ( The lower the delay is the more cpu power the api will consume) The best
     *                      delay I have tried is 60ms.
     * @param timeout       set the timeout, how long the client have time before timeout
     * @param redEugene     set your own redEugene pool
     * @apiNote your custom value must be above 10000ms
     */
    public Client(
            @NotNull final String hostname,
            final int port,
            @NotNull final Class<? extends SignalCaller> signalCaller,
            final RedEugene redEugene,
            final long scheduleDelay,
            final boolean performSendAsync,
            final int timeout) {
        this.hostname = hostname;
        this.port = port;
        RedEugeneScheduler.setRedEugene(redEugene);
        SignalCallRegistry.registerReferenceCaller(signalCaller);
        this.scheduleDelay = scheduleDelay;
        this.performSendAsync = performSendAsync;
        this.timeout = timeout;
        Allocator.setAllocation(Allocation.CLIENT_SIDE);
        if (timeout <= 10000) {
            throw new SignalException(SignalProvider.getSignalProvider().getTimeoutThrowsAnException());
        }
    }

    /**
     * @param socket        use to connect server with client
     * @param scheduleDelay this is the delay how long the input and output stream wait before receive
     *                      * or send packets ( The lower the delay is the more cpu power the api will consume) The
     *                      best * delay I have tried is 60ms.
     * @param timeout       set the timeout, how long the client have time before timeout
     * @apiNote this object will be only called from Server object
     * @apiNote your custom value must be above 10000ms
     * @see com.zyonicsoftware.minereaper.signal.server.ServerSocketAcceptingThread
     * @see com.zyonicsoftware.minereaper.signal.server.Server
     */
    public Client(@NotNull final Socket socket, final long scheduleDelay, final int timeout) {
        this.socket = socket;
        this.scheduleDelay = scheduleDelay;
        this.timeout = timeout;
        if (timeout <= 10000) {
            throw new SignalException(SignalProvider.getSignalProvider().getTimeoutThrowsAnException());
        }
    }

    /**
     * The connect method will be override from original
     *
     * @throws IOException will be called when socket throw an exception
     * @see Connection refernce object
     * @see KeepAliveThread
     * @see com.zyonicsoftware.minereaper.signal.packet.ahead.KeepAlivePacket will be only send from
     * CLIENT_SIDE and received CLIENT_FROM_SERVER_SIDE
     */
    @Override
    public void connect() throws IOException {
        // check if socket is initialised
        if (this.socket == null) {
            // initialise socket
            this.socket = new Socket(this.hostname, this.port);
            // this.socket.setSoTimeout(this.timeout);
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
        if (Allocator.getAllocation().equals(Allocation.CLIENT_SIDE)) {
            this.keepAliveThread =
                    new KeepAliveThread(
                            "KeepAliveThread-" + random, TimeUnit.MILLISECONDS, this.timeout - 1000, this);
            RedEugeneScheduler.getRedEugeneIntroduction().scheduleWithoutDelay(this.keepAliveThread);
        }
        this.connected = true;
    }

    /**
     * The disconnect method will be override from original
     *
     * @throws IOException will be called when socket throw an exception
     * @see Connection refernce object
     */
    @Override
    public void disconnect() throws IOException {
        // interrupt the keep alive thread
        if (Allocator.getAllocation().equals(Allocation.CLIENT_SIDE)) {
            try {
                this.send(new ClientDisconnectPacket());
                Thread.sleep(150);
            } catch (final InterruptedException exception) {
                exception.printStackTrace();
            }
            this.keepAliveThread.interrupt();
        }

        // interrupt reading and writing thread
        this.inputStreamThread.interrupt();
        this.outputStreamThread.interrupt();

        // check if socket is closed
        if (!this.socket.isClosed()) {
            // closed socket
            this.socket.close();
        }
        this.connected = false;
        this.disconnected = true;
    }

    /**
     * @return your custom timeout value
     * @apiNote your custom value must be above 10000ms
     */
    public int getTimeout() {
        return this.timeout;
    }

    public boolean isDisconnected() {
        return this.disconnected;
    }

    public boolean isConnected() {
        return this.connected;
    }

    /**
     * @param packet adds the packet to list
     */
    public void send(final Packet packet) {
        if (!this.performSendAsync) {
            this.outputStreamThread.send(packet);
        } else {
            RedEugeneScheduler.getRedEugene().getRedThreadPool().execute(() -> this.outputStreamThread.send(packet));
        }
    }
}
