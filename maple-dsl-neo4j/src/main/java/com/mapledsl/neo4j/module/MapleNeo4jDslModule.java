package com.mapledsl.neo4j.module;

import com.mapledsl.cypher.module.MapleCypherDslModule;
import org.jetbrains.annotations.NotNull;

public class MapleNeo4jDslModule extends MapleCypherDslModule {
    public static final String VERSION = "neo4j:1.0.0-release";

    @Override
    public @NotNull String version() {
        return VERSION;
    }
}
