/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.caller;

import java.util.LinkedList;

public class SignalCallRegistry {

    private static final LinkedList<Class<? extends SignalCaller>> registeredReferenceCaller = new LinkedList<>();

    public static Class<? extends SignalCaller> getReferenceCaller() {
        return SignalCallRegistry.registeredReferenceCaller.get(0);
    }

    public static void registerReferenceCaller(final Class<? extends SignalCaller> signalCaller) {
        SignalCallRegistry.registeredReferenceCaller.add(signalCaller);
    }

    public static LinkedList<Class<? extends SignalCaller>> getRegisteredReferenceCaller() {
        return SignalCallRegistry.registeredReferenceCaller;
    }
}
