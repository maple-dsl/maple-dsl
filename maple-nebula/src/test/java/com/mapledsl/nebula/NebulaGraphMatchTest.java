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
    @ValueSource(strings = "LOOKUP ON person YIELD vertex AS v | YIELD $-.v AS v,COUNT(*) AS cnt")
    public void should_match_vertex_with_shadow_selection(String expected) {
        assertEquals(expected, vertex(Person.class)
                .count("cnt")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON person YIELD person.name AS name | YIELD COUNT($-.name) AS name_cnt")
    public void should_match_vertex_with_shadow_selection_2(String expected) {
        assertEquals(expected, vertex(Person.class)
                .count(Person::getName, "name_cnt")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON person YIELD person.name AS name | YIELD COUNT(*) AS cnt,COUNT($-.name) AS name_cnt")
    public void should_match_vertex_with_selection_count(String expected) {
        assertEquals(expected, vertex(Person.class)
                .count("cnt")
                .count(Person::getName, "name_cnt")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON person YIELD person.age AS age | YIELD SUM($-.age) AS sum_age,SUM($-.age) AS sum_age2")
    public void should_match_vertex_with_selection_sum(String expected) {
        assertEquals(expected, vertex(Person.class)
                .sum(Person::getAge, "sum_age")
                .sum("age", "sum_age2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON person YIELD person.age AS age | YIELD MAX($-.age) AS max_age,MAX($-.age) AS max_age2")
    public void should_match_vertex_with_selection_max(String expected) {
        assertEquals(expected, vertex(Person.class)
                .max(Person::getAge, "max_age")
                .max("age", "max_age2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON person YIELD person.age AS age | YIELD MIN($-.age) AS min_age,MIN($-.age) AS min_age2")
    public void should_match_vertex_with_selection_min(String expected) {
        assertEquals(expected, vertex(Person.class)
                .min(Person::getAge, "min_age")
                .min("age", "min_age2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON person YIELD person.age AS age | YIELD AVG($-.age) AS avg_age,AVG($-.age) AS avg_age2")
    public void should_match_vertex_with_selection_avg(String expected) {
        assertEquals(expected, vertex(Person.class)
                .avg(Person::getAge, "avg_age")
                .avg("age", "avg_age2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON person YIELD person.name AS name | OFFSET 5 LIMIT 10")
    public void should_match_vertex_with_selection_limit(String expected) {
        assertEquals(expected, vertex(Person.class)
                .select(Person::getName)
                .limit(5, 10)
                .render());
    }
}
