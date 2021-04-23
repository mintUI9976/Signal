package com.zyonicsoftware.minereaper.signal.caller;

import java.util.LinkedList;

public class SignalCallRegistry {

    private static final LinkedList<Class<? extends SignalCaller>> registeredReferenceCaller = new LinkedList<>();

    public static Class<? extends SignalCaller> get() {
        return SignalCallRegistry.registeredReferenceCaller.get(0);
    }

    public static LinkedList<Class<? extends SignalCaller>> getRegisteredReferenceCaller() {
        return SignalCallRegistry.registeredReferenceCaller;
    }
}
