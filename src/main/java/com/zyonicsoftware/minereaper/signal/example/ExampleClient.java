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

public class ExampleClient {

  private static Client client;

  public static void main(final String[] args) throws IOException {
    ExampleClient.addShutdownHook();
    ExampleClient.registerPackets();
    ExampleClient.executeClient();
    ExampleClient.sendMessage();
  }

  private static void registerPackets() {
    PacketRegistry.registerPacket(ExamplePacket.class);
  }

  private static void executeClient() throws IOException {
    ExampleClient.client = new Client("localhost", 9976, ExampleSignalMessageInstance.class, 60);
    ExampleClient.client.connect();
    System.out.println("Client has been connect on port: " + ExampleClient.client.getPort());
  }

  public static void sendMessage() {
    ExampleClient.client.send(
        new ExamplePacket("Client -> Hello Server".getBytes(StandardCharsets.UTF_8)));
  }

  public static void disconnectClient() throws IOException {
    ExampleClient.client.disconnect();
    System.out.println("Client has been disconnected.");
  }

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

  public static Client getClient() {
    return ExampleClient.client;
  }
}
