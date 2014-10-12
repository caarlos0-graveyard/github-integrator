package com.carlosbecker.process;

import java.io.IOException;

public class ProcessExecutor {
    public void execute(String path, String... params) throws IOException {
        Runtime.getRuntime().exec(path, params);
    }
}
