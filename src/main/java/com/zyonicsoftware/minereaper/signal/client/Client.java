package com.zyonicsoftware.minereaper.signal.client;

import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.SchedulerConfig;
import com.zyonicsoftware.minereaper.signal.caller.SignalCallRegistry;
import com.zyonicsoftware.minereaper.signal.caller.SignalCaller;
import com.zyonicsoftware.minereaper.signal.connection.Connection;
import com.zyonicsoftware.minereaper.signal.incoming.InputStreamThread;
import com.zyonicsoftware.minereaper.signal.outgoing.OutputStreamThread;
import com.zyonicsoftware.minereaper.signal.packet.Packet;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

public class Client extends Connection {

    private String hostname;
    private int port;
    private Socket socket;
    private InputStreamThread inputStreamThread;
    private OutputStreamThread outputStreamThread;
    private final Scheduler scheduler;
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
     * @param hostname      to bound on right hostname.
     * @param port          to bound the Socket on the right server port.
     * @param signalCaller  create your own event caller to receive message from Signal ( e.g signal has detect the package is to large or an client has been disconnected or else)
     * @param scheduler     you can create your own scheduler with Wisp technology / check out https://github.com/Coreoz/Wisp
     * @param scheduleDelay this is the delay how long the input and output stream wait before receive or send packets ( The lower the delay is the more cpu power the api will consume)
     *                      The best delay I have tried is 60ms.
     */

    public Client(@NotNull final String hostname, final int port, @NotNull final Class<? extends SignalCaller> signalCaller, @NotNull final Scheduler scheduler, final long scheduleDelay) {
        this.hostname = hostname;
        this.port = port;
        SignalCallRegistry.registerReferenceCaller(signalCaller);
        this.scheduler = scheduler;
        this.scheduleDelay = scheduleDelay;
    }

    /**
     * @param hostname      to bound on right hostname.
     * @param port          to bound the ServerSocket on your own port.
     * @param signalCaller  create your own event caller to receive message from Signal ( e.g signal has detect the package is to large or an client has been disconnected or else)
     * @param minThreads    this set the minimal usable threads for Wisp scheduler /  check out https://github.com/Coreoz/Wisp
     * @param maxThreads    this set the maximal usable threads for Wisp scheduler  / check out https://github.com/Coreoz/Wisp
     * @param scheduleDelay this is the delay how long the input and output stream wait before receive or send packets ( The lower the delay is the more cpu power the api will consume)
     *                      The best delay I have tried is 60ms.
     */

    public Client(@NotNull final String hostname, final int port, @NotNull final Class<? extends SignalCaller> signalCaller, final int minThreads, final int maxThreads, final long scheduleDelay) {
        this.hostname = hostname;
        this.port = port;
        SignalCallRegistry.registerReferenceCaller(signalCaller);
        this.scheduler = new Scheduler(SchedulerConfig.builder().minThreads(minThreads).maxThreads(maxThreads).build());
        this.scheduleDelay = scheduleDelay;
    }

    public Client(@NotNull final Socket socket, @NotNull final Class<? extends SignalCaller> signalCaller, @NotNull final Scheduler scheduler, final long scheduleDelay) {
        this.socket = socket;
        SignalCallRegistry.registerReferenceCaller(signalCaller);
        this.scheduler = scheduler;
        this.scheduleDelay = scheduleDelay;
    }


    @Override
    public void connect() throws IOException {
        //check if socket is initialised
        if (this.socket == null) {
            //initialise socket
            this.socket = new Socket(this.hostname, this.port);
        }
        //start reading and writing
        this.inputStreamThread = new InputStreamThread(this);
        this.inputStreamThread.run();
        this.outputStreamThread = new OutputStreamThread(this);
        this.outputStreamThread.run();
    }

    @Override
    public void disconnect() throws IOException {
        //interrupt reading and writing
        this.inputStreamThread.interrupt();
        this.outputStreamThread.interrupt();

        //check if socket is closed
        if (!this.socket.isClosed()) {
            //closed socket
            this.socket.close();
        }
    }

    public Scheduler getScheduler() {
        return this.scheduler;
    }

    public void send(final Packet packet) {
        this.outputStreamThread.send(packet);
    }
}
