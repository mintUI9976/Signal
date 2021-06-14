/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.caller;

public abstract class SignalCaller {

  private final String calledClass;

  public SignalCaller(final String calledClass) {
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

  public abstract void unAcceptedSocketConnectionMessage(final String message);

  public abstract void disconnectAllClientMessage(final String message);

  public abstract void disconnectClientMessage(final String message);

  public abstract void canceledJob(final String message);
}
