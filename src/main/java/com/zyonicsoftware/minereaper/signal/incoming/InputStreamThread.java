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
import com.zyonicsoftware.minereaper.signal.cache.Cache;
import com.zyonicsoftware.minereaper.signal.caller.SignalCallRegistry;
import com.zyonicsoftware.minereaper.signal.client.Client;
import com.zyonicsoftware.minereaper.signal.exception.SignalException;
import com.zyonicsoftware.minereaper.signal.packet.Packet;
import com.zyonicsoftware.minereaper.signal.packet.PacketRegistry;
import com.zyonicsoftware.minereaper.signal.packet.ahead.KeepAlivePacket;
import com.zyonicsoftware.minereaper.signal.packet.ahead.UpdateUUIDPacket;
import com.zyonicsoftware.minereaper.signal.scheduler.RedEugeneScheduler;
import com.zyonicsoftware.minereaper.signal.signal.SignalProvider;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Niklas Griese
 * @see com.zyonicsoftware.minereaper.runnable.RedEugeneSchedulerRunnable
 * @see java.lang.Runnable
 * @see Client
 * @see Socket
 * @see InputStream
 * @see ReadingByteBuffer
 * @see Packet
 * @see SignalProvider
 */
public class InputStreamThread extends RedEugeneSchedulerRunnable {

  private final Client client;
  private final Socket socket;
  private final InputStream finalInputStream;
  private long cachedTime = 0;

  public InputStreamThread(
      @NotNull final String eugeneJobName,
      @NotNull final TimeUnit timeUnit,
      final long period,
      final Client client) {
    super(eugeneJobName, timeUnit, period);
    this.client = client;
    this.socket = this.client.getSocket();
    try {
      this.finalInputStream = this.socket.getInputStream();
    } catch (final IOException exception) {
      throw new SignalException(
          SignalProvider.getSignalProvider().getOutputStreamThrowsAnException(), exception);
    }
  }

  @Override
  public void run() {
    super.run();
    // initialise inputStream
    try {
      if (this.socket.isClosed()) {
        // interrupt thread
        this.interrupt();
        return;
      }
      // check if finalInputStream is null
      if (this.finalInputStream != null) {
        if (this.finalInputStream.available() > 0) {
          final int b = this.finalInputStream.read();
          if (b != -1) {
            // check if byte array length smaller then 255 bytes
            if (b < 255) {
              // this.bytes.set(new byte[b]);
              // receive bytes
              // this.finalInputStream.read(this.bytes.get(), 0, b);
              final ReadingByteBuffer readingByteBuffer =
                  new ReadingByteBuffer(this.finalInputStream.readNBytes(b));
              // read packetId
              final int packetId = readingByteBuffer.readInt();
              // check if packet is UpdateUUIDPacket
              if (packetId == -2) {
                // read connectionUUID
                final UUID connectionUUID = readingByteBuffer.readUUID();
                // set updated connectionUUID
                this.client.getConnectionUUID().set(connectionUUID);
                this.resetCalculation();
                this.receiveIncomingPacketMessage(
                    UpdateUUIDPacket.class.getName(), connectionUUID.toString());
              } else if (packetId == -3) {
                // read connectionUUID
                final UUID connectionUUID = readingByteBuffer.readUUID();
                // set updated connectionUUID
                this.resetCalculation();
                this.receiveIncomingPacketMessage(
                    KeepAlivePacket.class.getName(), connectionUUID.toString());
              } else if (packetId == -4) {
                final UUID connectionUUID = readingByteBuffer.readUUID();
                this.resetCalculation();
                this.receiveIncomingPacketMessage(
                    KeepAlivePacket.class.getName(), connectionUUID.toString());
                this.client.disconnect();
              } else {
                // get packet
                final Class<? extends Packet> packet = PacketRegistry.get(packetId);

                // check if received packet not null
                if (packet != null) {
                  // read connectionUUID
                  final UUID connectionUUID = readingByteBuffer.readUUID();
                  // initialise packet
                  packet
                      .getDeclaredConstructor(UUID.class)
                      .newInstance(connectionUUID)
                      .receive(readingByteBuffer);
                  Cache.setIncomingPackets(Cache.getIncomingPackets() + 1);
                  this.client.setIncomingPackets(this.client.getIncomingPackets() + 1);
                  // set cached time to 0;
                  this.resetCalculation();
                  // SignalProvider.getSignalProvider().setIncomingPackets(SignalProvider.getSignalProvider().getIncomingPackets() + 1);
                  this.receiveIncomingPacketMessage(packet.getName(), connectionUUID.toString());
                } else {
                  SignalCallRegistry.getReferenceCaller()
                      .getDeclaredConstructor(String.class)
                      .newInstance(this.toString())
                      .receivePacketIsNullMessage(
                          SignalProvider.getSignalProvider().getIncomingPacketIsNull());
                }
              }
            } else {
              SignalCallRegistry.getReferenceCaller()
                  .getDeclaredConstructor(String.class)
                  .newInstance(this.toString())
                  .receiveLengthToLargeMessage(
                      SignalProvider.getSignalProvider().getIncomingLengthToLarge());
            }
          } else {
            // close socket
            SignalCallRegistry.getReferenceCaller()
                .getDeclaredConstructor(String.class)
                .newInstance(this.toString())
                .receiveSocketCloseMessage(
                    SignalProvider.getSignalProvider().getIncomingSocketCloseMessage());
            this.socket.close();
          }
        } else {
          this.calculateClientTimeoutTime();
        }
      }

    } catch (final InstantiationException
        | IOException
        | IllegalAccessException
        | NoSuchMethodException
        | InvocationTargetException exception) {
      throw new SignalException(
          SignalProvider.getSignalProvider().getIncomingInputThrowsAnException(), exception);
    }
  }

