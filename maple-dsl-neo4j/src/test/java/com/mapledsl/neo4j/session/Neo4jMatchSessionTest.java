package com.mapledsl.neo4j.session;

import com.mapledsl.core.model.Model;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.mapledsl.core.G.edge;
import static com.mapledsl.core.G.vertex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Prepare stage:
 * 1. Using Docker Compose can quickly deploy NebulaGraph services based on the prepared configuration file.
 * 2. cd `maple-dsl-nebula`/build
 * 3. Run `docker-compose up -d` then wait the nebula all services is already.
 * <p></p>
 * This class contains test cases for the NebulaGraphFetchSession.
 * It tests various scenarios of fetching vertices and edges from the graph using selection, filtering, ordering, and aggregation.
 */
public class Neo4jMatchSessionTest extends Neo4jSessionBaseTest {

    @Test
    public void should_match_vertex_return_itself_automatic() {
        final List<Person> personList = sessionTemplate.selectList(vertex(Person.class), Person.class);
        assertNotNull(personList);
        assertEquals(3, personList.size());

        final List<Model.V<Object>> vertexList = sessionTemplate.selectVertexList(vertex(Person.class));
        assertNotNull(vertexList);
        assertEquals(3, vertexList.size());
    }

    @Test
    public void should_match_edge_return_itself_automatic() {
        final List<Impact> impactList = sessionTemplate.selectList(edge(Impact.class), Impact.class);
        assertNotNull(impactList);
        assertEquals(2, impactList.size());
        assertEquals("e001", impactList.get(0).id());
        assertEquals("p001", impactList.get(0).src());
        assertEquals("p002", impactList.get(0).dst());
        assertEquals("e002", impactList.get(1).id());
        assertEquals("p001", impactList.get(1).src());
        assertEquals("p002", impactList.get(1).dst());

        final List<Model.E<Object>> edgeList = sessionTemplate.selectEdgeList(edge(Impact.class));
        assertNotNull(edgeList);
        assertEquals(2, edgeList.size());
        assertEquals("e001", impactList.get(0).id());
        assertEquals("p001", edgeList.get(0).src());
        assertEquals("p002", edgeList.get(0).dst());
        assertEquals("e002", impactList.get(1).id());
        assertEquals("p001", edgeList.get(1).src());
        assertEquals("p002", edgeList.get(1).dst());
    }

    @Test
    public void should_match_vertex_with_predicate() {
        final Person person = sessionTemplate.selectOne(vertex(Person.class).eq(Person::getName, "bofa"), Person.class);
        assertNotNull(person);
        assertEquals("p001", person.id());
        assertEquals("bofa", person.getName());

        final Model.V<Object> vertex = sessionTemplate.selectVertex(vertex(Person.class).eq(Person::getName, "bofa"));
        assertNotNull(vertex);
        assertEquals("p001", vertex.id());
        assertEquals("bofa", vertex.get("name"));

        final List<Model.V<Object>> vertexList = sessionTemplate.selectVertexList(vertex(Person.class).eq(Person::getName, "bofa"));
        assertNotNull(vertexList);
        assertEquals(1, vertexList.size());
        assertEquals("p001", vertexList.get(0).id());
        assertEquals("bofa", vertexList.get(0).get("name"));

        final Model.E<Object> edge = sessionTemplate.selectEdge(vertex(Person.class).eq(Person::getName, "bofa").render());
        assertNotNull(edge);
        assertEquals("bofa", edge.<Map<String, Object>>get("v").get("name"));

        final List<Model.E<Object>> edgeList = sessionTemplate.selectEdgeList(vertex(Person.class).eq(Person::getName, "bofa").render());
        assertNotNull(edgeList);
        assertEquals(1, edgeList.size());
        assertEquals("bofa", edgeList.get(0).<Map<String, Object>>get("v").get("name"));
    }

    @Test
    public void should_match_edge_with_predicate() {
        final Impact impact = sessionTemplate.selectOne(edge(Impact.class)
                .in(Impact::getType, Arrays.asList("type1", "type2")), Impact.class);

        assertNotNull(impact);
        assertEquals("p001", impact.src());
        assertEquals("type1", impact.getType());

        final List<Impact> impactList = sessionTemplate.selectList(edge(Impact.class)
                .in(Impact::getType, Arrays.asList("type1", "type2")), Impact.class);

        assertNotNull(impactList);
        assertEquals(2, impactList.size());
        assertEquals("p001", impactList.get(0).src());
        assertEquals("type1", impactList.get(0).getType());

        final Model.E<Object> edge = sessionTemplate.selectEdge(edge(Impact.class)
                .in(Impact::getType, Arrays.asList("type1", "type2")));

        assertNotNull(edge);
        assertEquals("p001", edge.src());
        assertEquals("type1", edge.get("type"));

        final List<Model.E<Object>> edgeList = sessionTemplate.selectEdgeList(edge(Impact.class)
                .in(Impact::getType, Arrays.asList("type1", "type2")));

        assertNotNull(edgeList);
        assertEquals(2, edgeList.size());
        assertEquals("p001", edgeList.get(0).src());
        assertEquals("type1", edgeList.get(0).get("type"));
    }

