package com.zyonicsoftware.minereaper.signal.signal;

public class SignalProvider {

    private static final SignalProvider signalProvider = new SignalProvider();

    private final String prefix;
    private final String inputStreamThrowsAnException;
    private final String incomingLengthToLarge;
    private final String incomingPacketIsNull;
    private final String incomingInputThrowsAnException;
    private final String outgoingLengthToLarge;
    private final String outgoingSocketException;
    private final String outputStreamThrowsAnException;
    private final String outgoingPacketMessage;
    private final String incomingPacketMessage;
    private final String incomingSocketCloseMessage;
    private final String acceptSocketConnectionMessage;
    private final String disconnectClient;
    private final String disconnectAllClients;
    private int outgoingPackets;
    private int incomingPackets;

    {
        this.prefix = "Signal > ";
        this.incomingLengthToLarge = this.prefix + "The received byte array length is larger than 255.";
        this.incomingPacketIsNull = this.prefix + "The received packet is null, check your PacketRegistry.";
        this.incomingInputThrowsAnException = this.prefix + "An accepted byte array throws an exception, please restart your service.";
        this.inputStreamThrowsAnException = this.prefix + "The input stream cannot be opened or is null.";
        this.outgoingLengthToLarge = this.prefix + "The sending byte array length is larger than 255.";
        this.outgoingSocketException = this.prefix + "The sending bytes can not be written or flushed.";
        this.outputStreamThrowsAnException = this.prefix + "The output stream is closed or is null.";
        this.outgoingPacketMessage = this.prefix + "The packet was successfully sent.";
        this.incomingPacketMessage = this.prefix + "The packet was received successfully.";
        this.incomingSocketCloseMessage = this.prefix + "The client connection will be closed.";
        this.acceptSocketConnectionMessage = this.prefix + "The client connection has been accepted.";
        this.disconnectClient = this.prefix + "Client: %client% will be disconnected.";
        this.disconnectAllClients = this.prefix + "All Clients will be disconnected.";
        this.outgoingPackets = 0;
        this.incomingPackets = 0;
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

    public void setIncomingPackets(final int incomingPackets) {
        this.incomingPackets = incomingPackets;
    }

    public int getIncomingPackets() {
        return this.incomingPackets;
    }

    public void setOutgoingPackets(final int outgoingPackets) {
        this.outgoingPackets = outgoingPackets;
    }

    public int getOutgoingPackets() {
        return this.outgoingPackets;
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

    public static SignalProvider getSignalProvider() {
        return SignalProvider.signalProvider;
    }

}
