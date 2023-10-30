package com.mapledsl.nebula;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.annotation.Label;
import com.mapledsl.core.exception.MapleDslExecutionException;
import com.mapledsl.core.model.Model;
import com.mapledsl.nebula.model.NebulaModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.mapledsl.core.G.traverse;
import static com.mapledsl.core.G.vertex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NebulaGraphTraversalTest {
    @BeforeAll
    public static void init() {
        MapleDslConfiguration.primaryConfiguration(MapleDslConfiguration.Builder::templatePrettyPrint)
                .registerBeanDefinition("com.mapledsl.nebula");
    }

    @Test
    public void should_fail_when_missing_direction_clause_value() {
        assertThrows(MapleDslExecutionException.class, () -> traverse("{{ vid }}").render());
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER follow REVERSELY WHERE id($$) IS NOT NULL YIELD id($$) AS dst_id")
    public void should_traverse_use_out_vertex_id_as_default_output(String expected) {
        assertEquals(expected, traverse("{{ vid }}").inE("follow").render());
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
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER * WHERE id($$) IS NOT NULL YIELD id($$) AS dst_id")
    public void should_traverse_all_edge_type(String expected) {
        assertEquals(expected, traverse("{{ vid }}").outE().render());
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER impact REVERSELY WHERE id($$) IS NOT NULL YIELD id($$) AS dst_id")
    public void should_traverse_detailed_relation_type(String expected) {
        assertEquals(expected, traverse("{{ vid }}").inE(Impact.class).render());
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER impact,follow REVERSELY WHERE id($$) IS NOT NULL YIELD id($$) AS dst_id")
    public void should_traverse_detailed_multi_relation_type(String expected) {
        assertEquals(expected, traverse("{{ vid }}").inE(Impact.class, Follow.class).render());
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER impact REVERSELY WHERE id($$) IS NOT NULL YIELD id($$) AS dst_id" +
            "| GO 0 TO 1 STEPS FROM $-.dst_id OVER follow WHERE id($$) IS NOT NULL YIELD id($$) AS dst_id")
    public void should_traverse_multi_step(String expected) {
        assertEquals(expected, traverse("{{ vid }}").inE(Impact.class).outE(Follow.class).render());
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER impact REVERSELY WHERE id($$) IS NOT NULL YIELD id($$) AS dst_id" +
            "| GO 0 TO 1 STEPS FROM $-.dst_id OVER follow WHERE id($$) IS NOT NULL YIELD id($$) AS next_id" +
            "| GO 0 TO 1 STEPS FROM $-.next_id OVER follow REVERSELY WHERE id($$) IS NOT NULL YIELD id($$) AS dst_id")
    public void should_traverse_multi_step_v2(String expected) {
        assertEquals(expected, traverse("{{ vid }}")
                .inE(Impact.class)
                .outE(Follow.class)
                .outV("dst", it -> it.selectAs(Model.ID, "next_id"))
                .inE(Follow.class)
                .render());
    }

    @Test
    public void should_failure_traverse_range_null_edge_types() {
        assertThrows(MapleDslExecutionException.class, () -> traverse("{{ vid }}").inE(1, Impact.class, null, Follow.class));
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER follow REVERSELY " +
            "WHERE $$.person.age < 30 AND ($$.person.name STARTS WITH \"bofa\" OR $$.person.name STARTS WITH \"zhangsan\") OR $$.person.age > 50 AND $$.person.aaaa > 1000 " +
            "YIELD id($$) AS id,$$.person.name AS name,id($$) AS dst_id")
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
                .render()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER follow REVERSELY " +
            "WHERE id($$) IS NOT NULL YIELD id($^) AS prop_id,id($^) AS real_vertex_id,id($$) AS next_traversal_id" +
            "| GO 0 TO 1 STEPS FROM $-.next_traversal_id OVER follow REVERSELY " +
            "WHERE id($$) IS NOT NULL YIELD $-.prop_id AS prop_id,$-.real_vertex_id AS real_vertex_id,id($$) AS dst_id")
    public void should_traverse_then_output_in_vertex_id(String expected) {
        assertEquals(expected, traverse("{{ vid }}")
                .inE(Follow.class)
                .inV("p", it -> it.selectAs("id", "prop_id").selectAs(Model.ID, "real_vertex_id"))
                .outV("p2", it -> it.selectAs(Model.ID, "next_traversal_id"))
                .inE(Follow.class)
                .render()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER follow REVERSELY " +
            "WHERE id($$) IS NOT NULL YIELD properties(edge).type AS follow_type")
    public void should_traverse_then_output_follow_type(String expected) {
        assertEquals(expected, traverse("{{ vid }}")
                .inE(Follow.class)
                .edge("e", it -> it.selectAs("type", "follow_type"))
                .render()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER follow REVERSELY " +
            "WHERE id($$) == \"p001\" YIELD id($$) AS id,$$.person.name AS name,$$.person.name AS dup_person_name,id($$) AS dup_person_id")
    public void should_traverse_then_output_out_vertex_id_and_desc(String expected) {
        assertEquals(expected, traverse("{{ vid }}")
                .inE(Follow.class)
                .outV("p", Person.class, it -> it
                        .eq(Person::id, "p001")
                        .select(Person::id, Person::getName)
                        .selectAs(Person::getName, "dup_person_name")
                        .selectAs(Person::id, "dup_person_id"))
                .render()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER follow REVERSELY WHERE id($$) IS NOT NULL YIELD id($$) AS dst_id")
    public void should_append_dst_model_id_auto(String expected) {
        assertEquals(expected, traverse("{{ vid }}")
                .inE(Follow.class)
                .render()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER follow REVERSELY WHERE id($$) IS NOT NULL YIELD $^.person.name AS p_name")
    public void should_not_append_dst_model_id_auto(String expected) {
        assertEquals(expected, traverse("{{ vid }}")
                .inE(Follow.class)
                .inV("src", Person.class, it -> it.selectAs(Person::getName, "p_name"))
                .render()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER follow REVERSELY WHERE id($$) IS NOT NULL YIELD id($$) AS custom_dst_id")
    public void should_rebase_dst_model_id(String expected) {
        assertEquals(expected, traverse("{{ vid }}")
                .inE(Follow.class)
                .outV("dst", Person.class, it -> it.selectAs(Person::id, "custom_dst_id"))
                .render()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON person WHERE person.name == \"bofa\" YIELD id(vertex) as dst" +
            "| GO 0 TO 1 STEPS FROM $-.dst OVER impact WHERE id($$) IS NOT NULL YIELD id($$) AS dst_id")
    public void should_traverse_via_match(String expected) {
        assertEquals(expected, traverse(vertex(Person.class)
                .eq(Person::getName, "bofa"))
                .outE(Impact.class)
                .render()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"p001\",\"p002\" OVER impact WHERE id($$) IS NOT NULL YIELD id($$) AS p_id,$$.person.name AS p_name,head(labels($$)) AS p_tag " +
            "| ORDER BY $-.p_tag ASC,$-.p_id,$-.p_name DESC")
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
                .render()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"p001\",\"p002\" OVER impact WHERE id($$) IS NOT NULL YIELD $$.person.name AS p_name,$$.person.age AS age " +
            "| YIELD $-.p_name AS p_name,SUM($-.age) AS p_sum_page,COUNT($-.p_name) AS p_cnt_name")
    public void should_traverse_function_with_shadow_selection(String expected) {
        assertEquals(expected, traverse("p001", "p002")
                .outE(Impact.class)
                .outV("p", Person.class, it -> it
                        .selectAs(Person::getName, "p_name")
                        .sum(Person::getAge, "p_sum_page")
                        .count("p_name", "p_cnt_name")
                )
                .render()
        );
    }

    @Label("person")
    public static class Person extends Model.V {
        private String name;
        private Integer age;

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }
    }

    @Label("impact")
    public static class Impact extends NebulaModel.E {
    }

    @Label("follow")
    public static class Follow extends NebulaModel.E {
    }
}
