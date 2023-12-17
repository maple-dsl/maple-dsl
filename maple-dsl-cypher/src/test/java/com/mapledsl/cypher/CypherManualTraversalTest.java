package com.mapledsl.cypher;

import com.mapledsl.core.exception.MapleDslExecutionException;
import com.mapledsl.core.model.Model;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.mapledsl.core.G.traverse;
import static com.mapledsl.core.G.vertex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CypherManualTraversalTest extends CypherManualBaseTest {

    @Test
    public void should_fail_when_missing_direction_clause_value() {
        assertThrows(MapleDslExecutionException.class, () -> traverse("{{ vid }}").render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (dst) - [e:follow*0..1] -> (src) WHERE src.id IN [\"{{ vid }}\"] RETURN dst.id AS dst_id")
    public void should_traverse_use_out_vertex_id_as_default_output(String expected) {
        assertEquals(expected, traverse("{{ vid }}").inE("follow").render(configuration));
    }

    @Test
    public void should_fail_traverse_with_negative_step_from() {
        assertThrows(MapleDslExecutionException.class, () -> traverse("{{ vid }}").inE(-2, 2));
    }

    @Test
    public void should_fail_traverse_with_negative_step_to() {
        assertThrows(MapleDslExecutionException.class, () -> traverse("{{ vid }}").inE(1, -1));
        assertThrows(MapleDslExecutionException.class, () -> traverse("{{ vid }}").inE(-1));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (src) - [e*0..1] -> (dst) WHERE src.id IN [\"{{ vid }}\"] RETURN dst.id AS dst_id")
    public void should_traverse_all_edge_type(String expected) {
        assertEquals(expected, traverse("{{ vid }}").outE().render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (dst) - [e:impact*0..1] -> (src) WHERE src.id IN [\"{{ vid }}\"] RETURN dst.id AS dst_id")
    public void should_traverse_detailed_relation_type(String expected) {
        assertEquals(expected, traverse("{{ vid }}").inE(Impact.class).render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (dst) - [e:impact|follow*0..1] -> (src) WHERE src.id IN [\"{{ vid }}\"] RETURN dst.id AS dst_id")
    public void should_traverse_detailed_multi_relation_type(String expected) {
        assertEquals(expected, traverse("{{ vid }}").inE(Impact.class, Follow.class).render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (dst) - [e:impact*0..1] -> (src) WHERE src.id IN [\"{{ vid }}\"] WITH dst as src" +
            " MATCH (src) - [e:follow*0..1] -> (dst) RETURN dst.id AS dst_id")
    public void should_traverse_multi_step(String expected) {
        assertEquals(expected, traverse("{{ vid }}").inE(Impact.class).outE(Follow.class).render(configuration));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (dst) - [e:impact*0..1] -> (src) WHERE src.id IN [\"{{ vid }}\"] WITH dst as src" +
            " MATCH (src) - [e:follow*0..1] -> (dst_fork) WITH dst_fork as src,dst_fork.id AS next_id" +
            " MATCH (dst) - [e:follow*0..1] -> (src) RETURN next_id,dst.id AS dst_id")
    public void should_traverse_multi_step_v2(String expected) {
        assertEquals(expected, traverse("{{ vid }}")
                .inE(Impact.class)
                .outE(Follow.class)
                .outV("dst_fork", it -> it.selectAs(Model.ID, "next_id"))
                .inE(Follow.class)
                .render(configuration));
    }

    @Test
    public void should_failure_traverse_range_null_edge_types() {
        assertThrows(MapleDslExecutionException.class, () -> traverse("{{ vid }}").inE(1, Impact.class, null, Follow.class));
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (p) - [e:follow*0..1] -> (src) " +
            "WHERE p.age < 30 AND (p.name STARTS WITH \"bofa\" OR p.name STARTS WITH \"zhangsan\") OR p.age > 50 AND p.aaaa > 1000 AND src.id IN [\"{{ vid }}\"] " +
            "RETURN p.id AS id,p.name AS name,p.id AS dst_id")
    public void should_traverse_then_output_out_vertex_id(String expected) {
        assertEquals(expected, traverse("{{ vid }}")
                .inE(Follow.class)
                .outV("p", Person.class, it -> it
                        .lt("age", 30)
                        .and(sub -> sub.startsWith("name", "bofa").or().startsWith("name", "zhangsan"))
                        .or()
                        .gt("age", 50)
                        .gt("aaaa", 1000)
                        .select(Model.ID, "name")
                        .selectAs(Model.ID, "dst_id")
                )
                .render(configuration)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (p2) - [e:follow*0..1] -> (p) WHERE p.id IN [\"{{ vid }}\"] " +
            "WITH p2 as src,p.id AS prop_id,p.id AS real_vertex_id,p2.id AS next_traversal_id " +
            "MATCH (dst) - [e:follow*0..1] -> (src) " +
            "RETURN prop_id,real_vertex_id,next_traversal_id,dst.id AS dst_id")
    public void should_traverse_then_output_in_vertex_id(String expected) {
        assertEquals(expected, traverse("{{ vid }}")
                .inE(Follow.class)
                .inV("p", it -> it.selectAs("id", "prop_id").selectAs(Model.ID, "real_vertex_id"))
                .outV("p2", it -> it.selectAs(Model.ID, "next_traversal_id"))
                .inE(Follow.class)
                .render(configuration)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (dst) - [e:follow*0..1] -> (src) WHERE src.id IN [\"{{ vid }}\"] RETURN e.type AS follow_type")
    public void should_traverse_then_output_follow_type(String expected) {
        assertEquals(expected, traverse("{{ vid }}")
                .inE(Follow.class)
                .edge("e", it -> it.selectAs("type", "follow_type"))
                .render(configuration)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (p) - [e:follow*0..1] -> (src) WHERE p.id = \"p001\" AND src.id IN [\"{{ vid }}\"] " +
            "RETURN p.id AS id,p.name AS name,p.name AS dup_person_name,p.id AS dup_person_id")
    public void should_traverse_then_output_out_vertex_id_and_desc(String expected) {
        assertEquals(expected, traverse("{{ vid }}")
                .inE(Follow.class)
                .outV("p", Person.class, it -> it
                        .eq(Person::id, "p001")
                        .select(Person::id, Person::getName)
                        .selectAs(Person::getName, "dup_person_name")
                        .selectAs(Person::id, "dup_person_id"))
                .render(configuration)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (dst) - [e:follow*0..1] -> (src) WHERE src.id IN [\"{{ vid }}\"] RETURN dst.id AS dst_id")
    public void should_append_dst_model_id_auto(String expected) {
        assertEquals(expected, traverse("{{ vid }}")
                .inE(Follow.class)
                .render(configuration)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (dst) - [e:follow*0..1] -> (src) WHERE src.id IN [\"{{ vid }}\"] RETURN src.name AS p_name")
    public void should_not_append_dst_model_id_auto(String expected) {
        assertEquals(expected, traverse("{{ vid }}")
                .inE(Follow.class)
                .inV("src", Person.class, it -> it.selectAs(Person::getName, "p_name"))
                .render(configuration)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (dst) - [e:follow*0..1] -> (src) WHERE src.id IN [\"{{ vid }}\"] RETURN dst.id AS custom_dst_id")
    public void should_rebase_dst_model_id(String expected) {
        assertEquals(expected, traverse("{{ vid }}")
                .inE(Follow.class)
                .outV("dst", Person.class, it -> it.selectAs(Person::id, "custom_dst_id"))
                .render(configuration)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (v:person) WHERE v.name = \"bofa\" " +
            "WITH v as src " +
            "MATCH (src) - [e:impact*0..1] -> (dst) RETURN dst.id AS dst_id")
    public void should_traverse_via_match(String expected) {
        assertEquals(expected, traverse(vertex(Person.class)
                .eq(Person::getName, "bofa"))
                .outE(Impact.class)
                .render(configuration)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (src) - [e:impact*0..1] -> (p) WHERE src.id IN [\"p001\",\"p002\"] " +
            "RETURN p.id AS p_id,p.name AS p_name,head(labels(p)) AS p_tag " +
            "ORDER BY p_tag ASC,p_name DESC")
    public void should_traverse_ordering(String expected) {
        assertEquals(expected, traverse("p001", "p002")
                .outE(Impact.class)
                .outV("p", Person.class, it -> it
                        .selectAs(Person::id, "p_id")
                        .selectAs(Person::getName, "p_name")
                        .descending()
                        .selectAs(Person::label, "p_tag")
                        .ascending()
                )
                .render(configuration)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (src) - [e:impact*0..1] -> (p) WHERE src.id IN [\"p001\",\"p002\"] " +
            "RETURN p.name AS p_name,SUM(p.age) AS p_sum_page,COUNT(p.name) AS p_cnt_name")
    public void should_traverse_function_with_shadow_selection(String expected) {
        assertEquals(expected, traverse("p001", "p002")
                .outE(Impact.class)
                .outV("p", Person.class, it -> it
                        .selectAs(Person::getName, "p_name")
                        .sum(Person::getAge, "p_sum_page")
                        .count(Person::getName, "p_cnt_name")
                )
                .render(configuration)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (src) - [e:impact*0..1] -> (p) WHERE src.id IN [\"p001\",\"p002\"] " +
            "RETURN SUM(p.age) AS p_sum_page,COUNT(p.name) AS p_cnt_name")
    public void should_traverse_function_with_shadow_selection_2(String expected) {
        assertEquals(expected, traverse("p001", "p002")
                .outE(Impact.class)
                .outV("p", Person.class, it -> it
                        .sum(Person::getAge, "p_sum_page")
                        .count(Person::getName, "p_cnt_name")
                )
                .render(configuration)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "MATCH (src) - [e:impact*0..1] -> (p) WHERE src.id IN [\"p001\",\"p002\"] " +
            "WITH p as src,p.id AS companion_id " +
            "MATCH (src) - [e:impact*0..1] -> (p2) RETURN companion_id,p2.id AS id")
    public void should_traverse_with_companion(String expected) {
        assertEquals(expected, traverse("p001", "p002")
                .outE(Impact.class)
                .outV("p", Person.class, it -> it.selectAs(Person::id, "companion_id"))
                .outE(Impact.class)
                .outV("p2", Person.class, it -> it.select(Person::id))
                .render(configuration)
        );
    }
}
