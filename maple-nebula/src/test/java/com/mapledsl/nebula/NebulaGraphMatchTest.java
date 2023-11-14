package com.mapledsl.nebula;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.mapledsl.core.G.edge;
import static com.mapledsl.core.G.vertex;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NebulaGraphMatchTest extends NebulaGraphBaseTest {

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON person YIELD vertex AS v")
    public void should_match_vertex_return_itself_automatic(String expected) {
        assertEquals(expected, vertex(Person.class).render());
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON impact YIELD edge AS e")
    public void should_match_edge_return_itself_automatic(String expected) {
        assertEquals(expected, edge(Impact.class).render());
    }
}
