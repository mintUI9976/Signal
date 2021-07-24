/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.signal;

/** @author Niklas Griese */
public class SignalProvider {

  private static final SignalProvider signalProvider = new SignalProvider();

  private final String prefix;
  private final String inputStreamThrowsAnException;
  private final String incomingLengthToLarge;
  private final String incomingPacketIsNull;
  private final String incomingInputThrowsAnException;
  private final String canceledJobThrowsAnException;
  private final String outgoingLengthToLarge;
  private final String outgoingSocketException;
  private final String outputStreamThrowsAnException;
  private final String outgoingPacketMessage;
  private final String incomingPacketMessage;
  private final String incomingSocketCloseMessage;
  private final String acceptSocketConnectionMessage;
  private final String unAcceptSocketConnectionMessage;
  private final String disconnectClient;
  private final String connectClient;
  private final String timeoutClient;
  private final String disconnectAllClients;
  private final String canceledJobMessage;
  private final String timeoutThrowsAnException;

  {
    this.prefix = "Signal | ";
    this.incomingLengthToLarge = this.prefix + "The received byte array length is larger than 255.";
    this.incomingPacketIsNull =
        this.prefix + "The received packet is null, check your PacketRegistry.";
    this.incomingInputThrowsAnException =
        this.prefix + "An accepted byte array throws an exception.";
    this.canceledJobThrowsAnException =
        this.prefix + "An canceled Job throws an exception, please restart your service.";
    this.inputStreamThrowsAnException =
        this.prefix + "The input stream cannot be opened or is null.";
    this.outgoingLengthToLarge = this.prefix + "The sending byte array length is larger than 255.";
    this.outgoingSocketException = this.prefix + "The sending bytes can not be written or flushed.";
    this.outputStreamThrowsAnException = this.prefix + "The output stream is closed or is null.";
    this.outgoingPacketMessage =
        this.prefix + "The packet: %packet% was sent successfully to client: %client%.";
    this.incomingPacketMessage =
        this.prefix + "The packet: %packet% was received successfully from client: %client%.";
    this.incomingSocketCloseMessage =
        this.prefix + "The client connection above %ip% will be closed.";
    this.acceptSocketConnectionMessage =
        this.prefix + "The client connection above %ip% has been accepted.";
    this.unAcceptSocketConnectionMessage =
        this.prefix + "The client connection above %ip% has been canceled.";
    this.disconnectClient = this.prefix + "Client: %client% has been disconnected.";
    this.connectClient = this.prefix + "Client: %client% has been connected.";
    this.disconnectAllClients = this.prefix + "All Clients will be disconnected.";
    this.canceledJobMessage = this.prefix + "Job: %job% has been canceled.";
    this.timeoutClient = this.prefix + "Client: %client% has timed out.";
    this.timeoutThrowsAnException =
        this.prefix
            + "The timeout value must be greater than 10000ms to use the technology in combination with the KeepAlive scheduler.";
  }

  public String getTimeoutThrowsAnException() {
    return this.timeoutThrowsAnException;
  }

  public String getConnectClient() {
    return this.connectClient;
  }

  public String getCanceledJobThrowsAnException() {
    return this.canceledJobThrowsAnException;
  }

  public String getCanceledJobMessage() {
    return this.canceledJobMessage;
  }

  public String getUnAcceptSocketConnectionMessage() {
    return this.unAcceptSocketConnectionMessage;
  }

  public String getDisconnectAllClients() {
    return this.disconnectAllClients;
  }

  public String getDisconnectClient() {
    return this.disconnectClient;
  }

  public String getAcceptSocketConnectionMessage() {
    return this.acceptSocketConnectionMessage;
  }

  public String getIncomingSocketCloseMessage() {
    return this.incomingSocketCloseMessage;
  }

  public String getIncomingPacketMessage() {
    return this.incomingPacketMessage;
  }

  public String getOutgoingPacketMessage() {
    return this.outgoingPacketMessage;
  }

  public String getOutputStreamThrowsAnException() {
    return this.outputStreamThrowsAnException;
  }

  public String getOutgoingSocketException() {
    return this.outgoingSocketException;
  }

  public String getOutgoingLengthToLarge() {
    return this.outgoingLengthToLarge;
  }

  public String getInputStreamThrowsAnException() {
    return this.inputStreamThrowsAnException;
  }

  public String getPrefix() {
    return this.prefix;
  }

  public String getIncomingLengthToLarge() {
    return this.incomingLengthToLarge;
  }

  public String getIncomingPacketIsNull() {
    return this.incomingPacketIsNull;
  }

  public String getIncomingInputThrowsAnException() {
    return this.incomingInputThrowsAnException;
  }

  public String getTimeoutClient() {
    return this.timeoutClient;
  }

  public static SignalProvider getSignalProvider() {
    return SignalProvider.signalProvider;
  }
}
