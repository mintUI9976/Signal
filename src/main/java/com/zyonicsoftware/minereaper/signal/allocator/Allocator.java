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
 * @apiNote to check which state will be use
 * @see java.lang.Enum
 * @see Allocation
 * @see com.zyonicsoftware.minereaper.signal.client.Client
 * @see com.zyonicsoftware.minereaper.signal.server.Server
 * @see com.zyonicsoftware.minereaper.signal.incoming.InputStreamThread
 */
public class Allocator {

    /**
     * @implNote register which state will be use
     */
    private static Allocation allocation;

    /**
     * @return which state has been used
     */
    public static Allocation getAllocation() {
        return Allocator.allocation;
    }

    /**
     * @param allocation set which state will be use
     */
    public static void setAllocation(final Allocation allocation) {
        Allocator.allocation = allocation;
    }
}
