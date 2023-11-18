package com.mapledsl.nebula;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.stream.LongStream;
import java.util.stream.Stream;

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
        assertEquals(expected, vertex("person", Stream.of("p001", "p002")).render());
        assertEquals(expected, vertex("person", Stream.<String>builder().add("p001").add("p002").build()).render());
        assertEquals(expected, vertex("person", ImmutableList.of("p001", "p002").stream()).render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON person 1,2,3 YIELD vertex AS v")
    public void should_fetch_vertices_numeric_id_return_itself_automatic(String expected) {
        assertEquals(expected, vertex(Person.class, 1,2,3).render());
        assertEquals(expected, vertex("person", 1,2,3).render());
        assertEquals(expected, vertex("person", LongStream.of(1,2,3)).render());
        assertEquals(expected, vertex("person", LongStream.builder().add(1).add(2).add(3).build()).render());
        assertEquals(expected, vertex("person", LongStream.rangeClosed(1,3)).render());
        assertEquals(expected, vertex("person", ImmutableList.of(1L,2L,3L).stream().mapToLong(Long::intValue)).render());
        assertEquals(expected, vertex("person", ImmutableList.of(1,2,3).stream().mapToLong(Integer::intValue)).render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON impact \"p001\" -> \"p002\"@0 YIELD edge AS e")
    public void should_fetch_edge_return_itself_automatic(String expected) {
        assertEquals(expected, edge(Impact.class, Impact.of("p001", "p002")).render());
        assertEquals(expected, edge("impact", Impact.of("p001", "p002")).render());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON impact \"p001\" -> \"p002\"@0,\"p001\" -> \"p002\"@1,\"p001\" -> \"p003\"@0 YIELD edge AS e")
    public void should_fetch_edges_string_vid_return_itself_automatic(String expected) {
        assertEquals(expected, edge(Impact.class,
                Impact.of("p001", "p002").setRank(0L),
                Impact.of("p001", "p002").setRank(1L),
                Impact.of("p001", "p003"))
                .render()
        );
        assertEquals(expected, edge("impact",
                Impact.of("p001", "p002").setRank(0L),
                Impact.of("p001", "p002").setRank(1L),
                Impact.of("p001", "p003"))
                .render()
        );
        assertEquals(expected, edge("impact",
                Impact.of("p001", "p002").setRank(0L),
                Impact.of("p001", "p002").setRank(1L),
                Impact.of("p001", "p003"))
                .render()
        );
        assertEquals(expected, edge("impact",
                Arrays.asList(
                        Impact.of("p001", "p002").setRank(0L),
                        Impact.of("p001", "p002").setRank(1L),
                        Impact.of("p001", "p003"))
                )
                .render()
        );
        assertEquals(expected, edge("impact",
                ImmutableList.of(
                        Impact.of("p001", "p002").setRank(0L),
                        Impact.of("p001", "p002").setRank(1L),
                        Impact.of("p001", "p003"))
                )
                .render()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON impact 1 -> 2@0,1 -> 2@1,1 -> 3@0 YIELD edge AS e")
    public void should_fetch_edges_numeric_vid_return_itself_automatic(String expected) {
        assertEquals(expected, edge(Impact.class,
                Impact.of(1,2).setRank(0L),
                Impact.of(1,2).setRank(1L),
                Impact.of(1,3))
                .render()
        );
        assertEquals(expected, edge("impact",
                Impact.of(1,2).setRank(0L),
                Impact.of(1,2).setRank(1L),
                Impact.of(1,3))
                .render()
        );
        assertEquals(expected, edge("impact",
                Arrays.asList(
                        Impact.of(1,2).setRank(0L),
                        Impact.of(1,2).setRank(1L),
                        Impact.of(1,3))
                )
                .render()
        );
        assertEquals(expected, edge("impact",
                ImmutableList.of(
                        Impact.of(1,2).setRank(0L),
                        Impact.of(1,2).setRank(1L),
                        Impact.of(1,3))
                )
                .render()
        );
    }


    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON person \"p001\",\"p002\" WHERE person.name == \"bofa\" YIELD vertex AS v")
    public void should_match_vertex_with_predicate(String expected) {
        assertEquals(expected, vertex(Person.class, "p001", "p002")
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
