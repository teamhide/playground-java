package com.teamhide.playground.gatekeeper.util;

public class KeyGenerator {
    public static String generate(final String key, final String identifier) {
        return key + ":" + identifier;
    }
}
