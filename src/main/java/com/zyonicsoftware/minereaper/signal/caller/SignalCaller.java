/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.caller;

import com.zyonicsoftware.minereaper.signal.client.Client;

import java.util.UUID;

/**
 * @author Niklas Griese
 * @see java.lang.reflect.Field
 * @see Class more information above event calling via reflection ->
 *     https://stackoverflow.com/questions/18471631/event-handling-with-java-reflection
 */
public abstract class SignalCaller {

  /** @apiNote to grab the class who called the event target */
  private final String calledClass;

  /** @param calledClass set the super class who called the event's */
  public SignalCaller(final String calledClass) {
    this.calledClass = calledClass;
  }

  /** @return the super class who called the event's */
  public String getCalledClass() {
    return this.calledClass;
  }

  /** @param message set the message via event call */
  public abstract void receivePacketIsNullMessage(final String message);

  /** @param message set the message via event call */
  public abstract void receiveLengthToLargeMessage(final String message);

  /** @param message set the message via event call */
  public abstract void sendLengthToLargeMessage(final String message);

  /** @param message set the message via event call */
  public abstract void sendPacketMessage(final String message);

  /** @param message set the message via event call */
  public abstract void receivePacketMessage(final String message);

  /** @param message set the message via event call */
  public abstract void receiveSocketCloseMessage(final String message);

  /** @param message set the message via event call */
  public abstract void acceptSocketConnectionMessage(final String message);

  /** @param message set the message via event call */
  public abstract void unAcceptedSocketConnectionMessage(final String message);

  /** @param message set the message via event call */
  public abstract void disconnectAllClientMessage(final String message);

  /** @param message set the message via event call */
  public abstract void disconnectClientMessage(final String message);

  /** @param message set the message via event call */
  public abstract void connectedClientMessage(final String message);

  /** @param message set the message via event call */
  public abstract void canceledJobMessage(final String message);

  /** @param message set the message via event call */
  public abstract void clientTimeoutMessage(final String message);

  /**
   * @param client set the client which is time outed and receive nothing response
   * @apiNote Important, you must let the client disconnect itself with the CLIENT_FROM_SERVER_SIDE
   *     or CLIENT_SIDE object an example can you find her
   * @see com.zyonicsoftware.minereaper.signal.example.ExampleSignalMessageInstance
   * @see com.zyonicsoftware.minereaper.signal.allocator.Allocator
   * @see com.zyonicsoftware.minereaper.signal.allocator.Allocation
   */
  public abstract void clientTimeout(final Client client);

  /**
   * @param uuid will be used to call client shutdown from client to server and the server
   *     disconnect the client.
   * @apiNote Important, you must call the event itself to disconnect him
   * @see Client
   * @see com.zyonicsoftware.minereaper.signal.allocator.Allocator
   * @see com.zyonicsoftware.minereaper.signal.allocator.Allocation
   * @see com.zyonicsoftware.minereaper.signal.example.ExampleSignalMessageInstance
   */
  public abstract void clientFromClientServerSideDisconnection(final UUID uuid);
}
