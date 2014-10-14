package com.carlosbecker.process;

import java.io.IOException;
import java.util.List;
import com.google.common.collect.Lists;

public class ProcessExecutor {
    public void execute(String script, List<String> params) throws IOException {
        final List<String> cmd = Lists.newArrayList();
        cmd.add(script);
        cmd.addAll(params);
        Runtime.getRuntime().exec(cmd.toArray(new String[] {}));
    }
}
