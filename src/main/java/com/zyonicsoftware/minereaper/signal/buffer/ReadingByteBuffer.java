package com.zyonicsoftware.minereaper.signal.buffer;


import org.boon.primitive.InputByteArray;

import java.util.UUID;

public class ReadingByteBuffer {
    //private final ByteBuffer byteBuffer;
    private final InputByteArray inputByteArray;

    public ReadingByteBuffer(final byte... bytes) {
        //this.byteBuffer = ByteBuffer.wrap(bytes);
        this.inputByteArray = new InputByteArray(bytes);
    }

    public boolean readBoolean() {
        //read boolean
        return this.inputByteArray.readBoolean();
    }

    public byte readByte() {
        //read byte

        return this.inputByteArray.readByte();
    }

    public byte[] readBytes() {
        return this.inputByteArray.readLargeByteArray();
    }

    public short readShort() {
        //read short
        return this.inputByteArray.readShort();
    }

    public int readInt() {
        //read int
        return this.inputByteArray.readInt();
    }

    public long readLong() {
        //read long
        return this.inputByteArray.readLong();
    }

    public float readFloat() {
        //read float
        return this.inputByteArray.readFloat();
    }

    public double readDouble() {
        //read double
        return this.inputByteArray.readDouble();
    }

    public char readChar() {
        //read chars
        return this.inputByteArray.readChar();
    }

    public String readLargeString() {
        //read string
        return this.inputByteArray.readLargeString();
    }

    public UUID readUUID() {
        //read uuid
        return UUID.fromString(this.readLargeString());
    }
}
