package com.zyonicsoftware.minereaper.signal.message;

import java.util.LinkedList;

public class MessengerRegistry {

    private static final LinkedList<Class<? extends SignalMessages>> registeredMessages = new LinkedList<>();

    public static Class<? extends SignalMessages> get() {
        return MessengerRegistry.registeredMessages.get(0);
    }

    public static LinkedList<Class<? extends SignalMessages>> getRegisteredMessages() {
        return MessengerRegistry.registeredMessages;
    }
}
