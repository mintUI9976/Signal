/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.example;

import com.zyonicsoftware.minereaper.signal.buffer.ReadingByteBuffer;
import com.zyonicsoftware.minereaper.signal.buffer.WritingByteBuffer;
import com.zyonicsoftware.minereaper.signal.compression.Compression;
import com.zyonicsoftware.minereaper.signal.packet.Packet;

import java.util.UUID;

/**
 * @author Niklas Griese
 * @see com.zyonicsoftware.minereaper.signal.packet.Packet
 * @see com.zyonicsoftware.minereaper.signal.packet.PacketRegistry
 * @see Compression
 * @see ReadingByteBuffer
 * @see WritingByteBuffer
 */
public class ExamplePacket extends Packet {

  byte[] message;

  /** @param connectionUUID to check which client will receive/send this packet */
  public ExamplePacket(final UUID connectionUUID) {
    super(connectionUUID);
  }

  /** @param message will be used to take your message and send or receive them */
  public ExamplePacket(final byte[] message) {
    super(null);
    this.message = message;
  }

  /** @param writingByteBuffer will be write all stuff in an buffer to allocate them */
  @Override
  public void send(final WritingByteBuffer writingByteBuffer) {
    writingByteBuffer.writeByteArray(Compression.getCompression().compress(this.message));
  }

  /**
   * @param readingByteBuffer will read all stuff from the buffer and call your receive method with
   *     your preferences
   */
  @Override
  public void receive(final ReadingByteBuffer readingByteBuffer) {
    this.message = readingByteBuffer.readBytes();
    final String byteArrayMessageToString =
        Compression.getCompression().decompressAsString(this.message);
    switch (byteArrayMessageToString) {
      case "Client -> Hello Server":
        ExampleServer.sendMessage();
        break;
      case "Server -> Hello Client":
        ExampleClient.sendMessage();
        break;
    }
    // System.out.println(byteArrayMessageToString);
  }
}
