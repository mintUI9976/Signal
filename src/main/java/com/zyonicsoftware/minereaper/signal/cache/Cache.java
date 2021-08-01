/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.cache;

import com.zyonicsoftware.minereaper.signal.client.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Niklas Griese
 * @see Client
 */
public class Cache {

    private static final List<Client> clientList = new ArrayList<>();
    private static long incomingPackets;
    private static long outgoingPackets;

    public static void add(final Client client) {
        Cache.getClientList().add(client);
    }

    public static void remove(final Client client) {
        Cache.getClientList().remove(client);
    }

    public static long getIncomingPackets() {
        return Cache.incomingPackets;
    }

    public static void setIncomingPackets(final long incomingPackets) {
        Cache.incomingPackets = incomingPackets;
    }

    public static long getOutgoingPackets() {
        return Cache.outgoingPackets;
    }

    public static void setOutgoingPackets(final long outgoingPackets) {
        Cache.outgoingPackets = outgoingPackets;
    }

    public static List<Client> getClientList() {
        return Cache.clientList;
    }
}