    @Test
    public void should_match_vertex_with_selection_complicit_alias() {
        final String personName = sessionTemplate.selectOne(vertex(Person.class)
                .eq(Person::getName, "bofa")
                .selectAs(Person::getName, "v"), String.class);

        assertNotNull(personName);
        assertEquals("bofa", personName);
    }

    @Test
    public void should_match_edge_with_selection_complicit_alias() {
        final Model.E<Object> edge = sessionTemplate.selectEdge(edge(Impact.class)
                .in(Impact::getType, Arrays.asList("type1", "type2"))
                .selectAs(Impact::getType, "impact_type")
                .select(Impact::src, Impact::dst, Impact::id));

        assertNotNull(edge);
        assertEquals("type1", edge.get("impact_type"));
        assertEquals("p001", edge.src());
        assertEquals("p002", edge.dst());

        final List<Model.E<Object>> edgeList = sessionTemplate.selectEdgeList(edge(Impact.class)
                .in(Impact::getType, Arrays.asList("type1", "type2"))
                .selectAs(Impact::getType, "impact_type")
                .select(Impact::src, Impact::dst, Impact::id));

        assertNotNull(edgeList);
        assertEquals(2, edgeList.size());
        assertEquals("type1", edgeList.get(0).get("impact_type"));
        assertEquals("p001", edgeList.get(0).src());
        assertEquals("p002", edgeList.get(0).dst());
        assertEquals("type2", edgeList.get(1).get("impact_type"));
        assertEquals("p001", edgeList.get(1).src());
        assertEquals("p002", edgeList.get(1).dst());

        final List<Impact> impactList = sessionTemplate.selectList(edge(Impact.class)
                .in(Impact::getType, Arrays.asList("type1", "type2"))
                .selectAs(Impact::getType, "impact_type")
                .select(Impact::src, Impact::dst, Impact::id), Impact.class);

        assertNotNull(impactList);
        assertEquals(2, impactList.size());
        assertEquals("type1", impactList.get(0).get("impact_type"));
        assertEquals("p001", impactList.get(0).src());
        assertEquals("p002", impactList.get(0).dst());
        assertEquals("type2", impactList.get(1).get("impact_type"));
        assertEquals("p001", impactList.get(1).src());
        assertEquals("p002", impactList.get(1).dst());
    }

    @ParameterizedTest
    @ValueSource(strings = "LOOKUP ON impact WHERE impact.type IN [\"Type1\",\"Type2\"] " +
            "YIELD impact.type AS impact_type,src(edge) AS src,dst(edge) AS dst,rank(edge) AS rank " +
            "| ORDER BY $-.impact_type ASC,$-.src,$-.dst,$-.rank DESC")
    public void should_match_edge_with_selection_ordering(String expected) {
        final Model.E<Object> edge = sessionTemplate.selectEdge(edge(Impact.class)
                .in(Impact::getType, Arrays.asList("type1", "type2"))
                .selectAs(Impact::getType, "impact_type")
                .select(Impact::src, Impact::dst, Impact::id).descending());

        assertNotNull(edge);
        assertEquals("type2", edge.get("impact_type"));
        assertEquals("p001", edge.src());
        assertEquals("p002", edge.dst());

        final List<Model.E<Object>> edgeList = sessionTemplate.selectEdgeList(edge(Impact.class)
                .in(Impact::getType, Arrays.asList("type1", "type2"))
                .selectAs(Impact::getType, "impact_type")
                .select(Impact::src, Impact::dst, Impact::id).descending());

        assertNotNull(edgeList);
        assertEquals(2, edgeList.size());
        assertEquals("type2", edgeList.get(0).get("impact_type"));
        assertEquals("p001", edgeList.get(0).src());
        assertEquals("p002", edgeList.get(0).dst());
        assertEquals("type1", edgeList.get(1).get("impact_type"));
        assertEquals("p001", edgeList.get(1).src());
        assertEquals("p002", edgeList.get(1).dst());

        final List<Impact> impactList = sessionTemplate.selectList(edge(Impact.class)
                .in(Impact::getType, Arrays.asList("type1", "type2"))
                .selectAs(Impact::getType, "impact_type")
                .select(Impact::src, Impact::dst, Impact::id).descending(), Impact.class);

        assertNotNull(impactList);
        assertEquals(2, impactList.size());
        assertEquals("type2", impactList.get(0).get("impact_type"));
        assertEquals("p001", impactList.get(0).src());
        assertEquals("p002", impactList.get(0).dst());
        assertEquals("type1", impactList.get(1).get("impact_type"));
        assertEquals("p001", impactList.get(1).src());
        assertEquals("p002", impactList.get(1).dst());
    }

    @Test
    public void should_match_vertex_with_shadow_selection() {
        final Long cnt = sessionTemplate.selectOne(vertex(Person.class).count("cnt"), Long.class);
        assertNotNull(cnt);
        assertEquals(3, cnt);

        final List<Long> resultList = sessionTemplate.selectList(vertex(Person.class).count("cnt"), Long.class);
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(3, resultList.get(0));
    }

