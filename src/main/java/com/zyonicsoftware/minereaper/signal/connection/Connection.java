/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.connection;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Niklas Griese
 * @see AtomicReference
 */
public abstract class Connection {
  /** @apiNote generated an uuid to verify any client */
  private final AtomicReference<UUID> connectionUUID = new AtomicReference<>(UUID.randomUUID());

  /** @return the generated uuid */
  public AtomicReference<UUID> getConnectionUUID() {
    return this.connectionUUID;
  }

  /**
   * @apiNote call socket connect
   * @throws IOException when socket connection failed
   */
  public abstract void connect() throws IOException;

  /**
   * @apiNote call socket disconnect
   * @throws IOException when socket disconnect failed
   */
  public abstract void disconnect() throws IOException;
}
