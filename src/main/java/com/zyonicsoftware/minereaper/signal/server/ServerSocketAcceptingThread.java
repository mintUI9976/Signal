package com.zyonicsoftware.minereaper.signal.server;

import com.zyonicsoftware.minereaper.signal.client.Client;
import com.zyonicsoftware.minereaper.signal.exception.SignalException;
import com.zyonicsoftware.minereaper.signal.message.RegisteredMessenger;
import com.zyonicsoftware.minereaper.signal.message.SignalMessages;
import com.zyonicsoftware.minereaper.signal.packet.Packet;
import com.zyonicsoftware.minereaper.signal.packet.ahead.UpdateUUIDPacket;
import com.zyonicsoftware.minereaper.signal.signal.SignalProvider;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServerSocketAcceptingThread extends Thread {

    private final ServerSocket serverSocket;
    private final List<Client> clients = new ArrayList<>();
    private final Class<? extends SignalMessages> signalMessages = RegisteredMessenger.get();

    public ServerSocketAcceptingThread(final ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        super.run();
        try {
            while (true) {
                if (this.serverSocket.isClosed()) {
                    this.interrupt();
                    break;
                } else {
                    final Socket socket = this.serverSocket.accept();
                    final Client client = new Client(socket);
                    client.connect();
                    this.clients.add(client);
                    this.signalMessages.getDeclaredConstructor(String.class).newInstance(this.toString()).acceptSocketConnectionMessage(SignalProvider.getSignalProvider().getAcceptSocketConnectionMessage());
                    //update connectionUUID on client side
                    final UpdateUUIDPacket updateUUIDPacket = new UpdateUUIDPacket(client.getConnectionUUID().get());
                    client.send(updateUUIDPacket);
                }
            }
        } catch (final IOException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException exception) {
            throw new SignalException(exception);
        }
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
        this.clients.stream().filter(client -> client.getConnectionUUID().get().equals(uuid)).forEach(client -> {
            try {
                System.out.println("[JavaSocketAPI] Client: " + client.getConnectionUUID().get() + " will be disconnected!");
                client.disconnect();
            } catch (final IOException exception) {
                throw new SignalException(exception);
            }
        });
    }

    public void disconnectAllClients() {
        //disconnect all clients
        System.out.println("[JavaSocketAPI] All Clients will be disconnected!");
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
