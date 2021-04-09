package com.zyonicsoftware.minereaper.signal.outgoing;

import com.zyonicsoftware.minereaper.signal.buffer.WritingByteBuffer;
import com.zyonicsoftware.minereaper.signal.client.Client;
import com.zyonicsoftware.minereaper.signal.exception.SignalException;
import com.zyonicsoftware.minereaper.signal.packet.Packet;
import com.zyonicsoftware.minereaper.signal.packet.PacketRegistry;
import com.zyonicsoftware.minereaper.signal.packet.ahead.UpdateUUIDPacket;
import com.zyonicsoftware.minereaper.signal.signal.SignalProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class OutputStreamThread {

    private final Client client;
    private final Socket socket;
    private final List<Packet> packets = new ArrayList<>();
    private final Timer timer = new Timer();
    private OutputStream finalOutputStream;

    public OutputStreamThread(final Client client) {
        this.client = client;
        this.socket = this.client.getSocket();
    }

    public void run() throws IOException {
        //initialise outputStream
        this.finalOutputStream = this.socket.getOutputStream();
        //start sending send byte arrays
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (OutputStreamThread.this.socket.isClosed()) {
                        //interrupt thread
                        OutputStreamThread.this.interrupt();
                        return;
                    }
                    //skip when no packets available to send
                    if (!OutputStreamThread.this.packets.isEmpty()) {
                        //get next packet available to send
                        final Packet packet = OutputStreamThread.this.packets.get(0);
                        //check if packet is valid
                        if (packet != null) {
                            //remove packet
                            OutputStreamThread.this.packets.remove(0);
                            final WritingByteBuffer writingByteBuffer = new WritingByteBuffer();
                            //check if packet is UpdateUUIDPacket
                            if (packet.getClass().equals(UpdateUUIDPacket.class)) {
                                writingByteBuffer.writeInt(-2);
                                writingByteBuffer.writeUUID(packet.getConnectionUUID());
                            } else {
                                //get packetId
                                final int packetId = PacketRegistry.indexOf(packet.getClass());
                                //write packetId
                                writingByteBuffer.writeInt(packetId);
                                //write connectionUuid
                                writingByteBuffer.writeUUID(OutputStreamThread.this.client.getConnectionUUID().get());
                                //initialise packet
                                packet.send(writingByteBuffer);
                            }
                            try {
                                //receive bytes
                                final byte[] bytes = writingByteBuffer.toBytes();
                                if (bytes.length < 255) {
                                    //check if outputstream is null
                                    assert OutputStreamThread.this.finalOutputStream != null;
                                    //write bytes length
                                    OutputStreamThread.this.finalOutputStream.write(bytes.length);
                                    //write bytes
                                    OutputStreamThread.this.finalOutputStream.write(bytes);
                                    //flush outputStream
                                    OutputStreamThread.this.finalOutputStream.flush();
                                } else {
                                    System.out.println(SignalProvider.getSignalProvider().getOutgoingLengthToLarge());
                                }
                            } catch (final SocketException exception) {
                                throw new SignalException(SignalProvider.getSignalProvider().getOutgoingSocketException(), exception);
                            }
                        }
                    }
                } catch (final IOException | NullPointerException exception) {
                    throw new SignalException(SignalProvider.getSignalProvider().getOutputStreamThrowsAnException(), exception);
                }
            }
        }, 0, 1);
    }

    public void interrupt() {
        try {
            this.finalOutputStream.close();
            this.timer.cancel();
        } catch (final IOException exception) {
            throw new SignalException(exception);
        }
    }

    public void send(final Packet packet) {
        this.packets.add(packet);
    }
}
