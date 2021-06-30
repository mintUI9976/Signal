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
import com.zyonicsoftware.minereaper.signal.server.Server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Niklas Griese
 * @see PacketRegistry
 * @see com.zyonicsoftware.minereaper.signal.packet.Packet
 * @see Server
 */
public class ExampleServer {

  /** init static reference of Client object */
  private static Server server;

  public static void main(final String[] args) throws IOException {
    // add shutdown hook
    ExampleServer.addShutdownHook();
    // register packets
    ExampleServer.registerPackets();
    // call server
    ExampleServer.executeServer();
  }

  /** @return return your allowed addresses */
  private static String[] registerAllowedIpv4Addresses() {
    return new String[] {"localhost", "127.0.0.1"};
  }

  /** @apiNote register custom packet */
  private static void registerPackets() {
    PacketRegistry.registerPacket(ExamplePacket.class);
  }

  /**
   * @apiNote create an new server object
   * @see Client
   * @throws IOException when server throw an exception
   */
  private static void executeServer() throws IOException {
    ExampleServer.server =
        new Server(
            9976,
            ExampleSignalMessageInstance.class,
            ExampleServer.registerAllowedIpv4Addresses(),
            60,
            10,
            20 * 1000);
    ExampleServer.server.connect();
    System.out.println("Server has been bound on port: " + ExampleServer.server.getPort());
  }

  /**
   * create an String compare the string to an byte array then the byte array will be try to
   * compress in an tiny byte array to safe a lot of unnecessary tcp traffic
   *
   * @see ExamplePacket
   * @see com.zyonicsoftware.minereaper.signal.compression.Compression
   */
  public static void sendMessage() {
    ExampleServer.server.sendToAllClients(
        new ExamplePacket("Server -> Hello Client".getBytes(StandardCharsets.UTF_8)));
  }

  /**
   * @throws IOException when server throw an exception
   * @see Server
   */
  public static void disconnectServer() throws IOException {
    ExampleServer.server.disconnect();
    System.out.println("Server has been disconnected.");
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
