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

/**
 * @author Niklas Griese
 * @see SignalCaller
 */
public class SignalCallRegistry {

  /** @apiNote cached your reference (intern) */
  private static final LinkedList<Class<? extends SignalCaller>> registeredReferenceCaller =
      new LinkedList<>();

  /** @return your caller reference (intern) */
  public static Class<? extends SignalCaller> getReferenceCaller() {
    return SignalCallRegistry.registeredReferenceCaller.get(0);
  }

  /** @param signalCaller registered your reference */
  public static void registerReferenceCaller(final Class<? extends SignalCaller> signalCaller) {
    SignalCallRegistry.registeredReferenceCaller.add(signalCaller);
  }
}
