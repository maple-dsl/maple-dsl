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

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON person WHERE person.name == \"bofa\" YIELD vertex AS v")
    public void should_match_vertex_with_predicate(String expected) {
        assertEquals(expected, vertex(Person.class)
                .eq(Person::getName, "bofa")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON person WHERE person.name == \"bofa\" YIELD person.name AS v")
    public void should_match_vertex_with_selection_complicit_alias(String expected) {
        assertEquals(expected, vertex(Person.class)
                .eq(Person::getName, "bofa")
                .selectAs(Person::getName, "v")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON person WHERE person.name == \"bofa\" YIELD person.name AS v,person.age AS age | ORDER BY $-.v ASC,$-.age DESC")
    public void should_match_vertex_with_selection_ordering(String expected) {
        assertEquals(expected, vertex(Person.class)
                .eq(Person::getName, "bofa")
                .selectAs(Person::getName, "v").ascending()
                .select(Person::getAge).descending()
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON person WHERE person.name == \"bofa\" YIELD person.name AS v | OFFSET 5 LIMIT 10")
    public void should_match_vertex_with_selection_limit(String expected) {
        assertEquals(expected, vertex(Person.class)
                .eq(Person::getName, "bofa")
                .selectAs(Person::getName, "v")
                .limit(5, 10)
                .render());
    }
}
