package com.mapledsl.nebula;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

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
    @ValueSource(strings = "LOOKUP ON impact WHERE impact.type IN [\"Type1\",\"Type2\"] YIELD edge AS e")
    public void should_match_edge_with_predicate(String expected) {
        assertEquals(expected, edge(Impact.class)
                .in(Impact::getType, Arrays.asList("Type1", "Type2"))
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
    @ValueSource(strings = "LOOKUP ON impact WHERE impact.type IN [\"Type1\",\"Type2\"] " +
            "YIELD impact.type AS impact_type,src(edge) AS src,dst(edge) AS dst,rank(edge) AS rank")
    public void should_match_edge_with_selection_complicit_alias(String expected) {
        assertEquals(expected, edge(Impact.class)
                .in(Impact::getType, Arrays.asList("Type1", "Type2"))
                .selectAs(Impact::getType, "impact_type")
                .select(Impact::src, Impact::dst, Impact::rank)
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON person WHERE person.name == \"bofa\" " +
            "YIELD person.name AS v,person.age AS age " +
            "| ORDER BY $-.v ASC,$-.age DESC")
    public void should_match_vertex_with_selection_ordering(String expected) {
        assertEquals(expected, vertex(Person.class)
                .eq(Person::getName, "bofa")
                .selectAs(Person::getName, "v").ascending()
                .select(Person::getAge).descending()
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON impact WHERE impact.type IN [\"Type1\",\"Type2\"] " +
            "YIELD impact.type AS impact_type,src(edge) AS src,dst(edge) AS dst,rank(edge) AS rank " +
            "| ORDER BY $-.impact_type ASC,$-.src,$-.dst,$-.rank DESC")
    public void should_match_edge_with_selection_ordering(String expected) {
        assertEquals(expected, edge(Impact.class)
                .in(Impact::getType, Arrays.asList("Type1", "Type2"))
                .selectAs(Impact::getType, "impact_type").ascending()
                .select(Impact::src, Impact::dst, Impact::rank).descending()
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON person YIELD vertex AS v | YIELD COUNT(*) AS cnt")
    public void should_match_vertex_with_shadow_selection(String expected) {
        assertEquals(expected, vertex(Person.class)
                .count("cnt")
                .render());
    }


    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON impact YIELD edge AS e | YIELD COUNT(*) AS cnt")
    public void should_match_edge_with_shadow_selection(String expected) {
        assertEquals(expected, edge(Impact.class)
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
    @ValueSource(strings = "LOOKUP ON impact YIELD impact.type AS type | YIELD COUNT($-.type) AS type_cnt")
    public void should_match_edge_with_shadow_selection_2(String expected) {
        assertEquals(expected, edge(Impact.class)
                .count(Impact::getType, "type_cnt")
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
    @ValueSource(strings = "LOOKUP ON impact YIELD impact.type AS type | YIELD COUNT(*) AS cnt,COUNT($-.type) AS type_cnt")
    public void should_match_edge_with_selection_count(String expected) {
        assertEquals(expected, edge(Impact.class)
                .count("cnt")
                .count(Impact::getType, "type_cnt")
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
    @ValueSource(strings = "LOOKUP ON impact YIELD rank(edge) AS rank | YIELD SUM($-.rank) AS sum_rank,SUM($-.rank) AS sum_rank2")
    public void should_match_edge_with_selection_sum(String expected) {
        assertEquals(expected, edge(Impact.class)
                .sum(Impact::rank, "sum_rank")
                .sum("rank", "sum_rank2")
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
    @ValueSource(strings = "LOOKUP ON impact YIELD rank(edge) AS rank | YIELD MAX($-.rank) AS max_rank,MAX($-.rank) AS max_rank2")
    public void should_match_edge_with_selection_max(String expected) {
        assertEquals(expected, edge(Impact.class)
                .max(Impact::rank, "max_rank")
                .max("rank", "max_rank2")
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
    @ValueSource(strings = "LOOKUP ON impact YIELD rank(edge) AS rank | YIELD MIN($-.rank) AS min_rank,MIN($-.rank) AS min_rank2")
    public void should_match_edge_with_selection_min(String expected) {
        assertEquals(expected, edge(Impact.class)
                .min(Impact::rank, "min_rank")
                .min("rank", "min_rank2")
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
    @ValueSource(strings = "LOOKUP ON impact YIELD rank(edge) AS rank | YIELD AVG($-.rank) AS avg_rank,AVG($-.rank) AS avg_rank2")
    public void should_match_edge_with_selection_avg(String expected) {
        assertEquals(expected, edge(Impact.class)
                .avg(Impact::rank, "avg_rank")
                .avg("rank", "avg_rank2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON person YIELD vertex AS v | OFFSET 5 LIMIT 10")
    public void should_match_vertex_with_selection_limit(String expected) {
        assertEquals(expected, vertex(Person.class)
                .limit(5, 10)
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON impact YIELD edge AS e | OFFSET 5 LIMIT 10")
    public void should_match_edge_with_selection_limit(String expected) {
        assertEquals(expected, edge(Impact.class)
                .limit(5, 10)
                .render());
    }
}
