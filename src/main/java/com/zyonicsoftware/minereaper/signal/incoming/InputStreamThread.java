package com.zyonicsoftware.minereaper.signal.incoming;

import com.zyonicsoftware.minereaper.signal.buffer.ReadingByteBuffer;
import com.zyonicsoftware.minereaper.signal.caller.SignalCallRegistry;
import com.zyonicsoftware.minereaper.signal.caller.SignalCaller;
import com.zyonicsoftware.minereaper.signal.client.Client;
import com.zyonicsoftware.minereaper.signal.exception.SignalException;
import com.zyonicsoftware.minereaper.signal.executor.Factory;
import com.zyonicsoftware.minereaper.signal.packet.Packet;
import com.zyonicsoftware.minereaper.signal.packet.PacketRegistry;
import com.zyonicsoftware.minereaper.signal.signal.SignalProvider;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class InputStreamThread {

    private final Client client;
    private final Socket socket;
    private InputStream finalInputStream;
    final AtomicReference<byte[]> bytes = new AtomicReference<>(null);
    private final Class<? extends SignalCaller> signalCaller = SignalCallRegistry.get();

    public InputStreamThread(final Client client) {
        this.client = client;
        this.socket = this.client.getSocket();
    }

    public void run() {
        //initialise inputStream
        try {
            this.finalInputStream = this.socket.getInputStream();
            Factory.getScheduledExecutorService().scheduleAtFixedRate(() -> {
                try {
                    if (this.socket.isClosed()) {
                        //interrupt thread
                        this.interrupt();
                        return;
                    }
                    //check if finalInputStream is null
                    assert this.finalInputStream != null;
                    if (this.finalInputStream.available() > 0) {
                        final int b = this.finalInputStream.read();
                        if (b != -1) {
                            //check if byte array length smaller then 255 bytes
                            if (b < 255) {
                                //this.bytes.set(new byte[b]);
                                //receive bytes
                                //this.finalInputStream.read(this.bytes.get(), 0, b);
                                final ReadingByteBuffer readingByteBuffer = new ReadingByteBuffer(this.finalInputStream.readNBytes(b));
                                //read packetId
                                final int packetId = readingByteBuffer.readInt();

                                //check if packet is UpdateUUIDPacket
                                if (packetId == -2) {
                                    //read connectionUUID
                                    final UUID connectionUUID = readingByteBuffer.readUUID();
                                    //set updated connectionUUID
                                    this.client.getConnectionUUID().set(connectionUUID);
                                } else {
                                    //get packet
                                    final Class<? extends Packet> packet = PacketRegistry.get(packetId);

                                    // check if received packet not null
                                    if (packet != null) {
                                        //read connectionUUID
                                        final UUID connectionUUID = readingByteBuffer.readUUID();
                                        //initialise packet
                                        packet.getDeclaredConstructor(UUID.class).newInstance(connectionUUID).receive(readingByteBuffer);
                                        //SignalProvider.getSignalProvider().setIncomingPackets(SignalProvider.getSignalProvider().getIncomingPackets() + 1);
                                        this.signalCaller.getDeclaredConstructor(String.class).newInstance(this.toString()).receivePacketMessage(SignalProvider.getSignalProvider().getIncomingPacketMessage());
                                    } else {
                                        this.signalCaller.getDeclaredConstructor(String.class).newInstance(this.toString()).receivePacketIsNullMessage(SignalProvider.getSignalProvider().getIncomingPacketIsNull());
                                    }
                                }
                            } else {
                                this.signalCaller.getDeclaredConstructor(String.class).newInstance(this.toString()).receiveLengthToLargeMessage(SignalProvider.getSignalProvider().getIncomingLengthToLarge());
                            }
                        } else {
                            //close socket
                            this.signalCaller.getDeclaredConstructor(String.class).newInstance(this.toString()).receiveSocketCloseMessage(SignalProvider.getSignalProvider().getIncomingSocketCloseMessage());
                            this.socket.close();
                        }
                    }
                } catch (final InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | IOException exception) {
                    throw new SignalException(SignalProvider.getSignalProvider().getIncomingInputThrowsAnException(), exception);
                }
            }, 0, 1, TimeUnit.MILLISECONDS);
        } catch (final IOException exception) {
            throw new SignalException(SignalProvider.getSignalProvider().getInputStreamThrowsAnException(), exception);
        }
        //start reading byte arrays
    }

    public void interrupt() {
        try {
            this.finalInputStream.close();
        } catch (final IOException exception) {
            throw new SignalException(exception);
        }
    }
}
