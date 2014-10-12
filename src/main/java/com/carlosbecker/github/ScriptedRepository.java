package com.carlosbecker.github;

import lombok.Data;
import lombok.experimental.Builder;
import org.eclipse.egit.github.core.RepositoryId;

@Data
@Builder
public class ScriptedRepository {
    private final RepositoryId id;
    private final String script;
}
