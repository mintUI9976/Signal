package com.zyonicsoftware.minereaper.signal.message;

public abstract class SignalMessages {

    private final String calledClass;

    public SignalMessages(final String calledClass) {
        this.calledClass = calledClass;
    }

    public String getCalledClass() {
        return this.calledClass;
    }

    public abstract void receivePacketIsNullMessage(final String message);

    public abstract void receiveLengthToLargeMessage(final String message);

    public abstract void sendLengthToLargeMessage(final String message);

    public abstract void sendPacketMessage(final String message);

    public abstract void receivePacketMessage(final String message);

    public abstract void receiveSocketCloseMessage(final String message);

    public abstract void acceptSocketConnectionMessage(final String message);

}
