package com.carlosbecker.process;

import static java.util.Arrays.asList;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.carlosbecker.ProcessModule;
import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;

@RunWith(GuiceTestRunner.class)
@GuiceModules(ProcessModule.class)
public class ProcessExecutorTest {

    @Inject
    private ProcessExecutor executor;

    @Test
    public void testName() throws Exception {
        executor.execute("./src/test/resources/sample.sh", asList("user", "repo", "whatever"));
    }

}
