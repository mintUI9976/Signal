package com.zyonicsoftware.minereaper.signal.example;

import com.zyonicsoftware.minereaper.signal.buffer.ReadingByteBuffer;
import com.zyonicsoftware.minereaper.signal.buffer.WritingByteBuffer;
import com.zyonicsoftware.minereaper.signal.compression.Compression;
import com.zyonicsoftware.minereaper.signal.packet.Packet;

import java.util.UUID;

public class ExamplePacket extends Packet {

    byte[] message;

    public ExamplePacket(final UUID connectionUUID) {
        super(connectionUUID);
    }

    public ExamplePacket(final byte[] message) {
        super(null);
        this.message = message;
    }

    @Override
    public void send(final WritingByteBuffer writingByteBuffer) {
        writingByteBuffer.writeByteArray(Compression.getCompression().compress(this.message));
    }

    @Override
    public void receive(final ReadingByteBuffer readingByteBuffer) {
        this.message = readingByteBuffer.readBytes();
        final String byteArrayMessageToString = Compression.getCompression().decompressAsString(this.message);
        switch (byteArrayMessageToString) {
            case "Client -> Hello Server":
                ExampleServer.sendMessage();
                break;
            case "Server -> Hello Client":
                ExampleClient.sendMessage();
                break;
        }
        System.out.println(byteArrayMessageToString);
    }
}