    @Test
    public void should_match_edge_with_shadow_selection() {
        final Long cnt = sessionTemplate.selectOne(edge(Impact.class).count("cnt"), Long.class);
        assertNotNull(cnt);
        assertEquals(2, cnt);

        final List<Long> resultList = sessionTemplate.selectList(edge(Impact.class).count("cnt"), Long.class);
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(2, resultList.get(0));
    }

    @Test
    public void should_match_vertex_with_shadow_selection_2() {
        final Long cnt = sessionTemplate.selectOne(vertex(Person.class).count(Person::getName, "cnt"), Long.class);
        assertNotNull(cnt);
        assertEquals(3, cnt);

        final List<Long> resultList = sessionTemplate.selectList(vertex(Person.class).count(Person::getName, "cnt"), Long.class);
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(3, resultList.get(0));
    }

    @Test
    public void should_match_edge_with_shadow_selection_2() {
        final Long cnt = sessionTemplate.selectOne(edge(Impact.class).count(Impact::getType, "cnt"), Long.class);
        assertNotNull(cnt);
        assertEquals(2, cnt);

        final List<Long> resultList = sessionTemplate.selectList(edge(Impact.class).count(Impact::getType, "cnt"), Long.class);
        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(2, resultList.get(0));
    }

    @Test
    public void should_match_vertex_with_selection_count() {
        final Long cnt = sessionTemplate.selectOne(vertex(Person.class)
                .count("cnt")
                .count(Person::getName, "name_cnt"), Long.class);

        assertNotNull(cnt);
        assertEquals(3L, cnt);

        final Map<String, Object> resultMap = sessionTemplate.selectMap(vertex(Person.class)
                .count("cnt")
                .count(Person::getName, "name_cnt"));

        assertNotNull(resultMap);
        assertEquals(3L, resultMap.get("cnt"));
        assertEquals(3L, resultMap.get("name_cnt"));

        final List<Long> resultList = sessionTemplate.selectList(vertex(Person.class)
                .count("cnt")
                .count(Person::getName, "name_cnt"), Long.class);

        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals(3L, resultList.get(0));
        assertEquals(3L, resultList.get(1));
    }

    @Test
    public void should_match_edge_with_selection_count() {
        final Long cnt = sessionTemplate.selectOne(edge(Impact.class)
                .count("cnt")
                .count(Impact::getType, "type_cnt"), Long.class);

        assertNotNull(cnt);
        assertEquals(2L, cnt);

        final Map<String, Object> resultMap = sessionTemplate.selectMap(edge(Impact.class)
                .count("cnt")
                .count(Impact::getType, "type_cnt"));

        assertNotNull(resultMap);
        assertEquals(2L, resultMap.get("cnt"));
        assertEquals(2L, resultMap.get("type_cnt"));

        final List<Long> resultList = sessionTemplate.selectList(edge(Impact.class)
                .count("cnt")
                .count(Impact::getType, "type_cnt"), Long.class);

        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals(2L, resultList.get(0));
        assertEquals(2L, resultList.get(1));
    }

    @Test
    public void should_match_vertex_with_selection_limit() {
        final Person person = sessionTemplate.selectOne(vertex(Person.class)
                .limit(1, 2), Person.class);
        assertNotNull(person);

        final Model.V<Object> vertex = sessionTemplate.selectVertex(vertex(Person.class)
                .limit(1, 2));
        assertNotNull(vertex);

        final List<Person> personList = sessionTemplate.selectList(vertex(Person.class)
                .limit(1, 2), Person.class);
        assertNotNull(personList);

        final List<Model.V<Object>> vertexList = sessionTemplate.selectVertexList(vertex(Person.class)
                .limit(1, 2));
        assertNotNull(vertexList);
    }

    @Test
    public void should_match_edge_with_selection_limit() {
        final Impact impact = sessionTemplate.selectOne(edge(Impact.class)
                .limit(1, 2), Impact.class);

        assertNotNull(impact);
        assertEquals("p001", impact.src());
        assertEquals("p002", impact.dst());
        assertEquals("type2", impact.getType());

        final Model.E<Object> edge = sessionTemplate.selectEdge(edge(Impact.class)
                .limit(1, 2));

        assertNotNull(edge);
        assertEquals("p001", edge.src());
        assertEquals("p002", edge.dst());
        assertEquals("type2", edge.get("type"));

        final List<Impact> impactList = sessionTemplate.selectList(edge(Impact.class)
                .limit(1, 2), Impact.class);

        assertNotNull(impactList);
        assertEquals("p001", impactList.get(0).src());
        assertEquals("p002", impactList.get(0).dst());
        assertEquals("type2", impactList.get(0).getType());

        final List<Model.E<Object>> edgeList = sessionTemplate.selectEdgeList(edge(Impact.class)
                .limit(1, 2));

        assertNotNull(edgeList);
        assertEquals("p001", edgeList.get(0).src());
        assertEquals("p002", edgeList.get(0).dst());
        assertEquals("type2", edgeList.get(0).get("type"));
    }
}