package com.zyonicsoftware.minereaper.signal.example;

import com.coreoz.wisp.stats.SchedulerStats;
import com.zyonicsoftware.minereaper.signal.packet.PacketRegistry;
import com.zyonicsoftware.minereaper.signal.server.Server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ExampleServer {

    private static Server server;

    public static void main(final String[] args) throws IOException {
        ExampleServer.addShutdownHook();
        ExampleServer.registerPackets();
        ExampleServer.executeServer();
    }

    private static String[] registerAllowedIpv4Addresses() {
        return new String[]{"localhost", "127.0.0.1"};
    }

    private static void registerPackets() {
        PacketRegistry.registerPacket(ExamplePacket.class);
    }

    private static void executeServer() throws IOException {
        ExampleServer.server = new Server(9976, ExampleSignalMessageInstance.class, ExampleServer.registerAllowedIpv4Addresses(), 2, 4, 60);
        final SchedulerStats schedulerStats = ExampleServer.server.getScheduler().stats();
        ExampleServer.server.connect();
        System.out.println("Server has been bound on port: " + ExampleServer.server.getPort());
        System.out.println("Server started with " + schedulerStats.getThreadPoolStats().getMinThreads() + " minimal Threads and " + schedulerStats.getThreadPoolStats().getMaxThreads() + " maximal Threads.");
    }

    public static void sendMessage() {
        ExampleServer.server.sendToAllClients(new ExamplePacket("Server -> Hello Client".getBytes(StandardCharsets.UTF_8)));
    }

    public static void disconnectServer() throws IOException {
        ExampleServer.server.disconnectAllClients();
        ExampleServer.server.disconnect();
        System.out.println("Server has been disconnected.");
    }

    public static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                ExampleServer.disconnectServer();
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }));
    }

    public static Server getServer() {
        return ExampleServer.server;
    }
}
