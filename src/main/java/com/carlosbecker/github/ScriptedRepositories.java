package com.carlosbecker.github;

import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScriptedRepositories {
    private final List<ScriptedRepository> repositories;

    public Iterator<ScriptedRepository> iterator() {
        return repositories.iterator();
    }
}
