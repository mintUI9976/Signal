package com.zyonicsoftware.minereaper.signal.packet;

import com.zyonicsoftware.minereaper.signal.buffer.ReadingByteBuffer;
import com.zyonicsoftware.minereaper.signal.buffer.WritingByteBuffer;

import java.util.UUID;

public class UpdateUUIDPacket extends Packet {
    public UpdateUUIDPacket(final UUID connectionUUID) {
        super(connectionUUID);
    }

    @Override
    public void send(final WritingByteBuffer writingByteBuffer) {

    }

    @Override
    public void receive(final ReadingByteBuffer readingByteBuffer) {

    }
}
