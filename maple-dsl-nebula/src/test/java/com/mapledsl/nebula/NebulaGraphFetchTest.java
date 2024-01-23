package com.mapledsl.nebula;

import com.google.common.collect.ImmutableList;
import com.mapledsl.nebula.model.NebulaEdgeID;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static com.mapledsl.core.G.edge;
import static com.mapledsl.core.G.vertex;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NebulaGraphFetchTest extends NebulaGraphBaseTest {

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON person \"p001\" YIELD vertex AS v")
    public void should_fetch_vertex_return_itself_automatic(String expected) {
        assertEquals(expected, vertex(Person.class, "p001").render());
        assertEquals(expected, vertex("person", "p001").render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON person \"p001\",\"p002\" YIELD vertex AS v")
    public void should_fetch_vertices_text_id_return_itself_automatic(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002").render());
        assertEquals(expected, vertex("person", "p001", "p002").render());
        assertEquals(expected, vertex("person", ImmutableList.of("p001", "p002")).render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON person 1,2,3 YIELD vertex AS v")
    public void should_fetch_vertices_numeric_id_return_itself_automatic(String expected) {
        assertEquals(expected, vertex(PersonHash.class, 1L, 2L, 3L).render());
        assertEquals(expected, vertex("person", 1, 2, 3).render());
        assertEquals(expected, vertex("person", ImmutableList.of(1L, 2L, 3L)).render());
        assertEquals(expected, vertex("person", ImmutableList.of(1, 2, 3)).render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON impact \"p001\"->\"p002\"@0 YIELD edge AS e")
    public void should_fetch_edge_return_itself_automatic(String expected) {
        assertEquals(expected, edge(Impact.class, new NebulaEdgeID<>("p001", "p002")).render());
        assertEquals(expected, edge("impact", new NebulaEdgeID<>("p001", "p002")).render());
    }

    // fetchE => go from src over edge_type where id($$) == 'p002' yield edge as e
    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON impact \"p001\"->\"p002\"@0,\"p001\"->\"p002\"@1,\"p001\"->\"p003\"@0 YIELD edge AS e")
    public void should_fetch_edges_string_vid_return_itself_automatic(String expected) {
        assertEquals(expected, edge(Impact.class,
                new NebulaEdgeID<>("p001", "p002", 0L),
                new NebulaEdgeID<>("p001", "p002", 1L),
                new NebulaEdgeID<>("p001", "p003"))
                .render());
        assertEquals(expected, edge("impact",
                new NebulaEdgeID<>("p001", "p002", 0L),
                new NebulaEdgeID<>("p001", "p002", 1L),
                new NebulaEdgeID<>("p001", "p003"))
                .render());
        assertEquals(expected, edge("impact",
                new NebulaEdgeID<>("p001", "p002", 0L),
                new NebulaEdgeID<>("p001", "p002", 1L),
                new NebulaEdgeID<>("p001", "p003"))
                .render());
        assertEquals(expected, edge("impact", Arrays.asList(
                new NebulaEdgeID<>("p001", "p002", 0L),
                new NebulaEdgeID<>("p001", "p002", 1L),
                new NebulaEdgeID<>("p001", "p003")))
                .render());
        assertEquals(expected, edge("impact", ImmutableList.of(
                new NebulaEdgeID<>("p001", "p002", 0L),
                new NebulaEdgeID<>("p001", "p002", 1L),
                new NebulaEdgeID<>("p001", "p003")))
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON impact 1->2@0,1->2@1,1->3@0 YIELD edge AS e")
    public void should_fetch_edges_numeric_vid_return_itself_automatic(String expected) {
        assertEquals(expected, edge(ImpactHash.class,
                new NebulaEdgeID<>(1L, 2L, 0L),
                new NebulaEdgeID<>(1L, 2L, 1L),
                new NebulaEdgeID<>(1L, 3L, 0L))
                .render());
        assertEquals(expected, edge("impact",
                new NebulaEdgeID<>(1L, 2L, 0L),
                new NebulaEdgeID<>(1L, 2L, 1L),
                new NebulaEdgeID<>(1L, 3L, 0L))
                .render());
        assertEquals(expected, edge("impact", Arrays.asList(
                new NebulaEdgeID<>(1L, 2L, 0L),
                new NebulaEdgeID<>(1L, 2L, 1L),
                new NebulaEdgeID<>(1L, 3L, 0L)))
                .render());
        assertEquals(expected, edge("impact", ImmutableList.of(
                new NebulaEdgeID<>(1L, 2L, 0L),
                new NebulaEdgeID<>(1L, 2L, 1L),
                new NebulaEdgeID<>(1L, 3L, 0L)))
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON person \"p001\",\"p002\" YIELD person.name AS p_name")
    public void should_fetch_vertex_with_selection_complicit_alias(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
                .selectAs(Person::getName, "p_name")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON impact \"p001\"->\"p002\"@0,\"p001\"->\"p002\"@1,\"p001\"->\"p003\"@0 YIELD dst(edge) AS impact_dst_vid")
    public void should_fetch_edge_with_selection_complicit_alias(String expected) {
        assertEquals(expected, edge(Impact.class,
                new NebulaEdgeID<>("p001", "p002", 0L),
                new NebulaEdgeID<>("p001", "p002", 1L),
                new NebulaEdgeID<>("p001", "p003"))
                .selectAs(Impact::dst, "impact_dst_vid")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON person \"p001\",\"p002\" YIELD person.name AS p_name,person.age AS age | ORDER BY $-.p_name ASC,$-.age DESC")
    public void should_fetch_vertex_with_selection_ordering(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
                .selectAs(Person::getName, "p_name").ascending()
                .select(Person::getAge).descending()
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON impact \"p001\"->\"p002\"@0,\"p001\"->\"p002\"@1,\"p001\"->\"p003\"@0 " +
            "YIELD dst(edge) AS impact_dst_vid,rank(edge) AS rank " +
            "| ORDER BY $-.impact_dst_vid ASC,$-.rank DESC")
    public void should_fetch_edge_with_selection_ordering(String expected) {
        assertEquals(expected, edge(Impact.class,
                new NebulaEdgeID<>("p001", "p002", 0L),
                new NebulaEdgeID<>("p001", "p002", 1L),
                new NebulaEdgeID<>("p001", "p003"))
                .selectAs(Impact::dst, "impact_dst_vid").ascending()
                .select(Impact::rank).descending()
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON person \"p001\",\"p002\" YIELD vertex AS v | YIELD COUNT(*) AS cnt")
    public void should_fetch_vertex_with_shadow_selection(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
                .count("cnt")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON impact \"p001\"->\"p002\"@0,\"p001\"->\"p002\"@1,\"p001\"->\"p003\"@0 YIELD edge AS e | YIELD COUNT(*) AS cnt")
    public void should_fetch_edge_with_shadow_selection(String expected) {
        assertEquals(expected, edge(Impact.class,
                new NebulaEdgeID<>("p001", "p002", 0L),
                new NebulaEdgeID<>("p001", "p002", 1L),
                new NebulaEdgeID<>("p001", "p003"))
                .count("cnt")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON person \"p001\",\"p002\" YIELD person.name AS name | YIELD COUNT($-.name) AS name_cnt")
    public void should_fetch_vertex_with_shadow_selection_2(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
                .count(Person::getName, "name_cnt")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON impact \"p001\"->\"p002\"@0,\"p001\"->\"p002\"@1,\"p001\"->\"p003\"@0 YIELD rank(edge) AS rank " +
            "| YIELD COUNT($-.rank) AS rank_cnt")
    public void should_fetch_edge_with_shadow_selection_2(String expected) {
        assertEquals(expected, edge(Impact.class,
                new NebulaEdgeID<>("p001", "p002", 0L),
                new NebulaEdgeID<>("p001", "p002", 1L),
                new NebulaEdgeID<>("p001", "p003"))
                .count(Impact::rank, "rank_cnt")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON person \"p001\",\"p002\" YIELD person.name AS name | YIELD COUNT(*) AS cnt,COUNT($-.name) AS name_cnt")
    public void should_fetch_vertex_with_selection_count(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
                .count("cnt")
                .count(Person::getName, "name_cnt")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON impact \"p001\"->\"p002\"@0,\"p001\"->\"p002\"@1,\"p001\"->\"p003\"@0 YIELD rank(edge) AS rank " +
            "| YIELD COUNT(*) AS cnt,COUNT($-.rank) AS rank_cnt")
    public void should_fetch_edge_with_selection_count(String expected) {
        assertEquals(expected, edge(Impact.class,
                new NebulaEdgeID<>("p001", "p002", 0L),
                new NebulaEdgeID<>("p001", "p002", 1L),
                new NebulaEdgeID<>("p001", "p003"))
                .count("cnt")
                .count(Impact::rank, "rank_cnt")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON person \"p001\",\"p002\" YIELD person.age AS age | YIELD SUM($-.age) AS sum_age,SUM($-.age) AS sum_age2")
    public void should_fetch_vertex_with_selection_sum(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
                .sum(Person::getAge, "sum_age")
                .sum("age", "sum_age2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON impact \"p001\"->\"p002\"@0,\"p001\"->\"p002\"@1,\"p001\"->\"p003\"@0 YIELD rank(edge) AS rank " +
            "| YIELD SUM($-.rank) AS sum_rank,SUM($-.rank) AS sum_rank2")
    public void should_fetch_edge_with_selection_sum(String expected) {
        assertEquals(expected, edge(Impact.class,
                new NebulaEdgeID<>("p001", "p002", 0L),
                new NebulaEdgeID<>("p001", "p002", 1L),
                new NebulaEdgeID<>("p001", "p003"))
                .sum(Impact::rank, "sum_rank")
                .sum("rank", "sum_rank2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON person \"p001\",\"p002\" YIELD person.age AS age | YIELD MAX($-.age) AS max_age,MAX($-.age) AS max_age2")
    public void should_fetch_vertex_with_selection_max(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
                .max(Person::getAge, "max_age")
                .max("age", "max_age2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON impact \"p001\"->\"p002\"@0,\"p001\"->\"p002\"@1,\"p001\"->\"p003\"@0 YIELD rank(edge) AS rank " +
            "| YIELD MAX($-.rank) AS max_rank,MAX($-.rank) AS max_rank2")
    public void should_fetch_edge_with_selection_max(String expected) {
        assertEquals(expected, edge(Impact.class,
                new NebulaEdgeID<>("p001", "p002", 0L),
                new NebulaEdgeID<>("p001", "p002", 1L),
                new NebulaEdgeID<>("p001", "p003"))
                .max(Impact::rank, "max_rank")
                .max("rank", "max_rank2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON person \"p001\",\"p002\" YIELD person.age AS age | YIELD MIN($-.age) AS min_age,MIN($-.age) AS min_age2")
    public void should_fetch_vertex_with_selection_min(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
                .min(Person::getAge, "min_age")
                .min("age", "min_age2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON impact \"p001\"->\"p002\"@0,\"p001\"->\"p002\"@1,\"p001\"->\"p003\"@0 YIELD rank(edge) AS rank " +
            "| YIELD MIN($-.rank) AS min_rank,MIN($-.rank) AS min_rank2")
    public void should_fetch_edge_with_selection_min(String expected) {
        assertEquals(expected, edge(Impact.class,
                new NebulaEdgeID<>("p001", "p002", 0L),
                new NebulaEdgeID<>("p001", "p002", 1L),
                new NebulaEdgeID<>("p001", "p003"))
                .min(Impact::rank, "min_rank")
                .min("rank", "min_rank2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON person \"p001\",\"p002\" YIELD person.age AS age | YIELD AVG($-.age) AS avg_age,AVG($-.age) AS avg_age2")
    public void should_fetch_vertex_with_selection_avg(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
                .avg(Person::getAge, "avg_age")
                .avg("age", "avg_age2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON impact \"p001\"->\"p002\"@0,\"p001\"->\"p002\"@1,\"p001\"->\"p003\"@0 YIELD rank(edge) AS rank " +
            "| YIELD AVG($-.rank) AS avg_rank,AVG($-.rank) AS avg_rank2")
    public void should_fetch_edge_with_selection_avg(String expected) {
        assertEquals(expected, edge(Impact.class,
                new NebulaEdgeID<>("p001", "p002", 0L),
                new NebulaEdgeID<>("p001", "p002", 1L),
                new NebulaEdgeID<>("p001", "p003"))
                .avg(Impact::rank, "avg_rank")
                .avg("rank", "avg_rank2")
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON person \"p001\",\"p002\",\"p003\" YIELD person.name AS name | OFFSET 1 LIMIT 2")
    public void should_match_vertex_with_selection_limit(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002", "p003")
                .select(Person::getName)
                .limit(1, 2)
                .render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON impact \"p001\"->\"p002\"@0,\"p001\"->\"p002\"@1,\"p001\"->\"p003\"@0 YIELD impact.type AS type " +
            "| OFFSET 1 LIMIT 2")
    public void should_match_edge_with_selection_limit(String expected) {
        assertEquals(expected, edge(Impact.class,
                new NebulaEdgeID<>("p001", "p002", 0L),
                new NebulaEdgeID<>("p001", "p002", 1L),
                new NebulaEdgeID<>("p001", "p003"))
                .select(Impact::getType)
                .limit(1, 2)
                .render());
    }
}
