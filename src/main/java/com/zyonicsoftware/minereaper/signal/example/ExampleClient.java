/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.example;

import com.zyonicsoftware.minereaper.signal.client.Client;
import com.zyonicsoftware.minereaper.signal.packet.PacketRegistry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Niklas Griese
 * @see Client
 * @see PacketRegistry
 * @see ExamplePacket
 * @see ExampleServer
 */
public class ExampleClient {

    /**
     * init static reference of Client object
     */
    private static Client client;

    public static void main(final String[] args) throws IOException {
        // add shutdown hook
        ExampleClient.addShutdownHook();
        // register packets
        ExampleClient.registerPackets();
        // call client
        ExampleClient.executeClient();
        // send first message
        ExampleClient.sendMessage();
    }

    /**
     * @apiNote register custom packet
     */
    private static void registerPackets() {
        PacketRegistry.registerPacket(ExamplePacket.class);
    }

    /**
     * @throws IOException when client throw an exception
     * @apiNote create an new client object
     * @see Client
     */
    private static void executeClient() throws IOException {
        ExampleClient.client =
                new Client("localhost", 9976, ExampleSignalMessageInstance.class, 60, true, 20 * 1000);
        ExampleClient.client.connect();
        System.out.println("Client has been connect on port: " + ExampleClient.client.getPort());
    }

    /**
     * create an String compare the string to an byte array then the byte array will be try to
     * compress in an tiny byte array to safe a lot of unnecessary tcp traffic
     *
     * @see ExamplePacket
     * @see com.zyonicsoftware.minereaper.signal.compression.Compression
     */
    public static void sendMessage() {
        ExampleClient.client.send(
                new ExamplePacket("Client -> Hello Server".getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * @throws IOException when client throw an exception
     * @see Client
     */
    public static void disconnectClient() throws IOException {
        ExampleClient.client.disconnect();
        System.out.println("Client has been disconnected.");
    }

    /**
     * create an hook to call shutdown
     *
     * @see Runtime
     * @see Client
     */
    public static void addShutdownHook() {
        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(
                                () -> {
                                    try {
                                        ExampleClient.disconnectClient();
                                    } catch (final IOException exception) {
                                        exception.printStackTrace();
                                    }
                                }));
    }

    /**
     * @return the created reference of Client object
     */
    public static Client getClient() {
        return ExampleClient.client;
    }
}
