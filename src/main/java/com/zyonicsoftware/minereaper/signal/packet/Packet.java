/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.packet;

import com.zyonicsoftware.minereaper.signal.buffer.ReadingByteBuffer;
import com.zyonicsoftware.minereaper.signal.buffer.WritingByteBuffer;

import java.util.UUID;

/**
 * @author Niklas Griese
 * @see ReadingByteBuffer
 * @see WritingByteBuffer
 */
public abstract class Packet {

  /** init uuid instance */
  private final UUID connectionUUID;

  /** @return the current uuid of client receiver / sender */
  public UUID getConnectionUUID() {
    return this.connectionUUID;
  }

  /** @param connectionUUID init uuid to specify sender and receiver */
  public Packet(final UUID connectionUUID) {
    this.connectionUUID = connectionUUID;
  }

  /** @param writingByteBuffer called the buffer to send the packet */
  public abstract void send(WritingByteBuffer writingByteBuffer);

  /** @param readingByteBuffer called the buffer to receive the packet */
  public abstract void receive(ReadingByteBuffer readingByteBuffer);
}
