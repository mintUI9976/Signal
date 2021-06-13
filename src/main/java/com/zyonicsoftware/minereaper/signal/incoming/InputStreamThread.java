/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.incoming;

import com.zyonicsoftware.minereaper.runnable.RedEugeneSchedulerRunnable;
import com.zyonicsoftware.minereaper.signal.buffer.ReadingByteBuffer;
import com.zyonicsoftware.minereaper.signal.caller.SignalCallRegistry;
import com.zyonicsoftware.minereaper.signal.client.Client;
import com.zyonicsoftware.minereaper.signal.exception.SignalException;
import com.zyonicsoftware.minereaper.signal.packet.Packet;
import com.zyonicsoftware.minereaper.signal.packet.PacketRegistry;
import com.zyonicsoftware.minereaper.signal.scheduler.RedEugeneScheduler;
import com.zyonicsoftware.minereaper.signal.signal.SignalProvider;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class InputStreamThread extends RedEugeneSchedulerRunnable {

    private final Client client;
    private final Socket socket;
    private final InputStream finalInputStream;

    public InputStreamThread(@NotNull final String eugeneJobName, @NotNull final TimeUnit timeUnit, final long period, final Client client) {
        super(eugeneJobName, timeUnit, period);
        this.client = client;
        this.socket = this.client.getSocket();
        try {
            this.finalInputStream = this.socket.getInputStream();
        } catch (final IOException exception) {
            throw new SignalException(SignalProvider.getSignalProvider().getOutputStreamThrowsAnException(), exception);
        }
    }

    @Override
    public void run() {
        super.run();
        //initialise inputStream
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
                                SignalCallRegistry.getReferenceCaller().getDeclaredConstructor(String.class).newInstance(this.toString()).receivePacketMessage(SignalProvider.getSignalProvider().getIncomingPacketMessage());
                            } else {
                                SignalCallRegistry.getReferenceCaller().getDeclaredConstructor(String.class).newInstance(this.toString()).receivePacketIsNullMessage(SignalProvider.getSignalProvider().getIncomingPacketIsNull());
                            }
                        }
                    } else {
                        SignalCallRegistry.getReferenceCaller().getDeclaredConstructor(String.class).newInstance(this.toString()).receiveLengthToLargeMessage(SignalProvider.getSignalProvider().getIncomingLengthToLarge());
                    }
                } else {
                    //close socket
                    SignalCallRegistry.getReferenceCaller().getDeclaredConstructor(String.class).newInstance(this.toString()).receiveSocketCloseMessage(SignalProvider.getSignalProvider().getIncomingSocketCloseMessage());
                    InputStreamThread.this.socket.close();
                }
            }
        } catch (final InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | IOException exception) {
            throw new SignalException(SignalProvider.getSignalProvider().getIncomingInputThrowsAnException(), exception);
        }
        //start reading byte arrays
    }

    public void interrupt() {
        try {
            this.finalInputStream.close();
            final String jobName = this.getEugeneJob().getName();
            if (RedEugeneScheduler.getRedEugeneIntroduction().forceCancelEugeneJob(jobName)) {
                try {
                    SignalCallRegistry.getReferenceCaller().getDeclaredConstructor(String.class).newInstance(this.toString()).canceledJob(SignalProvider.getSignalProvider().getCanceledJobMessage().replaceAll("%job%", jobName));
                } catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
                    throw new SignalException(SignalProvider.getSignalProvider().getCanceledJobThrowsAnException(), exception);
                }
            }
        } catch (final IOException exception) {
            throw new SignalException(SignalProvider.getSignalProvider().getIncomingInputThrowsAnException(), exception);
        }
    }
}
