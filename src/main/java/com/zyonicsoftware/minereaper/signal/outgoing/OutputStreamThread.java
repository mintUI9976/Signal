/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.outgoing;

import com.zyonicsoftware.minereaper.runnable.RedEugeneSchedulerRunnable;
import com.zyonicsoftware.minereaper.signal.buffer.WritingByteBuffer;
import com.zyonicsoftware.minereaper.signal.caller.SignalCallRegistry;
import com.zyonicsoftware.minereaper.signal.client.Client;
import com.zyonicsoftware.minereaper.signal.exception.SignalException;
import com.zyonicsoftware.minereaper.signal.packet.Packet;
import com.zyonicsoftware.minereaper.signal.packet.PacketRegistry;
import com.zyonicsoftware.minereaper.signal.packet.ahead.UpdateUUIDPacket;
import com.zyonicsoftware.minereaper.signal.scheduler.RedEugeneScheduler;
import com.zyonicsoftware.minereaper.signal.signal.SignalProvider;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OutputStreamThread extends RedEugeneSchedulerRunnable {

  private final Client client;
  private final Socket socket;
  private final List<Packet> packets = new ArrayList<>();
  private final OutputStream finalOutputStream;

  public OutputStreamThread(
      @NotNull final String eugeneJobName,
      @NotNull final TimeUnit timeUnit,
      final long period,
      final Client client) {
    super(eugeneJobName, timeUnit, period);
    this.client = client;
    this.socket = this.client.getSocket();
    try {
      this.finalOutputStream = this.socket.getOutputStream();
    } catch (final IOException exception) {
      throw new SignalException(
          SignalProvider.getSignalProvider().getOutputStreamThrowsAnException(), exception);
    }
  }

  @Override
  public void run() {
    super.run();
    // initialise outputStream
    try {
      if (this.socket.isClosed()) {
        // interrupt thread
        this.interrupt();
        return;
      }
      // skip when no packets available to send
      if (this.finalOutputStream != null && !this.packets.isEmpty()) {
        // get next packet available to send
        final Packet packet = this.packets.get(0);
        // check if packet is valid
        if (packet != null) {
          // remove packet
          this.packets.remove(0);
          final WritingByteBuffer writingByteBuffer = new WritingByteBuffer();
          // check if packet is UpdateUUIDPacket
          if (packet.getClass().equals(UpdateUUIDPacket.class)) {
            writingByteBuffer.writeInt(-2);
            writingByteBuffer.writeUUID(packet.getConnectionUUID());
          } else {
            // get packetId
            final int packetId = PacketRegistry.indexOf(packet.getClass());
            // write packetId
            writingByteBuffer.writeInt(packetId);
            // write connectionUuid
            writingByteBuffer.writeUUID(this.client.getConnectionUUID().get());
            // initialise packet
            packet.send(writingByteBuffer);
          }
          // receive bytes
          final byte[] bytes = writingByteBuffer.toBytes();
          if (bytes.length < 255) {
            // check if outputstream is null
            // write bytes length
            this.finalOutputStream.write(bytes.length);
            // write bytes
            this.finalOutputStream.write(bytes);
            // flush outputStream
            this.finalOutputStream.flush();
            // SignalProvider.getSignalProvider().setOutgoingPackets(SignalProvider.getSignalProvider().getOutgoingPackets() + 1);
            SignalCallRegistry.getReferenceCaller()
                .getDeclaredConstructor(String.class)
                .newInstance(this.toString())
                .sendPacketMessage(SignalProvider.getSignalProvider().getOutgoingPacketMessage());
          } else {
            SignalCallRegistry.getReferenceCaller()
                .getDeclaredConstructor(String.class)
                .newInstance(this.toString())
                .sendLengthToLargeMessage(
                    SignalProvider.getSignalProvider().getOutgoingLengthToLarge());
          }
        }
      }
    } catch (final IOException
        | NullPointerException
        | InstantiationException
        | InvocationTargetException
        | NoSuchMethodException
        | IllegalAccessException exception) {
      throw new SignalException(
          SignalProvider.getSignalProvider().getOutputStreamThrowsAnException(), exception);
    }
  }

  public void interrupt() {
    try {
      this.finalOutputStream.close();
      final String jobName = this.getEugeneJob().getName();
      if (RedEugeneScheduler.getRedEugeneIntroduction().forceCancelEugeneJob(jobName)) {
        try {
          SignalCallRegistry.getReferenceCaller()
              .getDeclaredConstructor(String.class)
              .newInstance(this.toString())
              .canceledJob(
                  SignalProvider.getSignalProvider()
                      .getCanceledJobMessage()
                      .replaceAll("%job%", jobName));
        } catch (final InstantiationException
            | IllegalAccessException
            | InvocationTargetException
            | NoSuchMethodException exception) {
          throw new SignalException(
              SignalProvider.getSignalProvider().getCanceledJobThrowsAnException()
                  + " | "
                  + SignalProvider.getSignalProvider().getOutputStreamThrowsAnException(),
              exception);
        }
      }
    } catch (final IOException exception) {
      throw new SignalException(
          SignalProvider.getSignalProvider().getOutputStreamThrowsAnException(), exception);
    }
  }

  public void send(final Packet packet) {
    this.packets.add(packet);
  }
}
