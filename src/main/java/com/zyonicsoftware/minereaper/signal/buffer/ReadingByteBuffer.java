/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.buffer;

import org.boon.primitive.InputByteArray;

import java.util.UUID;

/**
 * @author Niklas Griese
 * @see InputByteArray
 * @see java.nio.Buffer
 * @see java.io.Serializable
 */
public class ReadingByteBuffer {
    /**
     * @implNote init an final InputByteArray
     */
    private final InputByteArray inputByteArray;

    public ReadingByteBuffer(final byte... bytes) {
        this.inputByteArray = new InputByteArray(bytes);
    }

    /**
     * @return a boolean value from InputByteArray
     */
    public boolean readBoolean() {
        // read boolean
        return this.inputByteArray.readBoolean();
    }

    /**
     * @return a byte value from InputByteArray
     */
    public byte readByte() {
        // read byte

        return this.inputByteArray.readByte();
    }

    /**
     * @return a byte array value from InputByteArray
     */
    public byte[] readBytes() {
        return this.inputByteArray.readLargeByteArray();
    }

    /**
     * @return a short value from InputByteArray
     */
    public short readShort() {
        // read short
        return this.inputByteArray.readShort();
    }

    /**
     * @return an int value from InputByteArray
     */
    public int readInt() {
        // read int
        return this.inputByteArray.readInt();
    }

    /**
     * @return a long value from InputByteArray
     */
    public long readLong() {
        // read long
        return this.inputByteArray.readLong();
    }

    /**
     * @return a float value from InputByteArray
     */
    public float readFloat() {
        // read float
        return this.inputByteArray.readFloat();
    }

    /**
     * @return a double value from InputByteArray
     */
    public double readDouble() {
        // read double
        return this.inputByteArray.readDouble();
    }

    /**
     * @return a char value from InputByteArray
     */
    public char readChar() {
        // read chars
        return this.inputByteArray.readChar();
    }

    /**
     * @return a big string value from InputByteArray
     */
    public String readLargeString() {
        // read string
        return this.inputByteArray.readLargeString();
    }

    /**
     * @return a uuid value above the readLargeString method
     */
    public UUID readUUID() {
        // read uuid
        return UUID.fromString(this.readLargeString());
    }
}
