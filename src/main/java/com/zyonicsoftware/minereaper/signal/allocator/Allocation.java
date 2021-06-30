/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.allocator;

/**
 * @author Niklas Griese
 * @see java.lang.Enum
 * @see Allocator
 * @see com.zyonicsoftware.minereaper.signal.client.Client
 * @see com.zyonicsoftware.minereaper.signal.server.Server
 * @see com.zyonicsoftware.minereaper.signal.incoming.InputStreamThread
 * @apiNote to check which state will be use
 */
public enum Allocation {
  /** @implNote called client on server */
  CLIENT_FROM_SERVER_SIDE,
  /** @implNote called client on client */
  CLIENT_SIDE,
}
