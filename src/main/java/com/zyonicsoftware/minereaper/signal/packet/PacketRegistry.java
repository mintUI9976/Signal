/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.packet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Niklas Griese
 * @see Packet
 */
public class PacketRegistry {

    /**
     * @apiNote init list of all packets
     */
    private static final List<Class<? extends Packet>> registeredPackets = new ArrayList<>();

    /**
     * @param packetClass will be use to find the int id of packet name
     * @return the int id of packet name
     */
    public static int indexOf(final Class<? extends Packet> packetClass) {
        return PacketRegistry.registeredPackets.indexOf(packetClass);
    }

    /**
     * @param index will be use to find the packet class of packed id
     * @return the packet class
     */
    public static Class<? extends Packet> get(final int index) {
        return PacketRegistry.registeredPackets.get(index);
    }

    /**
     * @param packetClass register an new packet
     */
    public static void registerPacket(@NotNull final Class<? extends Packet> packetClass) {
        // register packet
        PacketRegistry.registeredPackets.add(packetClass);
    }

    /**
     * @param packetClasses adds more on the same time
     */
    public static void registerPackets(@NotNull final List<Class<? extends Packet>> packetClasses) {
        // register packets
        PacketRegistry.registeredPackets.addAll(packetClasses);
    }
}
