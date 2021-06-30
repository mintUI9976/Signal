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

/**
 * @author Niklas Griese
 * @see ByteBuf
 * @see java.io.Serializable
 */
public class WritingByteBuffer {
  /** @implNote init an final ByteBuf */
  private final ByteBuf byteBuf;

  public WritingByteBuffer() {
    this.byteBuf = ByteBuf.create(0);
  }

  /** @param value adds a boolean value to the byte buf */
  public void writeBoolean(final boolean value) {
    // writing boolean
    this.byteBuf.writeBoolean(value);
  }

  /** @param bytes adds a byte array value to the byte buf */
  public void writeByteArray(final byte[] bytes) {
    this.byteBuf.writeLargeByteArray(bytes);
  }

  /** @param value adds a byte value to the byte buf */
  public void writeByte(final byte value) {
    // writing byte
    this.byteBuf.writeByte(value);
  }

  /** @param value adds a short value to the byte buf */
  public void writeShort(final short value) {
    // writing short
    this.byteBuf.writeShort(value);
  }

  /** @param value adds an int value to the byte buf */
  public void writeInt(final int value) {
    // writing int
    this.byteBuf.writeInt(value);
  }

  /** @param value adds a long value to the byte buf */
  public void writeLong(final long value) {
    // writing long
    this.byteBuf.writeLong(value);
  }

  /** @param value adds a float value to the byte buf */
  public void writeFloat(final float value) {
    // writing float
    this.byteBuf.writeFloat(value);
  }

  /** @param value adds a double value to the byte buf */
  public void writeDouble(final double value) {
    // writing double
    this.byteBuf.writeDouble(value);
  }

  /** @param value adds a char value to the byte buf */
  public void writeChar(final char value) {
    // writing char
    this.byteBuf.writeChar(value);
  }

  /** @param value adds a string to the byte buf */
  public void writeString(final String value) {

    // writing string*/
    this.byteBuf.writeLargeString(value);
  }

  /** @param value adds an uuid value to the byte buf via write string method */
  public void writeUUID(final UUID value) {
    // check value
    this.writeString(value.toString());
    // writing uuid
  }

  /** @return all byte buf information as byte array */
  public byte[] toBytes() {
    // convert to byte array
    return this.byteBuf.toBytes();
  }
}
