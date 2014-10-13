package com.carlosbecker.process;

import java.io.IOException;

public class ProcessExecutor {
    public void execute(String... cmdArray) throws IOException {
        Runtime.getRuntime().exec(cmdArray);
    }
}
