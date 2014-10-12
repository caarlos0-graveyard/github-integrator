package com.carlosbecker;

import javax.inject.Inject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;

@RunWith(GuiceTestRunner.class)
@GuiceModules(MainModule.class)
public class MainIntegratorTest {
    @Inject
    private MainIntegrator integrator;

    @BeforeClass
    public static void before() throws Exception {
        TestPropertiesLoader.load();
    }

    @Test
    public void testItWorks() throws Exception {
        integrator.work();
    }
}
