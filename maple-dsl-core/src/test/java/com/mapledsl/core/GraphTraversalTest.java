package com.mapledsl.core;

import com.mapledsl.core.annotation.Label;
import com.mapledsl.core.exception.MapleDslExecutionException;
import com.mapledsl.core.extension.KeyPolicyStrategies;
import com.mapledsl.core.extension.NamingStrategies;
import com.mapledsl.core.model.Model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

import static com.mapledsl.core.G.traverse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphTraversalTest {

    @BeforeAll
    public static void init() {
        new MapleDslConfiguration.Builder()
                .module(MapleDslMockModule.class)
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

    @Test
    public void should_traverse_use_out_vertex_id_as_default_output() {
        assertTrue(traverse("{{ vid }}").inE("follow").render().contains("SELECT OUT.id AS dst_id"));
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

    @Test
    public void should_traverse_all_edge_type() {
        assertTrue(traverse("{{ vid }}").outE().render().contains("OVER *"));
    }

    @Test
    public void should_traverse_detailed_relation_type() {
        assertTrue(traverse("{{ vid }}").inE(Impact.class).render().contains("OVER impact"));
    }

    @Test
    public void should_traverse_detailed_multi_relation_type() {
        assertTrue(traverse("{{ vid }}").inE(Impact.class, Follow.class).render().contains("OVER impact,follow"));
    }

    @Test
    public void should_traverse_multi_step() {
        String sql = G.traverse("{{ vid }}").inE(Impact.class).outE(Follow.class).render();
        assertTrue(sql.contains("OVER impact"));
        assertTrue(sql.contains("OVER follow"));
    }

    @Test
    public void should_failure_traverse_range_null_edge_types() {
        assertThrows(MapleDslExecutionException.class, () -> traverse("{{ vid }}").inE(1, Impact.class, null, Follow.class));
    }

    @Test
    public void should_traverse_then_output_out_vertex_id() {
        String sql = traverse("{{ vid }}")
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
                .render();
        assertTrue(sql.contains(MapleDslDialectMockSelectionRender.render(
                MapleDslDialectRenderConstants.OUT_VERTEX, "person",
                new String[]{"id"},
                new String[]{"dst_id"}
        )));
    }

    @Test
    public void should_traverse_then_output_in_vertex_id() {
        String sql = traverse("{{ vid }}").inE(Follow.class)
                .inV("p", it -> it.selectAs("id", "dst_id"))
                .render();
        assertTrue(sql.contains(MapleDslDialectMockSelectionRender.render(
                MapleDslDialectRenderConstants.IN_VERTEX, null,
                new String[]{"id"},
                new String[]{"dst_id"}
        )));
    }

    @Test
    public void should_traverse_then_output_follow_type() {
        String sql = traverse("{{ vid }}").inE(Follow.class)
                .edge("e", it -> it.selectAs("type", "follow_type"))
                .render();
        assertTrue(sql.contains(MapleDslDialectMockSelectionRender.render(
                MapleDslDialectRenderConstants.EDGE, null,
                new String[]{"type"},
                new String[]{"follow_type"}
        )));
    }


    @Test
    public void should_traverse_then_output_out_vertex_id_and_desc() {
        String sql = traverse("{{ vid }}")
                .inE(Follow.class)
                .outV("p", Person.class, it -> it
                        .eq(Person::id, "p001")
                        .select(Person::id, Person::getName)
                        .selectAs(Person::getName, "dup_person_name")
                        .selectAs(Person::id, "dup_person_id"))
                .render();
        assertTrue(sql.contains(MapleDslDialectMockSelectionRender.render(
                MapleDslDialectRenderConstants.OUT_VERTEX, "person",
                new String[]{"id"},
                new String[]{"dup_person_id"}
        )));
    }

    @Label("person")
    static class Person extends Model.V<String> {
        private String name;

        public String getName() {
            return name;
        }

        public Person setName(String name) {
            this.name = name;
            return this;
        }
    }

    @Label("impact")
    static class Impact extends Model.E<String> {
    }

    @Label("follow")
    static class Follow extends Model.E<String> {
    }
}
