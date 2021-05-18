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
            if (this.finalInputStream == null) {
                this.finalInputStream = this.socket.getInputStream();
            }

            this.client.getScheduler().schedule(this.jobName, () -> {
                try {
                    if (InputStreamThread.this.socket.isClosed()) {
                        //interrupt thread
                        InputStreamThread.this.interrupt();
                        return;
                    }
                    //check if finalInputStream is null
                    if (InputStreamThread.this.finalInputStream != null && InputStreamThread.this.finalInputStream.available() > 0) {
                        final int b = InputStreamThread.this.finalInputStream.read();
                        if (b != -1) {
                            //check if byte array length smaller then 255 bytes
                            if (b < 255) {
                                //this.bytes.set(new byte[b]);
                                //receive bytes
                                //this.finalInputStream.read(this.bytes.get(), 0, b);
                                final ReadingByteBuffer readingByteBuffer = new ReadingByteBuffer(InputStreamThread.this.finalInputStream.readNBytes(b));
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
                                        //SignalProvider.getSignalProvider().setIncomingPackets(SignalProvider.getSignalProvider().getIncomingPackets() + 1);
                                        InputStreamThread.this.signalCaller.getDeclaredConstructor(String.class).newInstance(this.toString()).receivePacketMessage(SignalProvider.getSignalProvider().getIncomingPacketMessage());
                                    } else {
                                        InputStreamThread.this.signalCaller.getDeclaredConstructor(String.class).newInstance(this.toString()).receivePacketIsNullMessage(SignalProvider.getSignalProvider().getIncomingPacketIsNull());
                                    }
                                }
                            } else {
                                InputStreamThread.this.signalCaller.getDeclaredConstructor(String.class).newInstance(this.toString()).receiveLengthToLargeMessage(SignalProvider.getSignalProvider().getIncomingLengthToLarge());
                            }
                        } else {
                            //close socket
                            InputStreamThread.this.signalCaller.getDeclaredConstructor(String.class).newInstance(this.toString()).receiveSocketCloseMessage(SignalProvider.getSignalProvider().getIncomingSocketCloseMessage());
                            InputStreamThread.this.socket.close();
                        }
                    }
                } catch (final InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | IOException exception) {
                    throw new SignalException(SignalProvider.getSignalProvider().getIncomingInputThrowsAnException(), exception);
                }
            }, Schedules.fixedDelaySchedule(Duration.ofMillis(this.client.getScheduleDelay())));
        } catch (final IOException exception) {
            throw new SignalException(SignalProvider.getSignalProvider().getInputStreamThrowsAnException(), exception);
        }
        //start reading byte arrays
    }

    public void interrupt() {
        try {
            this.finalInputStream.close();
            Thread.currentThread().interrupt();
            this.client.getScheduler().cancel(this.jobName).thenAccept(job -> {
                try {
                    this.signalCaller.getDeclaredConstructor(String.class).newInstance(this.toString()).canceledJob(SignalProvider.getSignalProvider().getCanceledJobMessage().replaceAll("%job%", job.name()));
                } catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
                    throw new SignalException(SignalProvider.getSignalProvider().getCanceledJobThrowsAnException(), exception);
                }
            });
        } catch (final IOException exception) {
            throw new SignalException(SignalProvider.getSignalProvider().getIncomingInputThrowsAnException(), exception);
        }
    }
}
