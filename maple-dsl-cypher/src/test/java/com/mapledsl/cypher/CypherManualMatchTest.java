package com.mapledsl.cypher;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static com.mapledsl.core.G.edge;
import static com.mapledsl.core.G.vertex;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CypherManualMatchTest extends CypherManualBaseTest {

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) RETURN v")
    public void should_match_vertex_return_itself_automatic(String expected) {
        assertEquals(expected, vertex(Person.class).render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (src) - [e:impact] -> (dst) RETURN e")
    public void should_match_edge_return_itself_automatic(String expected) {
        assertEquals(expected, edge(Impact.class).render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) WHERE v.name = \"bofa\" RETURN v")
    public void should_match_vertex_with_predicate(String expected) {
        assertEquals(expected, vertex(Person.class)
                .eq(Person::getName, "bofa")
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (src) - [e:impact] -> (dst) WHERE e.type IN [\"Type1\",\"Type2\"] RETURN e")
    public void should_match_edge_with_predicate(String expected) {
        assertEquals(expected, edge(Impact.class)
                .in(Impact::getType, Arrays.asList("Type1", "Type2"))
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) WHERE v.name = \"bofa\" RETURN v.name AS v")
    public void should_match_vertex_with_selection_complicit_alias(String expected) {
        assertEquals(expected, vertex(Person.class)
                .eq(Person::getName, "bofa")
                .selectAs(Person::getName, "v")
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (src) - [e:impact] -> (dst) WHERE e.type IN [\"Type1\",\"Type2\"] RETURN e.type AS impact_type,src.id AS src,dst.id AS dst")
    public void should_match_edge_with_selection_complicit_alias(String expected) {
        assertEquals(expected, edge(Impact.class)
                .in(Impact::getType, Arrays.asList("Type1", "Type2"))
                .selectAs(Impact::getType, "impact_type")
                .select(Impact::src, Impact::dst)
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) WHERE v.name = \"bofa\" RETURN v.name AS v,v.age AS age ORDER BY v ASC,age DESC")
    public void should_match_vertex_with_selection_ordering(String expected) {
        assertEquals(expected, vertex(Person.class)
                .eq(Person::getName, "bofa")
                .selectAs(Person::getName, "v").ascending()
                .select(Person::getAge).descending()
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (src) - [e:impact] -> (dst) WHERE e.type IN [\"Type1\",\"Type2\"] RETURN e.type AS impact_type,src.id AS src,dst.id AS dst " +
            "ORDER BY impact_type ASC,src,dst DESC")
    public void should_match_edge_with_selection_ordering(String expected) {
        assertEquals(expected, edge(Impact.class)
                .in(Impact::getType, Arrays.asList("Type1", "Type2"))
                .selectAs(Impact::getType, "impact_type").ascending()
                .select(Impact::src, Impact::dst).descending()
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) RETURN COUNT(*) AS cnt")
    public void should_match_vertex_with_shadow_selection(String expected) {
        assertEquals(expected, vertex(Person.class)
                .count("cnt")
                .render(configuration));
    }


    @ParameterizedTest
    @ValueSource(strings = "MATCH (src) - [e:impact] -> (dst) RETURN COUNT(*) AS cnt")
    public void should_match_edge_with_shadow_selection(String expected) {
        assertEquals(expected, edge(Impact.class)
                .count("cnt")
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) RETURN COUNT(v.name) AS name_cnt")
    public void should_match_vertex_with_shadow_selection_2(String expected) {
        assertEquals(expected, vertex(Person.class)
                .count(Person::getName, "name_cnt")
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (src) - [e:impact] -> (dst) RETURN COUNT(e.type) AS type_cnt")
    public void should_match_edge_with_shadow_selection_2(String expected) {
        assertEquals(expected, edge(Impact.class)
                .count(Impact::getType, "type_cnt")
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) RETURN COUNT(*) AS cnt,COUNT(v.name) AS name_cnt")
    public void should_match_vertex_with_selection_count(String expected) {
        assertEquals(expected, vertex(Person.class)
                .count("cnt")
                .count(Person::getName, "name_cnt")
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (src) - [e:impact] -> (dst) RETURN COUNT(*) AS cnt,COUNT(e.type) AS type_cnt")
    public void should_match_edge_with_selection_count(String expected) {
        assertEquals(expected, edge(Impact.class)
                .count("cnt")
                .count(Impact::getType, "type_cnt")
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) RETURN SUM(v.age) AS sum_age,SUM(v.age) AS sum_age2")
    public void should_match_vertex_with_selection_sum(String expected) {
        assertEquals(expected, vertex(Person.class)
                .sum(Person::getAge, "sum_age")
                .sum("age", "sum_age2")
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (src) - [e:impact] -> (dst) RETURN SUM(e.type) AS sum_type,SUM(e.type) AS sum_type2")
    public void should_match_edge_with_selection_sum(String expected) {
        assertEquals(expected, edge(Impact.class)
                .sum(Impact::getType, "sum_type")
                .sum("type", "sum_type2")
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) RETURN MAX(v.age) AS max_age,MAX(v.age) AS max_age2")
    public void should_match_vertex_with_selection_max(String expected) {
        assertEquals(expected, vertex(Person.class)
                .max(Person::getAge, "max_age")
                .max("age", "max_age2")
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (src) - [e:impact] -> (dst) RETURN SUM(e.type) AS max_type,MAX(e.type) AS max_type2")
    public void should_match_edge_with_selection_max(String expected) {
        assertEquals(expected, edge(Impact.class)
                .sum(Impact::getType, "max_type")
                .max("type", "max_type2")
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) RETURN MIN(v.age) AS min_age,MIN(v.age) AS min_age2")
    public void should_match_vertex_with_selection_min(String expected) {
        assertEquals(expected, vertex(Person.class)
                .min(Person::getAge, "min_age")
                .min("age", "min_age2")
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (src) - [e:impact] -> (dst) RETURN SUM(e.type) AS min_type,MIN(e.type) AS min_type2")
    public void should_match_edge_with_selection_min(String expected) {
        assertEquals(expected, edge(Impact.class)
                .sum(Impact::getType, "min_type")
                .min("type", "min_type2")
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) RETURN AVG(v.age) AS avg_age,AVG(v.age) AS avg_age2")
    public void should_match_vertex_with_selection_avg(String expected) {
        assertEquals(expected, vertex(Person.class)
                .avg(Person::getAge, "avg_age")
                .avg("age", "avg_age2")
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (src) - [e:impact] -> (dst) RETURN AVG(e.type) AS avg_type,AVG(e.type) AS avg_type2")
    public void should_match_edge_with_selection_avg(String expected) {
        assertEquals(expected, edge(Impact.class)
                .avg(Impact::getType, "avg_type")
                .avg("type", "avg_type2")
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) RETURN v SKIP 5 LIMIT 10")
    public void should_match_vertex_with_selection_limit(String expected) {
        assertEquals(expected, vertex(Person.class)
                .limit(5, 10)
                .render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (src) - [e:impact] -> (dst) RETURN e SKIP 5 LIMIT 10")
    public void should_match_edge_with_selection_limit(String expected) {
        assertEquals(expected, edge(Impact.class)
                .limit(5, 10)
                .render(configuration));
    }
}