  /**
   * @param packetName intern call
   * @param connectionUUID intern call
   * @throws NoSuchMethodException when instance can not be called
   * @throws InvocationTargetException when instance can not be called
   * @throws InstantiationException when instance can not be called
   * @throws IllegalAccessException when instance can not be called
   */
  private void receiveIncomingPacketMessage(final String packetName, final String connectionUUID)
      throws NoSuchMethodException, InvocationTargetException, InstantiationException,
          IllegalAccessException {
    SignalCallRegistry.getReferenceCaller()
        .getDeclaredConstructor(String.class)
        .newInstance(this.toString())
        .receivePacketMessage(
            SignalProvider.getSignalProvider()
                .getIncomingPacketMessage()
                .replace("%packet%", packetName)
                .replace("%client%", connectionUUID));
  }

  /** rested the calculation at the client time outed */
  private void resetCalculation() {
    if (this.cachedTime > 0) {
      this.cachedTime = 0;
    }
  }

  /** calculate the client timeout */
  private void calculateClientTimeoutTime() {
    if (this.cachedTime == 0) {
      this.cachedTime = System.currentTimeMillis();
    } else {
      final long estimatedTime = System.currentTimeMillis() - this.cachedTime;
      if (estimatedTime == this.client.getTimeout()) {
        try {
          SignalCallRegistry.getReferenceCaller()
              .getDeclaredConstructor(String.class)
              .newInstance(this.toString())
              .clientTimeoutMessage(
                  SignalProvider.getSignalProvider()
                      .getTimeoutClient()
                      .replace("%client%", this.client.getConnectionUUID().get().toString()));
          SignalCallRegistry.getReferenceCaller()
              .getDeclaredConstructor(String.class)
              .newInstance(this.toString())
              .clientTimeout(this.client);
        } catch (final InstantiationException
            | NoSuchMethodException
            | InvocationTargetException
            | IllegalAccessException exception) {
          throw new SignalException(
              SignalProvider.getSignalProvider().getIncomingInputThrowsAnException(), exception);
        }
      }
    }
  }

  /** destroy the runner at the client disconnect */
  public void interrupt() {
    try {
      this.finalInputStream.close();
      final String jobName = this.getEugeneJob().getName();
      if (RedEugeneScheduler.getRedEugeneIntroduction().forceCancelEugeneJob(jobName)) {
        try {
          SignalCallRegistry.getReferenceCaller()
              .getDeclaredConstructor(String.class)
              .newInstance(this.toString())
              .canceledJobMessage(
                  SignalProvider.getSignalProvider()
                      .getCanceledJobMessage()
                      .replaceAll("%job%", jobName));
        } catch (final InstantiationException
            | IllegalAccessException
            | InvocationTargetException
            | NoSuchMethodException exception) {
          throw new SignalException(
              SignalProvider.getSignalProvider().getCanceledJobThrowsAnException(), exception);
        }
      }
    } catch (final IOException exception) {
      throw new SignalException(
          SignalProvider.getSignalProvider().getIncomingInputThrowsAnException(), exception);
    }
  }
}
