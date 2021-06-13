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
