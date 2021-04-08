package com.zyonicsoftware.minereaper.signal.incoming;

import com.zyonicsoftware.minereaper.signal.buffer.ReadingByteBuffer;
import com.zyonicsoftware.minereaper.signal.client.Client;
import com.zyonicsoftware.minereaper.signal.packet.Packet;
import com.zyonicsoftware.minereaper.signal.packet.PacketRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class InputStreamThread {

    private final Client client;
    private final Socket socket;
    private final Timer timer = new Timer();
    private InputStream finalInputStream;
    final AtomicReference<byte[]> bytes = new AtomicReference<>(null);

    public InputStreamThread(final Client client) {
        this.client = client;
        this.socket = this.client.getSocket();
    }

    public void run() {
        //initialise inputStream
        try {

            this.finalInputStream = this.socket.getInputStream();
            this.timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (InputStreamThread.this.socket.isClosed()) {
                            //interrupt thread
                            InputStreamThread.this.interrupt();
                            return;
                        }
                        //check if finalInputStream is null
                        assert InputStreamThread.this.finalInputStream != null;
                        if (InputStreamThread.this.finalInputStream.available() > 0) {
                            final int b = InputStreamThread.this.finalInputStream.read();
                            if (b != -1) {
                                System.out.println(b);
                                InputStreamThread.this.bytes.set(new byte[b]);
                                System.out.println(Arrays.toString(InputStreamThread.this.bytes.get()));
                                //receive bytes
                                InputStreamThread.this.finalInputStream.read(InputStreamThread.this.bytes.get(), 0, b);
                                final ReadingByteBuffer readingByteBuffer = new ReadingByteBuffer(InputStreamThread.this.bytes.get());
                                //read packetId
                                final int packetId = readingByteBuffer.readInt();

                                //check if packet is UpdateUUIDPacket
                                if (packetId == -2) {
                                    //read connectionUUID
                                    final UUID connectionUUID = readingByteBuffer.readUUID();
                                    //set updated connectionUUID
                                    InputStreamThread.this.client.getConnectionUUID().set(connectionUUID);
                                } else {
                                    //get packet
                                    final Class<? extends Packet> packet = PacketRegistry.get(packetId);
                                    //read connectionUUID
                                    final UUID connectionUUID = readingByteBuffer.readUUID();
                                    //initialise packet
                                    packet.getDeclaredConstructor(UUID.class).newInstance(connectionUUID).receive(readingByteBuffer);
                                }
                            } else {
                                //close socket
                                InputStreamThread.this.socket.close();
                            }
                        }
                    } catch (final InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }, 0, 1);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        //start reading byte arrays
    }

    public void interrupt() {
        try {
            this.finalInputStream.close();
            this.timer.cancel();
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }
}
