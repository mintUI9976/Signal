package com.zyonicsoftware.minereaper.signal.outgoing;

import com.coreoz.wisp.schedule.Schedules;
import com.zyonicsoftware.minereaper.signal.buffer.WritingByteBuffer;
import com.zyonicsoftware.minereaper.signal.caller.SignalCallRegistry;
import com.zyonicsoftware.minereaper.signal.caller.SignalCaller;
import com.zyonicsoftware.minereaper.signal.client.Client;
import com.zyonicsoftware.minereaper.signal.exception.SignalException;
import com.zyonicsoftware.minereaper.signal.packet.Packet;
import com.zyonicsoftware.minereaper.signal.packet.PacketRegistry;
import com.zyonicsoftware.minereaper.signal.packet.ahead.UpdateUUIDPacket;
import com.zyonicsoftware.minereaper.signal.signal.SignalProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.SocketException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OutputStreamThread {

    private final Client client;
    private final Socket socket;
    private final List<Packet> packets = new ArrayList<>();
    private OutputStream finalOutputStream;
    private final Class<? extends SignalCaller> signalCaller;
    private final String jobName = "outgoing_packets_" + UUID.randomUUID().toString();

    public OutputStreamThread(final Client client) {
        this.client = client;
        this.socket = this.client.getSocket();
        this.signalCaller = SignalCallRegistry.getReferenceCaller();
    }

    public void run() throws IOException {
        //initialise outputStream
        this.finalOutputStream = this.socket.getOutputStream();
        //start sending send byte arrays
        this.client.getScheduler().schedule(this.jobName, () -> {
            try {
                if (this.socket.isClosed()) {
                    //interrupt thread
                    this.interrupt();
                    return;
                }
                //skip when no packets available to send
                if (!this.packets.isEmpty()) {
                    //get next packet available to send
                    final Packet packet = this.packets.get(0);
                    //check if packet is valid
                    if (packet != null) {
                        //remove packet
                        this.packets.remove(0);
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
                            writingByteBuffer.writeUUID(this.client.getConnectionUUID().get());
                            //initialise packet
                            packet.send(writingByteBuffer);
                        }
                        try {
                            //receive bytes
                            final byte[] bytes = writingByteBuffer.toBytes();
                            if (bytes.length < 255) {
                                //check if outputstream is null
                                assert this.finalOutputStream != null;
                                //write bytes length
                                this.finalOutputStream.write(bytes.length);
                                //write bytes
                                this.finalOutputStream.write(bytes);
                                //flush outputStream
                                this.finalOutputStream.flush();
                                //SignalProvider.getSignalProvider().setOutgoingPackets(SignalProvider.getSignalProvider().getOutgoingPackets() + 1);
                                this.signalCaller.getDeclaredConstructor(String.class).newInstance(this.toString()).sendPacketMessage(SignalProvider.getSignalProvider().getOutgoingPacketMessage());
                            } else {
                                this.signalCaller.getDeclaredConstructor(String.class).newInstance(this.toString()).sendLengthToLargeMessage(SignalProvider.getSignalProvider().getOutgoingLengthToLarge());
                            }
                        } catch (final SocketException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException ignored) {

                        }
                    }
                }
            } catch (final IOException | NullPointerException exception) {
                throw new SignalException(SignalProvider.getSignalProvider().getOutputStreamThrowsAnException(), exception);
            }
        }, Schedules.fixedDelaySchedule(Duration.ofMillis(1)));
    }

    public void interrupt() {
        try {
            this.finalOutputStream.close();
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

    public void send(final Packet packet) {
        this.packets.add(packet);
    }
}
