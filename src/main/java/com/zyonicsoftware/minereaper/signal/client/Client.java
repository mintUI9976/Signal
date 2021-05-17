package com.zyonicsoftware.minereaper.signal.client;

import com.coreoz.wisp.Scheduler;
import com.coreoz.wisp.SchedulerConfig;
import com.zyonicsoftware.minereaper.signal.caller.SignalCallRegistry;
import com.zyonicsoftware.minereaper.signal.caller.SignalCaller;
import com.zyonicsoftware.minereaper.signal.connection.Connection;
import com.zyonicsoftware.minereaper.signal.incoming.InputStreamThread;
import com.zyonicsoftware.minereaper.signal.outgoing.OutputStreamThread;
import com.zyonicsoftware.minereaper.signal.packet.Packet;

import java.io.IOException;
import java.net.Socket;

public class Client extends Connection {

    private String hostname;
    private int port;
    private Socket socket;
    private InputStreamThread inputStreamThread;
    private OutputStreamThread outputStreamThread;
    private final Scheduler scheduler;

    public String getHostname() {
        return this.hostname;
    }

    public int getPort() {
        return this.port;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public Client(final String hostname, final int port, final Class<? extends SignalCaller> signalCaller, final Scheduler scheduler) {
        this.hostname = hostname;
        this.port = port;
        SignalCallRegistry.registerReferenceCaller(signalCaller);
        this.scheduler = scheduler;
    }

    public Client(final String hostname, final int port, final Class<? extends SignalCaller> signalCaller, final int minThreads, final int maxThreads) {
        this.hostname = hostname;
        this.port = port;
        SignalCallRegistry.registerReferenceCaller(signalCaller);
        this.scheduler = new Scheduler(SchedulerConfig.builder().minThreads(minThreads).maxThreads(maxThreads).build());
    }

    public Client(final Socket socket, final Class<? extends SignalCaller> signalCaller, final Scheduler scheduler) {
        this.socket = socket;
        SignalCallRegistry.registerReferenceCaller(signalCaller);
        this.scheduler = scheduler;
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
