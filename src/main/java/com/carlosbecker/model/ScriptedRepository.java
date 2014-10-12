package com.carlosbecker.model;

import lombok.Data;
import lombok.experimental.Builder;
import org.eclipse.egit.github.core.RepositoryId;

@Data
@Builder
public class ScriptedRepository {
    private final String owner;
    private final String name;
    private final String regex;
    private final String script;

    public RepositoryId getId() {
        return new RepositoryId(owner, name);
    }
}
