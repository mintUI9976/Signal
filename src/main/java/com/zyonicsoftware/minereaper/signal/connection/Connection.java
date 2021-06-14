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

public abstract class Connection {

  private final AtomicReference<UUID> connectionUUID = new AtomicReference<>(UUID.randomUUID());

  public AtomicReference<UUID> getConnectionUUID() {
    return this.connectionUUID;
  }

  public abstract void connect() throws IOException;

  public abstract void disconnect() throws IOException;
}
