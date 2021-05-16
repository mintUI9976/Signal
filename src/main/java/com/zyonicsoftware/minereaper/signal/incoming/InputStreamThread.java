package com.zyonicsoftware.minereaper.signal.incoming;

import com.coreoz.wisp.schedule.Schedules;
import com.zyonicsoftware.minereaper.signal.buffer.ReadingByteBuffer;
import com.zyonicsoftware.minereaper.signal.caller.SignalCallRegistry;
import com.zyonicsoftware.minereaper.signal.caller.SignalCaller;
import com.zyonicsoftware.minereaper.signal.client.Client;
import com.zyonicsoftware.minereaper.signal.exception.SignalException;
import com.zyonicsoftware.minereaper.signal.packet.Packet;
import com.zyonicsoftware.minereaper.signal.packet.PacketRegistry;
import com.zyonicsoftware.minereaper.signal.signal.SignalProvider;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.time.Duration;
import java.util.UUID;

public class InputStreamThread {

    private final Client client;
    private final Socket socket;
    private InputStream finalInputStream;
    private final Class<? extends SignalCaller> signalCaller;
    private final String jobName = "incoming_packets_" + UUID.randomUUID().toString();

    public InputStreamThread(final Client client) {
        this.client = client;
        this.socket = this.client.getSocket();
        this.signalCaller = SignalCallRegistry.getReferenceCaller();
    }

    public void run() {
        //initialise inputStream
        try {
            this.finalInputStream = this.socket.getInputStream();
            this.client.getScheduler().schedule(this.jobName, () -> {
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
                } catch (final InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | IOException ignored) {

                }
            }, Schedules.fixedDelaySchedule(Duration.ofMillis(1)));
        } catch (final IOException exception) {
            throw new SignalException(SignalProvider.getSignalProvider().getInputStreamThrowsAnException(), exception);
        }
        //start reading byte arrays
    }

    public void interrupt() {
        try {
            this.finalInputStream.close();
            this.client.getScheduler().findJob(this.jobName).ifPresent(job -> job.threadRunningJob().interrupt());
            this.client.getScheduler().cancel(this.jobName).thenAccept(job -> {
                try {
                    this.signalCaller.getDeclaredConstructor(String.class).newInstance(this.toString()).canceledJob(SignalProvider.getSignalProvider().getCanceledJobMessage().replaceAll("%job%", job.name()));
                } catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
                    throw new SignalException(SignalProvider.getSignalProvider().getCanceledJobThrowsAnException(), exception);
                }
            });
        } catch (final IOException exception) {
            throw new SignalException(exception);
        }
    }
}
