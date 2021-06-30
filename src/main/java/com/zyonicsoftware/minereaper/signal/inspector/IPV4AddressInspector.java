/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.inspector;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Niklas Griese
 * @see com.zyonicsoftware.minereaper.signal.server.ServerSocketAcceptingThread
 */
public class IPV4AddressInspector {

  /** init static reference */
  private static final List<String> acceptedIPAddresses = new ArrayList<>();

  /** @param hostname add one specific hostname */
  public static void addIpv4Address(@NotNull final String hostname) {
    IPV4AddressInspector.acceptedIPAddresses.add(hostname);
  }

  /** @param hostname adds more then one hostname */
  public static void addIpv4Addresses(@NotNull final List<String> hostname) {
    IPV4AddressInspector.acceptedIPAddresses.addAll(hostname);
  }

  /** @return the list of accepted ip addresses to check */
  public static List<String> getAcceptedIPAddresses() {
    return IPV4AddressInspector.acceptedIPAddresses;
  }
}
