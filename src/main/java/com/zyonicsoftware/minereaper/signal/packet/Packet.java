package com.zyonicsoftware.minereaper.signal.packet;

import com.zyonicsoftware.minereaper.signal.buffer.ReadingByteBuffer;
import com.zyonicsoftware.minereaper.signal.buffer.WritingByteBuffer;

import java.util.UUID;

public abstract class Packet {

    private final UUID connectionUUID;

    public UUID getConnectionUUID() {
        return this.connectionUUID;
    }

    public Packet(final UUID connectionUUID) {
        this.connectionUUID = connectionUUID;
    }

    public abstract void send(WritingByteBuffer writingByteBuffer);

    public abstract void receive(ReadingByteBuffer readingByteBuffer);

}
