package com.zyonicsoftware.minereaper.signal.server;

import com.zyonicsoftware.minereaper.signal.connection.Connection;
import com.zyonicsoftware.minereaper.signal.packet.Packet;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Server extends Connection {

    private final int port;
    private ServerSocket serverSocket;
    private ServerSocketAcceptingThread serverSocketAcceptingThread;

    public int getPort() {
        return this.port;
    }

    public Server(final int port) {
        this.port = port;
    }


    @Override
    public void connect() throws IOException {
        //check if serverSocket is initialised
        if (this.serverSocket == null) {
            //initialise serverSocket
            this.serverSocket = new ServerSocket(this.port);
        }
        //start accepting clients
        this.serverSocketAcceptingThread = new ServerSocketAcceptingThread(this.serverSocket);
        this.serverSocketAcceptingThread.start();
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

    public void disconnectClient(final UUID uuid) throws IOException {
        //disconnect client
        this.serverSocketAcceptingThread.disconnectClient(uuid);
    }

    public void disconnectAllClients() throws IOException {
        //disconnect all clients
        this.serverSocketAcceptingThread.disconnectAllClients();
    }
}
