package com.carlosbecker.integration;

public class TestPropertiesLoader {
    public static void init() {
        init("./src/test/resources/test.properties");
    }

    public static void init(String config) {
        System.setProperty("INTEGRATOR_CONFIG", config);
    }

    public static void shutdown() {
        System.clearProperty("INTEGRATOR_CONFIG");
    }
}
