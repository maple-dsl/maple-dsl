package com.mapledsl.nebula;

import com.mapledsl.core.MapleDslConfiguration;
import com.mapledsl.core.annotation.Label;
import com.mapledsl.core.exception.MapleDslExecutionException;
import com.mapledsl.core.extension.KeyPolicyStrategies;
import com.mapledsl.core.extension.NamingStrategies;
import com.mapledsl.core.model.Model;
import com.mapledsl.nebula.model.NebulaModel;
import com.mapledsl.nebula.module.MapleNebulaDslModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

import static com.mapledsl.core.G.traverse;
import static com.mapledsl.core.G.vertex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NebulaGraphTraversalTest {
    static String formatSql(String origin) {
        return origin.trim().replaceAll("\\s{2,}", " ");
    }

    @BeforeAll
    public static void init() {
        new MapleDslConfiguration.Builder()
                .module(MapleNebulaDslModule.class)
                .templatePoolConfig(200, Runtime.getRuntime().availableProcessors() * 2, 2)
                .namingStrategy(NamingStrategies.SNAKE_CASE)
                .keyPolicyStrategy(KeyPolicyStrategies.MANUAL)
                .dateFormatter("yyyy-MM-dd")
                .dateTimeFormatter("yyyy-MM-dd HH:mm:ss")
                .timeFormatter("HH:mm:ss")
                .locale(Locale.ENGLISH)
                .zoneId(ZoneId.systemDefault())
                .timeZone(TimeZone.getDefault())
                .build();
    }

    @Test
    public void should_fail_when_missing_direction_clause_value() {
        assertThrows(MapleDslExecutionException.class, () -> traverse("{{ vid }}").render());
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER follow REVERSELY WHERE 1==1 AND id($$) IS NOT NULL YIELD id($$) AS _dst")
    public void should_traverse_use_out_vertex_id_as_default_output(String expected) {
        assertEquals(
                expected,
                formatSql(traverse("{{ vid }}").inE("follow").render())
        );
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
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER * WHERE 1==1 AND id($$) IS NOT NULL YIELD id($$) AS _dst")
    public void should_traverse_all_edge_type(String expected) {
        assertEquals(
                expected,
                formatSql(traverse("{{ vid }}").outE().render())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER impact REVERSELY WHERE 1==1 AND id($$) IS NOT NULL YIELD id($$) AS _dst")
    public void should_traverse_detailed_relation_type(String expected) {
        assertEquals(
                expected,
                formatSql(traverse("{{ vid }}").inE(Impact.class).render())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER impact,follow REVERSELY WHERE 1==1 AND id($$) IS NOT NULL YIELD id($$) AS _dst")
    public void should_traverse_detailed_multi_relation_type(String expected) {
        assertEquals(
                expected,
                formatSql(traverse("{{ vid }}").inE(Impact.class, Follow.class).render())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER impact REVERSELY WHERE 1==1 AND id($$) IS NOT NULL YIELD id($$) AS _dst" +
            " | GO 0 TO 1 STEPS FROM $-._dst OVER follow WHERE 1==1 AND id($$) IS NOT NULL YIELD id($$) AS _dst")
    public void should_traverse_multi_step(String expected) {
        assertEquals(
                expected,
                formatSql(traverse("{{ vid }}").inE(Impact.class).outE(Follow.class).render())
        );
    }

    @Test
    public void should_failure_traverse_range_null_edge_types() {
        assertThrows(MapleDslExecutionException.class, () -> traverse("{{ vid }}").inE(1, Impact.class, null, Follow.class));
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER follow REVERSELY " +
            "WHERE $$.person.age < 30 AND ($$.person.name STARTS WITH \"bofa\" OR $$.person.name STARTS WITH \"zhangsan\") OR $$.person.age != 50 AND $$.person.aaaa != 1000 " +
            "YIELD $$.person.id AS id,$$.person.name AS name,$$.person.id AS dst_id")
    public void should_traverse_then_output_out_vertex_id(String expected) {
        assertEquals(
                expected,
                formatSql(traverse("{{ vid }}")
                        .inE(Follow.class)
                        .outV("p", Person.class, it -> it
                                .lt("age", 30)
                                .and(sub -> sub.startsWith("name", "bofa").or().startsWith("name", "zhangsan"))
                                .or()
                                .gt("age", 50)
                                .gt("aaaa", 1000)
                                .select("id", "name")
                                .selectAs("id", "dst_id")
                        )
                        .render())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER follow REVERSELY " +
            "WHERE 1==1 AND id($$) IS NOT NULL YIELD properties($^).id AS prop_id,id($^) AS real_vertex_id,id($$) AS next_traversal_id " +
            "| GO 0 TO 1 STEPS FROM $-.next_traversal_id OVER follow REVERSELY " +
            "WHERE 1==1 AND id($$) IS NOT NULL YIELD id($$) AS _dst,$-.prop_id,$-.real_vertex_id")
    public void should_traverse_then_output_in_vertex_id(String expected) {
        assertEquals(
                expected,
                formatSql(traverse("{{ vid }}")
                        .inE(Follow.class)
                        .inV("p", it -> it.selectAs("id", "prop_id").selectAs(Model.ID, "real_vertex_id"))
                        .outV("p2", it -> it.selectAs(Model.ID, "next_traversal_id"))
                        .inE(Follow.class)
                        .render())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER follow REVERSELY " +
            "WHERE 1==1 AND id($$) IS NOT NULL YIELD properties(edge).type AS follow_type,id($$) AS _dst")
    public void should_traverse_then_output_follow_type(String expected) {
        assertEquals(
                expected,
                formatSql(traverse("{{ vid }}")
                        .inE(Follow.class)
                        .edge("e", it -> it.selectAs("type", "follow_type"))
                        .render())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "GO 0 TO 1 STEPS FROM \"{{ vid }}\" OVER follow REVERSELY " +
            "WHERE $$.person.id == \"p001\" YIELD $$.person.id AS id,$$.person.name AS name,$$.person.name AS dup_person_name,$$.person.id AS dup_person_id")
    public void should_traverse_then_output_out_vertex_id_and_desc(String expected) {
        assertEquals(
                expected,
                formatSql(traverse("{{ vid }}")
                        .inE(Follow.class)
                        .outV("p", Person.class, it -> it
                                .eq(Person::id, "p001")
                                .select(Person::id, Person::getName)
                                .selectAs(Person::getName, "dup_person_name")
                                .selectAs(Person::id, "dup_person_id"))
                        .render())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON person WHERE person.name == \"bofa\" YIELD id(vertex) as dst " +
            "| GO 0 TO 1 STEPS FROM $-.dst OVER impact WHERE 1==1 AND id($$) IS NOT NULL YIELD id($$) AS _dst")
    public void should_traverse_via_match(String expected) {
        assertEquals(
                expected,
                formatSql(traverse(vertex(Person.class)
                        .eq(Person::getName, "bofa"))
                        .outE(Impact.class)
                        .render())
        );
    }

    @Label("person")
    static class Person extends Model.V {
        private String name;

        public String getName() {
            return name;
        }
    }

    @Label("impact")
    static class Impact extends NebulaModel.E {
    }

    @Label("follow")
    static class Follow extends NebulaModel.E {
    }
}
