package com.zyonicsoftware.minereaper.signal.message;

public abstract class SignalMessages {

    public SignalMessages() {
    }

    public abstract void incomingPacketIsNullMessage(final String message);

    public abstract void incomingLengthToLargeMessage(final String message);

    public abstract void outgoingLengthToLargeMessage(final String message);

    public abstract void sendPacketMessage(final String message);

    public abstract void receivePacketMessage(final String message);

}
