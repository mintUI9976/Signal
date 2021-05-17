package com.zyonicsoftware.minereaper.signal.signal;

import java.util.concurrent.TimeUnit;

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
    private final String disconnectAllClients;
    private final String canceledJobMessage;

    {
        this.prefix = "Signal > ";
        this.incomingLengthToLarge = this.prefix + "The received byte array length is larger than 255.";
        this.incomingPacketIsNull = this.prefix + "The received packet is null, check your PacketRegistry.";
        this.incomingInputThrowsAnException = this.prefix + "An accepted byte array throws an exception.";
        this.canceledJobThrowsAnException = this.prefix + "An canceled Job throws an exception, please restart your service.";
        this.inputStreamThrowsAnException = this.prefix + "The input stream cannot be opened or is null.";
        this.outgoingLengthToLarge = this.prefix + "The sending byte array length is larger than 255.";
        this.outgoingSocketException = this.prefix + "The sending bytes can not be written or flushed.";
        this.outputStreamThrowsAnException = this.prefix + "The output stream is closed or is null.";
        this.outgoingPacketMessage = this.prefix + "The packet was successfully sent.";
        this.incomingPacketMessage = this.prefix + "The packet was received successfully.";
        this.incomingSocketCloseMessage = this.prefix + "The client connection will be closed.";
        this.acceptSocketConnectionMessage = this.prefix + "The client connection has been accepted.";
        this.unAcceptSocketConnectionMessage = this.prefix + "The client connection has been canceled.";
        this.disconnectClient = this.prefix + "Client: %client% will be disconnected.";
        this.disconnectAllClients = this.prefix + "All Clients will be disconnected.";
        this.canceledJobMessage = this.prefix + "Job: %job% has been canceled.";
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

    public void sleepCurrentThread(final long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (final InterruptedException ignored) {

        }
    }

    public static SignalProvider getSignalProvider() {
        return SignalProvider.signalProvider;
    }

}
