/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.example;

import com.zyonicsoftware.minereaper.signal.caller.SignalCaller;
import com.zyonicsoftware.minereaper.signal.client.Client;

public class ExampleSignalMessageInstance extends SignalCaller {

  public ExampleSignalMessageInstance(final String calledClass) {
    super(calledClass);
  }

  @Override
  public void receivePacketIsNullMessage(final String message) {}

  @Override
  public void receiveLengthToLargeMessage(final String message) {}

  @Override
  public void sendLengthToLargeMessage(final String message) {}

  @Override
  public void sendPacketMessage(final String message) {}

  @Override
  public void receivePacketMessage(final String message) {}

  @Override
  public void receiveSocketCloseMessage(final String message) {}

  @Override
  public void acceptSocketConnectionMessage(final String message) {}

  @Override
  public void unAcceptedSocketConnectionMessage(final String message) {}

  @Override
  public void disconnectAllClientMessage(final String message) {}

  @Override
  public void disconnectClientMessage(final String message) {
    System.out.println(message);
  }

  @Override
  public void connectedClientMessage(final String message) {
    System.out.println(message);
  }

  @Override
  public void canceledJobMessage(final String message) {}

  @Override
  public void clientTimeoutMessage(final String message) {
    System.out.println(message);
  }

  @Override
  public void clientTimeout(final Client client) {
    ExampleServer.getServer().disconnectClient(client.getConnectionUUID().get());
  }
}
