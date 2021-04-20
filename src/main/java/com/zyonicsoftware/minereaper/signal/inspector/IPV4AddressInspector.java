package com.zyonicsoftware.minereaper.signal.inspector;

import java.util.ArrayList;
import java.util.List;

public class IPV4AddressInspector {

    private static final List<String> acceptedIPAddresses = new ArrayList<>();

    public static List<String> getAcceptedIPAddresses() {
        return IPV4AddressInspector.acceptedIPAddresses;
    }
}
