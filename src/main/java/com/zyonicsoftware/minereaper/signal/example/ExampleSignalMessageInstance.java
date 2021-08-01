/*
 *
 *  * Copyright (c) 2021. Zyonic Software - Niklas Griese
 *  * This File, its contents and by extention the corresponding project is property of Zyonic Software and may not be used without explicit permission to do so.
 *  *
 *  * contact(at)zyonicsoftware.com
 *
 */

package com.zyonicsoftware.minereaper.signal.example;

import com.zyonicsoftware.minereaper.signal.allocator.Allocator;
import com.zyonicsoftware.minereaper.signal.caller.SignalCaller;

import java.util.UUID;

/**
 * @author Niklas Griese
 * @see com.zyonicsoftware.minereaper.signal.caller.SignalCaller
 * @see Allocator
 * @see com.zyonicsoftware.minereaper.signal.allocator.Allocation
 */
public class ExampleSignalMessageInstance extends SignalCaller {

    public ExampleSignalMessageInstance(final String calledClass) {
        super(calledClass);
    }

    @Override
    public void receivePacketIsNullMessage(final String message) {
    }

    @Override
    public void receiveLengthToLargeMessage(final String message) {
    }

    @Override
    public void sendLengthToLargeMessage(final String message) {
    }

    @Override
    public void sendPacketMessage(final String message) {
        System.out.println(message);
    }

    @Override
    public void receivePacketMessage(final String message) {
        System.out.println(message);
    }

    @Override
    public void receiveSocketCloseMessage(final String message) {
    }

    @Override
    public void acceptSocketConnectionMessage(final String message) {
    }

    @Override
    public void unAcceptedSocketConnectionMessage(final String message) {
    }

    @Override
    public void disconnectAllClientMessage(final String message) {
    }

    @Override
    public void disconnectClientMessage(final String message) {
        System.out.println(message);
    }

    @Override
    public void connectedClientMessage(final String message) {
        System.out.println(message);
    }

    @Override
    public void canceledJobMessage(final String message) {
    }

    @Override
    public void clientTimeoutMessage(final String message) {
        System.out.println(message);
    }

    /**
     * @param uuid set the client which is time outed and receive nothing response
     * @apiNote at the first case the server has checked the client receive nothing response at the
     * second case the client has checked the server send nothing response to receive them
     */
    @Override
    public void clientTimeout(final UUID uuid) {
        switch (Allocator.getAllocation()) {
            case CLIENT_FROM_SERVER_SIDE:
                ExampleServer.getServer().disconnectClient(uuid);
                break;
            case CLIENT_SIDE:
                System.out.println("You are timed out.");
                break;
        }
    }

    @Override
    public void clientFromClientServerSideDisconnection(final UUID uuid) {
        ExampleServer.getServer().disconnectClient(uuid);
    }
}
