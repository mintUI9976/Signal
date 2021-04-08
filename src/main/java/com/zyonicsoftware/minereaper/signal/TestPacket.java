package com.zyonicsoftware.minereaper.signal;

import com.zyonicsoftware.minereaper.signal.buffer.ReadingByteBuffer;
import com.zyonicsoftware.minereaper.signal.buffer.WritingByteBuffer;
import com.zyonicsoftware.minereaper.signal.packet.Packet;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class TestPacket extends Packet {

    private byte[] alloc;

    public TestPacket(final UUID connectionUUID) {
        super(connectionUUID);
    }

    public TestPacket(final byte[] alloc) {
        super(null);
        this.alloc = alloc;
    }

    @Override
    public void send(final WritingByteBuffer writingByteBuffer) {
        writingByteBuffer.writeByteArray(this.alloc);
        System.out.println("WRITE SUCCESS");
    }

    @Override
    public void receive(final ReadingByteBuffer readingByteBuffer) {
        this.alloc = readingByteBuffer.readBytes();
        System.out.println("RECEIVE SUCCESS - " + new String(Core.decompress(this.alloc), StandardCharsets.UTF_8));
    }

   /* public static byte[] decompress(final byte[] compressedTxt) {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (final OutputStream ios = new InflaterOutputStream(os)) {
            ios.write(compressedTxt);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }

        return os.toByteArray();
    }*/
}
