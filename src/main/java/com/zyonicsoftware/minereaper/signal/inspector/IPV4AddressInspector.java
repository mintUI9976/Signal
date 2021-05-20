package com.zyonicsoftware.minereaper.signal.inspector;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class IPV4AddressInspector {

    private static final List<String> acceptedIPAddresses = new ArrayList<>();

    public static void addIpv4Address(@NotNull final String hostname) {
        IPV4AddressInspector.acceptedIPAddresses.add(hostname);
    }

    public static void addIpv4Addresses(@NotNull final List<String> hostname) {
        IPV4AddressInspector.acceptedIPAddresses.addAll(hostname);
    }

    public static List<String> getAcceptedIPAddresses() {
        return IPV4AddressInspector.acceptedIPAddresses;
    }
}
