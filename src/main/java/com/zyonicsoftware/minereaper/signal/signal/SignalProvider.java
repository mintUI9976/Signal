package com.zyonicsoftware.minereaper.signal.signal;

public class SignalProvider {

    private static final SignalProvider signalProvider = new SignalProvider();

    private final String prefix;
    private final String inputStreamThrowsAnException;
    private final String incomingLengthToLarge;
    private final String incomingPacketIsNull;
    private final String incomingInputThrowsAnException;
    private final String outgoingLengthToLarge;

    {
        this.prefix = "Signal > ";
        this.incomingLengthToLarge = this.prefix + "The received byte array length is larger than 255.";
        this.incomingPacketIsNull = this.prefix + "The received packet is null, check your PacketRegistry.";
        this.incomingInputThrowsAnException = this.prefix + "An accepted byte array throws an exception, please restart your service.";
        this.inputStreamThrowsAnException = this.prefix + "The input stream cannot be opened or is null.";
        this.outgoingLengthToLarge = this.prefix + "The sending byte array length is larger than 255.";
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
