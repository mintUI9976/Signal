/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.buffer;

import org.boon.primitive.ByteBuf;

import java.util.UUID;

public class WritingByteBuffer {
  private final ByteBuf byteBuf;

  public WritingByteBuffer() {
    this.byteBuf = ByteBuf.create(0);
  }

  public void writeBoolean(final boolean value) {
    // writing boolean
    this.byteBuf.writeBoolean(value);
  }

  public void writeByteArray(final byte[] bytes) {
    this.byteBuf.writeLargeByteArray(bytes);
  }

  public void writeByte(final byte value) {
    // writing byte
    this.byteBuf.writeByte(value);
  }

  public void writeShort(final short value) {
    // writing short
    this.byteBuf.writeShort(value);
  }

  public void writeInt(final int value) {
    // writing int
    this.byteBuf.writeInt(value);
  }

  public void writeLong(final long value) {
    // writing long
    this.byteBuf.writeLong(value);
  }

  public void writeFloat(final float value) {
    // writing float
    this.byteBuf.writeFloat(value);
  }

  public void writeDouble(final double value) {
    // writing double
    this.byteBuf.writeDouble(value);
  }

  public void writeChar(final char value) {
    // writing char
    this.byteBuf.writeChar(value);
  }

  public void writeString(final String value) {

    // writing string*/
    this.byteBuf.writeLargeString(value);
  }

  public void writeUUID(final UUID value) {
    // check value
    this.writeString(value.toString());
    // writing uuid
  }

  public byte[] toBytes() {
    // convert to byte array
    return this.byteBuf.toBytes();
  }
}
