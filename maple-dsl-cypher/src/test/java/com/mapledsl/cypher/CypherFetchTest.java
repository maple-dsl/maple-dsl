package com.mapledsl.cypher;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.mapledsl.core.G.edge;
import static com.mapledsl.core.G.vertex;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CypherFetchTest extends CypherBaseTest {

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) WHERE id(v) IN [\"p001\"] RETURN v")
    public void should_fetch_vertex_return_itself_automatic(String expected) {
        assertEquals(expected, vertex(Person.class, "p001").render());
        assertEquals(expected, vertex("person", "p001").render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) WHERE id(v) IN [\"p001\",\"p002\"] RETURN v")
    public void should_fetch_vertices_text_id_return_itself_automatic(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002").render());
        assertEquals(expected, vertex("person", "p001", "p002").render());
        assertEquals(expected, vertex("person", Stream.of("p001", "p002")).render());
        assertEquals(expected, vertex("person", Stream.<String>builder().add("p001").add("p002").build()).render());
        assertEquals(expected, vertex("person", ImmutableList.of("p001", "p002").stream()).render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) WHERE id(v) IN [1,2,3] RETURN v")
    public void should_fetch_vertices_numeric_id_return_itself_automatic(String expected) {
        assertEquals(expected, vertex(Person.class, 1, 2, 3).render());
        assertEquals(expected, vertex("person", 1, 2, 3).render());
        assertEquals(expected, vertex("person", LongStream.of(1, 2, 3)).render());
        assertEquals(expected, vertex("person", LongStream.builder().add(1).add(2).add(3).build()).render());
        assertEquals(expected, vertex("person", LongStream.rangeClosed(1, 3)).render());
        assertEquals(expected, vertex("person", ImmutableList.of(1L, 2L, 3L).stream().mapToLong(Long::intValue)).render());
        assertEquals(expected, vertex("person", ImmutableList.of(1, 2, 3).stream().mapToLong(Integer::intValue)).render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH () - [e:impact] - () WHERE id(e) IN [\"e001\"] RETURN e")
    public void should_fetch_edge_return_itself_automatic(String expected) {
        assertEquals(expected, edge(Impact.class, Impact.of("e001", "p001", "p002")).render());
        assertEquals(expected, edge("impact", Impact.of("e001", "p001", "p002")).render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH () - [e:impact] - () WHERE id(e) IN [\"e001\",\"e002\",\"e003\"] RETURN e")
    public void should_fetch_edges_string_vid_return_itself_automatic(String expected) {
        assertEquals(expected, edge(Impact.class,
                Impact.of("e001", "p001", "p002"),
                Impact.of("e002", "p001", "p002"),
                Impact.of("e003", "p001", "p003"))
                .render());
        assertEquals(expected, edge("impact",
                Impact.of("e001", "p001", "p002"),
                Impact.of("e002", "p001", "p002"),
                Impact.of("e003", "p001", "p003"))
                .render());
        assertEquals(expected, edge("impact", Arrays.asList(
                Impact.of("e001", "p001", "p002"),
                Impact.of("e002", "p001", "p002"),
                Impact.of("e003", "p001", "p003")))
                .render());
        assertEquals(expected, edge("impact", ImmutableList.of(
                Impact.of("e001", "p001", "p002"),
                Impact.of("e002", "p001", "p002"),
                Impact.of("e003", "p001", "p003")))
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH () - [e:impact] - () WHERE id(e) IN [1001,1002,1003] RETURN e")
    public void should_fetch_edges_numeric_vid_return_itself_automatic(String expected) {
        assertEquals(expected, edge(Impact.class,
                Impact.of(1001, 2001, 2002),
                Impact.of(1002, 2001, 2002),
                Impact.of(1003, 2001, 2003))
                .render());
        assertEquals(expected, edge("impact", Arrays.asList(
                Impact.of(1001, 2001, 2002),
                Impact.of(1002, 2001, 2002),
                Impact.of(1003, 2001, 2003)))
                .render());
        assertEquals(expected, edge("impact", ImmutableList.of(
                Impact.of(1001, 2001, 2002),
                Impact.of(1002, 2001, 2002),
                Impact.of(1003, 2001, 2003)))
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) WHERE id(v) IN [\"p001\",\"p002\"] RETURN v.name AS p_name")
    public void should_fetch_vertex_with_selection_complicit_alias(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
                .selectAs(Person::getName, "p_name")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH () - [e:impact] - () WHERE id(e) IN [\"e001\",\"e002\",\"e003\"] RETURN dst(e) AS impact_dst_vid")
    public void should_fetch_edge_with_selection_complicit_alias(String expected) {
        assertEquals(expected, edge(Impact.class,
                Impact.of("e001", "p001", "p002"),
                Impact.of("e002", "p001", "p002"),
                Impact.of("e003", "p001", "p003"))
                .selectAs(Impact::dst, "impact_dst_vid")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) WHERE id(v) IN [\"p001\",\"p002\"] RETURN v.name AS p_name,v.age AS age ORDER BY p_name ASC,age DESC")
    public void should_fetch_vertex_with_selection_ordering(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
                .selectAs(Person::getName, "p_name").ascending()
                .select(Person::getAge).descending()
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH () - [e:impact] - () WHERE id(e) IN [\"e001\",\"e002\",\"e003\"] RETURN dst(e) AS impact_dst_vid,e.type AS type ORDER BY impact_dst_vid ASC,type DESC")
    public void should_fetch_edge_with_selection_ordering(String expected) {
        assertEquals(expected, edge(Impact.class,
                Impact.of("e001", "p001", "p002"),
                Impact.of("e002", "p001", "p002"),
                Impact.of("e003", "p001", "p003"))
                .selectAs(Impact::dst, "impact_dst_vid").ascending()
                .select(Impact::getType).descending()
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) WHERE id(v) IN [\"p001\",\"p002\"] RETURN COUNT(*) AS cnt")
    public void should_fetch_vertex_with_shadow_selection(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
                .count("cnt")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH () - [e:impact] - () WHERE id(e) IN [\"e001\",\"e002\",\"e003\"] RETURN COUNT(*) AS cnt")
    public void should_fetch_edge_with_shadow_selection(String expected) {
        assertEquals(expected, edge(Impact.class,
                Impact.of("e001", "p001", "p002"),
                Impact.of("e002", "p001", "p002"),
                Impact.of("e003", "p001", "p003"))
                .count("cnt")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) WHERE id(v) IN [\"p001\",\"p002\"] RETURN COUNT(v.name) AS name_cnt")
    public void should_fetch_vertex_with_shadow_selection_2(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
                .count(Person::getName, "name_cnt")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH () - [e:impact] - () WHERE id(e) IN [\"e001\",\"e002\",\"e003\"] RETURN COUNT(e.type) AS type_cnt")
    public void should_fetch_edge_with_shadow_selection_2(String expected) {
        assertEquals(expected, edge(Impact.class,
                Impact.of("e001", "p001", "p002"),
                Impact.of("e002", "p001", "p002"),
                Impact.of("e003", "p001", "p003"))
                .count(Impact::getType, "type_cnt")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) WHERE id(v) IN [\"p001\",\"p002\"] RETURN COUNT(*) AS cnt,COUNT(v.name) AS name_cnt")
    public void should_fetch_vertex_with_selection_count(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
                .count("cnt")
                .count(Person::getName, "name_cnt")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH () - [e:impact] - () WHERE id(e) IN [\"e001\",\"e002\",\"e003\"] RETURN COUNT(*) AS cnt,COUNT(e.type) AS type_cnt")
    public void should_fetch_edge_with_selection_count(String expected) {
        assertEquals(expected, edge(Impact.class,
                Impact.of("e001", "p001", "p002"),
                Impact.of("e002", "p001", "p002"),
                Impact.of("e003", "p001", "p003"))
                .count("cnt")
                .count(Impact::getType, "type_cnt")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) WHERE id(v) IN [\"p001\",\"p002\"] RETURN SUM(v.age) AS sum_age,SUM(v.age) AS sum_age2")
    public void should_fetch_vertex_with_selection_sum(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
                .sum(Person::getAge, "sum_age")
                .sum("age", "sum_age2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH () - [e:impact] - () WHERE id(e) IN [\"e001\",\"e002\",\"e003\"] RETURN SUM(e.type) AS sum_type,SUM(e.type) AS sum_type2")
    public void should_fetch_edge_with_selection_sum(String expected) {
        assertEquals(expected, edge(Impact.class,
                Impact.of("e001", "p001", "p002"),
                Impact.of("e002", "p001", "p002"),
                Impact.of("e003", "p001", "p003"))
                .sum(Impact::getType, "sum_type")
                .sum("type", "sum_type2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) WHERE id(v) IN [\"p001\",\"p002\"] RETURN MAX(v.age) AS max_age,MAX(v.age) AS max_age2")
    public void should_fetch_vertex_with_selection_max(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
                .max(Person::getAge, "max_age")
                .max("age", "max_age2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH () - [e:impact] - () WHERE id(e) IN [\"e001\",\"e002\",\"e003\"] RETURN MAX(e.type) AS max_type,MAX(e.type) AS max_type2")
    public void should_fetch_edge_with_selection_max(String expected) {
        assertEquals(expected, edge(Impact.class,
                Impact.of("e001", "p001", "p002"),
                Impact.of("e002", "p001", "p002"),
                Impact.of("e003", "p001", "p003"))
                .max(Impact::getType, "max_type")
                .max("type", "max_type2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) WHERE id(v) IN [\"p001\",\"p002\"] RETURN MIN(v.age) AS min_age,MIN(v.age) AS min_age2")
    public void should_fetch_vertex_with_selection_min(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
                .min(Person::getAge, "min_age")
                .min("age", "min_age2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH () - [e:impact] - () WHERE id(e) IN [\"e001\",\"e002\",\"e003\"] RETURN MIN(e.type) AS min_type,MIN(e.type) AS min_type2")
    public void should_fetch_edge_with_selection_min(String expected) {
        assertEquals(expected, edge(Impact.class,
                Impact.of("e001", "p001", "p002"),
                Impact.of("e002", "p001", "p002"),
                Impact.of("e003", "p001", "p003"))
                .min(Impact::getType, "min_type")
                .min("type", "min_type2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) WHERE id(v) IN [\"p001\",\"p002\"] RETURN AVG(v.age) AS avg_age,AVG(v.age) AS avg_age2")
    public void should_fetch_vertex_with_selection_avg(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
                .avg(Person::getAge, "avg_age")
                .avg("age", "avg_age2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH () - [e:impact] - () WHERE id(e) IN [\"e001\",\"e002\",\"e003\"] RETURN AVG(e.type) AS avg_type,AVG(e.type) AS avg_type2")
    public void should_fetch_edge_with_selection_avg(String expected) {
        assertEquals(expected, edge(Impact.class,
                Impact.of("e001", "p001", "p002"),
                Impact.of("e002", "p001", "p002"),
                Impact.of("e003", "p001", "p003"))
                .avg(Impact::getType, "avg_type")
                .avg("type", "avg_type2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) WHERE id(v) IN [\"p001\",\"p002\",\"p003\"] RETURN v.name AS name OFFSET 1 LIMIT 2")
    public void should_match_vertex_with_selection_limit(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002", "p003")
                .select(Person::getName)
                .limit(1, 2)
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH () - [e:impact] - () WHERE id(e) IN [\"e001\",\"e002\",\"e003\"] RETURN e.type AS type OFFSET 1 LIMIT 2")
    public void should_match_edge_with_selection_limit(String expected) {
        assertEquals(expected, edge(Impact.class,
                Impact.of("e001", "p001", "p002"),
                Impact.of("e002", "p001", "p002"),
                Impact.of("e003", "p001", "p003"))
                .select(Impact::getType)
                .limit(1, 2)
                .render());
    }
}
