package com.carlosbecker;

import com.carlosbecker.integration.TestPropertiesLoader;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AppTest {
    @BeforeClass
    public static void init() {
        TestPropertiesLoader.init("./src/test/resources/main.test.properties");
    }

    @AfterClass
    public static void shutdown() {
        TestPropertiesLoader.shutdown();
    }

    @Test
    public void testInstantiation() throws Exception {
        new App();
    }

    @Test
    public void testStaticCall() throws Exception {
        App.main(null);
    }
}