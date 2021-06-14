/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.packet.ahead;

import com.zyonicsoftware.minereaper.signal.buffer.ReadingByteBuffer;
import com.zyonicsoftware.minereaper.signal.buffer.WritingByteBuffer;
import com.zyonicsoftware.minereaper.signal.packet.Packet;

import java.util.UUID;

public class UpdateUUIDPacket extends Packet {
  public UpdateUUIDPacket(final UUID connectionUUID) {
    super(connectionUUID);
  }

  @Override
  public void send(final WritingByteBuffer writingByteBuffer) {}

  @Override
  public void receive(final ReadingByteBuffer readingByteBuffer) {}
}
