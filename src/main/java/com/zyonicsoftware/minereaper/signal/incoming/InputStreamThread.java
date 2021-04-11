package com.zyonicsoftware.minereaper.signal.incoming;

import com.zyonicsoftware.minereaper.signal.buffer.ReadingByteBuffer;
import com.zyonicsoftware.minereaper.signal.client.Client;
import com.zyonicsoftware.minereaper.signal.exception.SignalException;
import com.zyonicsoftware.minereaper.signal.message.RegisteredMessenger;
import com.zyonicsoftware.minereaper.signal.message.SignalMessages;
import com.zyonicsoftware.minereaper.signal.packet.Packet;
import com.zyonicsoftware.minereaper.signal.packet.PacketRegistry;
import com.zyonicsoftware.minereaper.signal.signal.SignalProvider;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
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
    private final Class<? extends SignalMessages> signalMessages = RegisteredMessenger.get();

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
                                //check if byte array length smaller then 255 bytes
                                if (b < 255) {
                                    InputStreamThread.this.bytes.set(new byte[b]);
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

                                        // check if received packet not null
                                        if (packet != null) {
                                            //read connectionUUID
                                            final UUID connectionUUID = readingByteBuffer.readUUID();
                                            //initialise packet
                                            packet.getDeclaredConstructor(UUID.class).newInstance(connectionUUID).receive(readingByteBuffer);
                                            SignalProvider.getSignalProvider().setIncomingPackets(SignalProvider.getSignalProvider().getIncomingPackets() + 1);
                                            InputStreamThread.this.signalMessages.getDeclaredConstructor(String.class).newInstance(this.toString()).receivePacketMessage(SignalProvider.getSignalProvider().getIncomingPacketMessage());
                                        } else {
                                            InputStreamThread.this.signalMessages.getDeclaredConstructor(String.class).newInstance(this.toString()).receivePacketIsNullMessage(SignalProvider.getSignalProvider().getIncomingPacketIsNull());
                                        }
                                    }
                                } else {
                                    InputStreamThread.this.signalMessages.getDeclaredConstructor(String.class).newInstance(this.toString()).receiveLengthToLargeMessage(SignalProvider.getSignalProvider().getIncomingLengthToLarge());
                                }
                            } else {
                                //close socket
                                InputStreamThread.this.signalMessages.getDeclaredConstructor(String.class).newInstance(this.toString()).receiveSocketCloseMessage(SignalProvider.getSignalProvider().getIncomingSocketCloseMessage());
                                InputStreamThread.this.socket.close();
                            }
                        }
                    } catch (final InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | IOException exception) {
                        throw new SignalException(SignalProvider.getSignalProvider().getIncomingInputThrowsAnException(), exception);
                    }
                }
            }, 0, 1);
        } catch (final IOException exception) {
            throw new SignalException(SignalProvider.getSignalProvider().getInputStreamThrowsAnException(), exception);
        }
        //start reading byte arrays
    }

    public void interrupt() {
        try {
            this.finalInputStream.close();
            this.timer.cancel();
        } catch (final IOException exception) {
            throw new SignalException(exception);
        }
    }
}
