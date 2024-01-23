package com.mapledsl.neo4j.session;

import com.google.common.collect.ImmutableList;
import com.mapledsl.core.model.Model;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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
public class Neo4jFetchSessionTest extends Neo4jSessionBaseTest {

    @Test
    public void should_fetch_vertex_return_itself_automatic() {
        final Model.V<String> vertex = sessionTemplate.selectVertex(vertex(Person.class, "p001"));
        assertNotNull(vertex);
        assertEquals("p001", vertex.id());
        assertEquals("person", vertex.label());
        assertEquals("bofa", vertex.get("name"));

        final Person person = sessionTemplate.selectOne(vertex(Person.class, "p001"), Person.class);
        assertNotNull(person);
        assertEquals("p001", person.id());
        assertEquals("person", person.label());
        assertEquals("bofa", person.getName());
    }

    @Test
    public void should_fetch_vertices_text_id_return_itself_automatic() {
        final Model.V<String> vertex = sessionTemplate.selectVertex(vertex(Person.class, "p001", "p002"));
        assertNotNull(vertex);
        assertEquals("p001", vertex.id());
        assertEquals("person", vertex.label());
        assertEquals("bofa", vertex.get("name"));

        final List<Model.V<String>> vertices = sessionTemplate.selectVertexList(vertex(Person.class, "p001", "p002"));
        assertNotNull(vertices);
        assertEquals(2, vertices.size());

        assertNotNull(vertices.get(0));
        assertEquals("p001", vertices.get(0).id());
        assertEquals("person", vertices.get(0).label());
        assertEquals("bofa", vertices.get(0).get("name"));

        assertNotNull(vertices.get(1));
        assertEquals("p002", vertices.get(1).id());
        assertEquals("person", vertices.get(1).label());
        assertEquals("zhangsan", vertices.get(1).get("name"));

        final Person person = sessionTemplate.selectOne(vertex(Person.class, "p001", "p002"), Person.class);
        assertNotNull(person);
        assertEquals("p001", person.id());
        assertEquals("person", person.label());
        assertEquals("bofa", person.getName());

        final List<Person> personList = sessionTemplate.selectList(vertex("person", "p001", "p002"), Person.class);
        assertNotNull(personList);
        assertEquals(2, personList.size());

        assertNotNull(personList.get(0));
        assertEquals("p001", personList.get(0).id());
        assertEquals("person", personList.get(0).label());
        assertEquals("bofa", personList.get(0).getName());

        assertNotNull(personList.get(1));
        assertEquals("p002", personList.get(1).id());
        assertEquals("person", personList.get(1).label());
        assertEquals("zhangsan", personList.get(1).getName());
    }

    @ParameterizedTest
    @ValueSource(strings = "FETCH PROP ON person 1,2,3 YIELD vertex AS v")
    public void should_fetch_vertices_numeric_id_return_itself_automatic(String expected) {
        final Model.V<Long> vertex = hashSessionTemplate.selectVertex(vertex(PersonHash.class, 1L, 2L));
        assertNotNull(vertex);
        assertEquals(1, vertex.id());
        assertEquals("person", vertex.label());
        assertEquals("bofa", vertex.get("name"));
    }

    @Test
    public void should_fetch_edge_return_itself_automatic() {
        final Model.E<String, String> edge = sessionTemplate.selectEdge(edge("impact", "e001"));
        assertNotNull(edge);
        assertEquals("e001", edge.id());
        assertEquals("p001", edge.src());
        assertEquals("p002", edge.dst());
        assertEquals("impact", edge.label());
        assertEquals("type1", edge.get("type"));

        final Impact impact = sessionTemplate.selectOne(edge("impact", "e001"), Impact.class);
        assertNotNull(impact);
        assertEquals("e001", edge.id());
        assertEquals("p001", impact.src());
        assertEquals("p002", impact.dst());
        assertEquals("impact", impact.label());
        assertEquals("type1", impact.getType());
    }

    @Test
    public void should_fetch_edges_string_vid_return_itself_automatic() {
        final Model.E<String, String> edge = sessionTemplate.selectEdge(edge(Impact.class, "e001","e002"));

        assertNotNull(edge);
        assertEquals("e001", edge.id());
        assertEquals("p001", edge.src());
        assertEquals("p002", edge.dst());
        assertEquals("impact", edge.label());
        assertEquals("type1", edge.get("type"));

        final Impact impact = sessionTemplate.selectOne(edge(Impact.class, "e001", "e002"), Impact.class);
        assertNotNull(impact);
        assertEquals("e001", edge.id());
        assertEquals("p001", impact.src());
        assertEquals("p002", impact.dst());
        assertEquals("impact", impact.label());
        assertEquals("type1", impact.getType());

        final List<Model.E<String, String>> edgeList = sessionTemplate.selectEdgeList(edge(Impact.class, "e001", "e002"));

        assertNotNull(edgeList);
        assertEquals(2, edgeList.size());

        assertNotNull(edgeList.get(0));
        assertEquals("p001", edgeList.get(0).src());
        assertEquals("p002", edgeList.get(0).dst());
        assertEquals("impact", edgeList.get(0).label());
        assertEquals("type1", edgeList.get(0).get("type"));

        assertNotNull(edgeList.get(1));
        assertEquals("p001", edgeList.get(1).src());
        assertEquals("p002", edgeList.get(1).dst());
        assertEquals("impact", edgeList.get(1).label());
        assertEquals("type2", edgeList.get(1).get("type"));

        final List<Impact> impactList = sessionTemplate.selectList(edge(Impact.class, "e001", "e002"), Impact.class);

        assertNotNull(impactList);
        assertEquals(2, impactList.size());

        assertNotNull(impactList.get(0));
        assertEquals("p001", impactList.get(0).src());
        assertEquals("p002", impactList.get(0).dst());
        assertEquals("impact", impactList.get(0).label());
        assertEquals("type1", impactList.get(0).getType());

        assertNotNull(impactList.get(1));
        assertEquals("p001", impactList.get(1).src());
        assertEquals("p002", impactList.get(1).dst());
        assertEquals("impact", impactList.get(1).label());
        assertEquals("type2", impactList.get(1).getType());
    }

    @Test
    public void should_fetch_edges_numeric_vid_return_itself_automatic() {
        final Model.E<Long, Long> edge = hashSessionTemplate.selectEdge(edge(ImpactHash.class, 1L, 2L));

        assertNotNull(edge);
        assertEquals(1, edge.id());
        assertEquals(1, edge.src());
        assertEquals(2, edge.dst());
        assertEquals("impact", edge.label());
        assertEquals("type1", edge.get("type"));

        final ImpactHash impact = hashSessionTemplate.selectOne(edge(ImpactHash.class, ImmutableList.of(1L, 2L)), ImpactHash.class);

        assertNotNull(impact);
        assertEquals(1, impact.id());
        assertEquals(1, impact.src());
        assertEquals(2, impact.dst());
        assertEquals("impact", impact.label());
        assertEquals("type1", impact.getType());

        final List<Model.E<Long, Long>> edgeList = hashSessionTemplate.selectEdgeList(edge("impact", 1L, 2L));

        assertNotNull(edgeList);
        assertEquals(2, edgeList.size());

        assertNotNull(edgeList.get(0));
        assertEquals(1, edgeList.get(0).src());
        assertEquals(2, edgeList.get(0).dst());
        assertEquals("impact", edgeList.get(0).label());
        assertEquals("type1", edgeList.get(0).get("type"));

        assertNotNull(edgeList.get(1));
        assertEquals(1, edgeList.get(1).src());
        assertEquals(2, edgeList.get(1).dst());
        assertEquals("impact", edgeList.get(1).label());
        assertEquals("type2", edgeList.get(1).get("type"));

        final List<ImpactHash> impactList = hashSessionTemplate.selectList(edge("impact", 1L, 2L), ImpactHash.class);

        assertNotNull(impactList);
        assertEquals(2, impactList.size());

        assertNotNull(impactList.get(0));
        assertEquals(1, impactList.get(0).src());
        assertEquals(2, impactList.get(0).dst());
        assertEquals("impact", impactList.get(0).label());
        assertEquals("type1", impactList.get(0).getType());

        assertNotNull(impactList.get(1));
        assertEquals(1, impactList.get(1).src());
        assertEquals(2, impactList.get(1).dst());
        assertEquals("impact", impactList.get(1).label());
        assertEquals("type2", impactList.get(1).getType());
    }

    @Test
    public void should_fetch_vertex_with_selection_complicit_alias() {
        final String personName = sessionTemplate.selectOne(vertex(Person.class, "p001", "p002")
                .selectAs(Person::getName, "p_name"), String.class);

        assertNotNull(personName);
        assertEquals("bofa", personName);

        final List<String> personNameList = sessionTemplate.selectList(vertex(Person.class, "p001", "p002")
                .selectAs(Person::getName, "p_name"), String.class);

        assertNotNull(personNameList);
        assertEquals(2, personNameList.size());
        assertEquals("bofa", personNameList.get(0));
        assertEquals("zhangsan", personNameList.get(1));

        final Model.V<String> vertex = sessionTemplate.selectVertex(vertex("person", "p001", "p002")
                .selectAs("name", "p_name"));

        assertNotNull(vertex);
        assertEquals("bofa", vertex.get("p_name"));

        final List<Model.V<String>> vertexList = sessionTemplate.selectVertexList(vertex("person", "p001", "p002").selectAs("name", "p_name"));

        assertNotNull(vertexList);
        assertEquals(2, vertexList.size());
        assertEquals("bofa", vertexList.get(0).get("p_name"));
        assertEquals("zhangsan", vertexList.get(1).get("p_name"));

        final List<Person> personList = sessionTemplate.selectList(vertex(Person.class, "p001", "p002")
                .select(Person::getName), Person.class);

        assertNotNull(personList);
        assertEquals(2, personList.size());
        assertEquals("bofa", personList.get(0).getName());
        assertEquals("zhangsan", personList.get(1).getName());
    }

    @Test
    public void should_fetch_edge_with_selection_complicit_alias() {
        final String impactDstVertexId = sessionTemplate.selectOne(edge(Impact.class, "e001", "e002")
                .selectAs(Impact::dst, "impact_dst_vid"), String.class);

        assertNotNull(impactDstVertexId);
        assertEquals("p002", impactDstVertexId);

        final List<String> impactDstVertexIdList = sessionTemplate.selectList(edge(Impact.class, "e001", "e002")
                .selectAs(Impact::dst, "impact_dst_vid"), String.class);

        assertNotNull(impactDstVertexIdList);
        assertEquals(2, impactDstVertexIdList.size());
        assertEquals("p002", impactDstVertexIdList.get(0));
        assertEquals("p002", impactDstVertexIdList.get(1));

        final Model.E<String, String> edge = sessionTemplate.selectEdge(edge(Impact.class, "e001", "e002")
                .selectAs(Impact::dst, "impact_dst_vid"));

        assertNotNull(edge);
        assertEquals("p002", edge.get("impact_dst_vid"));

        final Impact impact = sessionTemplate.selectOne(edge(Impact.class, "e001", "e002")
                .select(Impact::dst), Impact.class);

        assertNotNull(impact);
        assertEquals("p002", impact.dst());

        final List<Model.E<String, String>> edgeList = sessionTemplate.selectEdgeList(edge(Impact.class, "e001", "e002")
                .select(Impact::dst));

        assertNotNull(edgeList);
        assertEquals(2, edgeList.size());
        assertEquals("p002", edgeList.get(0).dst());
        assertEquals("p002", edgeList.get(1).dst());

    }

    @Test
    public void should_fetch_vertex_with_selection_ordering() {
        final Map<String, Object> result = sessionTemplate.selectMap(vertex(Person.class, "p001", "p002")
                .selectAs(Person::getName, "p_name")
                .select(Person::getAge).descending());

        assertNotNull(result);
        assertEquals("zhangsan", result.get("p_name"));

        final List<Map<String, Object>> personNameList = sessionTemplate.selectMaps(vertex(Person.class, "p001", "p002")
                .selectAs(Person::getName, "p_name")
                .select(Person::getAge).descending());

        assertNotNull(personNameList);
        assertEquals(2, personNameList.size());
        assertEquals("zhangsan", personNameList.get(0).get("p_name"));
        assertEquals("bofa", personNameList.get(1).get("p_name"));

        final Model.V<String> vertex = sessionTemplate.selectVertex(vertex("person", "p001", "p002")
                .selectAs("name", "p_name")
                .select("age").descending());

        assertNotNull(vertex);
        assertEquals("zhangsan", vertex.get("p_name"));
        assertEquals(30L, vertex.<Long>get("age"));

        final List<Model.V<String>> vertexList = sessionTemplate.selectVertexList(vertex("person", "p001", "p002")
                .selectAs("name", "p_name")
                .select("age").descending());

        assertNotNull(vertexList);
        assertEquals(2, vertexList.size());
        assertEquals("zhangsan", vertexList.get(0).get("p_name"));
        assertEquals(30L, vertexList.get(0).<Long>get("age"));
        assertEquals("bofa", vertexList.get(1).get("p_name"));
        assertEquals(26L, vertexList.get(1).<Long>get("age"));

        final List<Person> personList = sessionTemplate.selectList(vertex(Person.class, "p001", "p002")
                .select(Person::getName)
                .select("age").descending(), Person.class);

        assertNotNull(personList);
        assertEquals(2, personList.size());
        assertEquals("zhangsan", personList.get(0).getName());
        assertEquals(30, personList.get(0).getAge());
        assertEquals("bofa", personList.get(1).getName());
        assertEquals(26, personList.get(1).getAge());
    }

    public void should_fetch_edge_with_selection_ordering() {
        final Map<String, Object> result = sessionTemplate.selectMap(edge(Impact.class, "e001", "e002")
                .selectAs(Impact::dst, "impact_dst_vid").ascending()
                .select(Impact::id).descending());

        assertNotNull(result);
        assertEquals("p002", result.get("impact_dst_vid"));
        assertEquals(1L, result.get("rank"));

        final List<Map<String, Object>> resultMapList = sessionTemplate.selectMaps(edge(Impact.class, "e001", "e002")
                .selectAs(Impact::dst, "impact_dst_vid").ascending()
                .select(Impact::id).descending());

        assertNotNull(resultMapList);
        assertEquals("p002", resultMapList.get(0).get("impact_dst_vid"));
        assertEquals(1L, resultMapList.get(0).get("rank"));
        assertEquals("p002", resultMapList.get(1).get("impact_dst_vid"));
        assertEquals(0L, resultMapList.get(1).get("rank"));

        final Model.E<String, String> edge = sessionTemplate.selectEdge(edge(Impact.class, "e001", "e002")
                .selectAs(Impact::dst, "impact_dst_vid").ascending()
                .select(Impact::id).descending());

        assertNotNull(edge);
        assertEquals("p002", edge.get("impact_dst_vid"));
        assertEquals(1L, edge.<Long>get("rank"));

        final Impact impact = sessionTemplate.selectOne(edge(Impact.class, "e001", "e002")
                .selectAs(Impact::dst, "impact_dst_vid").ascending()
                .select(Impact::id).descending(), Impact.class);

        assertNotNull(impact);
        assertEquals("p002", impact.get("impact_dst_vid"));
        assertEquals("e001", impact.id());
    }

    @Test
    public void should_fetch_vertex_with_shadow_selection() {
        final Long cnt = sessionTemplate.selectOne(vertex(Person.class, "p001", "p002")
                .count("cnt"), Long.class);

        assertNotNull(cnt);
        assertEquals(2, cnt);

        final List<Long> resultList = sessionTemplate.selectList(vertex(Person.class, "p001", "p002")
                .count("cnt"), Long.class);

        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(2L, resultList.get(0));

        final Map<String, Object> resultMap = sessionTemplate.selectMap(vertex(Person.class, "p001", "p002")
                .count("cnt"));

        assertNotNull(resultMap);
        assertEquals(2L, resultMap.get("cnt"));

        final List<Map<String, Object>> resultMapList = sessionTemplate.selectMaps(vertex(Person.class, "p001", "p002")
                .count("cnt"));

        assertNotNull(resultMapList);
        assertEquals(1, resultMapList.size());
        assertEquals(2L, resultMapList.get(0).get("cnt"));

        final Model.V<String> vertex = sessionTemplate.selectVertex(vertex(Person.class, "p001", "p002")
                .count("cnt"));

        assertNotNull(vertex);
        assertEquals(2L, vertex.<Long>get("cnt"));

        final List<Model.V<String>> vertexList = sessionTemplate.selectVertexList(vertex(Person.class, "p001", "p002")
                .count("cnt"));

        assertNotNull(vertexList);
        assertEquals(1, vertexList.size());
        assertEquals(2L, vertexList.get(0).<Long>get("cnt"));

        final Model.E<String, String> edge = sessionTemplate.selectEdge(vertex(Person.class, "p001", "p002").count("cnt").render());

        assertNotNull(edge);
        assertEquals(2L, edge.<Long>get("cnt"));

        final List<Model.E<String, String>> edgeList = sessionTemplate.selectEdgeList(vertex(Person.class, "p001", "p002").count("cnt").render());

        assertNotNull(edgeList);
        assertEquals(1, edgeList.size());
        assertEquals(2L, edgeList.get(0).<Long>get("cnt"));
    }

    @Test
    public void should_fetch_edge_with_shadow_selection() {
        final Long cnt = sessionTemplate.selectOne(edge(Impact.class, "e001", "e002")
                .count("cnt"), Long.class);

        assertNotNull(cnt);
        assertEquals(2, cnt);

        final List<Long> resultList = sessionTemplate.selectList(edge(Impact.class, "e001", "e002")
                .count("cnt"), Long.class);

        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(2L, resultList.get(0));

        final Map<String, Object> result = sessionTemplate.selectMap(edge(Impact.class, "e001", "e002")
                .count("cnt"));

        assertNotNull(result);
        assertEquals(2L, result.get("cnt"));

        final Map<String, Object> resultMap = sessionTemplate.selectMap(edge(Impact.class, "e001", "e002")
                .count("cnt"));

        assertNotNull(resultMap);
        assertEquals(2L, resultMap.get("cnt"));

        final List<Map<String, Object>> resultMapList = sessionTemplate.selectMaps(edge(Impact.class, "e001", "e002")
                .count("cnt"));

        assertNotNull(resultMapList);
        assertEquals(1, resultMapList.size());
        assertEquals(2L, resultMapList.get(0).get("cnt"));

        final Model.V<String> vertex = sessionTemplate.selectVertex(edge(Impact.class, "e001", "e002")
                .count("cnt")
                .render());

        assertNotNull(vertex);
        assertEquals(2L, vertex.<Long>get("cnt"));

        final List<Model.V<String>> vertexList = sessionTemplate.selectVertexList(edge(Impact.class, "e001", "e002")
                .count("cnt")
                .render());

        assertNotNull(vertexList);
        assertEquals(1, vertexList.size());
        assertEquals(2L, vertexList.get(0).<Long>get("cnt"));

        final Model.E<String, String> edge = sessionTemplate.selectEdge(edge(Impact.class, "e001", "e002")
                .count("cnt"));

        assertNotNull(edge);
        assertEquals(2L, edge.<Long>get("cnt"));

        final List<Model.E<String, String>> edgeList = sessionTemplate.selectEdgeList(edge(Impact.class, "e001", "e002")
                .count("cnt"));

        assertNotNull(edgeList);
        assertEquals(1, edgeList.size());
        assertEquals(2L, edgeList.get(0).<Long>get("cnt"));
    }

    @Test
    public void should_fetch_vertex_with_shadow_selection_2() {
        final Long cnt = sessionTemplate.selectOne(vertex(Person.class, "p001", "p002")
                .count(Person::getName, "name_cnt"), Long.class);

        assertNotNull(cnt);
        assertEquals(2, cnt);

        final List<Long> resultList = sessionTemplate.selectList(vertex(Person.class, "p001", "p002")
                .count(Person::getName, "name_cnt"), Long.class);

        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(2L, resultList.get(0));

        final Map<String, Object> resultMap = sessionTemplate.selectMap(vertex(Person.class, "p001", "p002")
                .count(Person::getName, "name_cnt"));

        assertNotNull(resultMap);
        assertEquals(2L, resultMap.get("name_cnt"));

        final List<Map<String, Object>> resultMapList = sessionTemplate.selectMaps(vertex(Person.class, "p001", "p002")
                .count(Person::getName, "name_cnt"));

        assertNotNull(resultMapList);
        assertEquals(1, resultMapList.size());
        assertEquals(2L, resultMapList.get(0).get("name_cnt"));

        final Model.V<String> vertex = sessionTemplate.selectVertex(vertex(Person.class, "p001", "p002")
                .count(Person::getName, "name_cnt"));

        assertNotNull(vertex);
        assertEquals(2L, vertex.<Long>get("name_cnt"));

        final List<Model.V<String>> vertexList = sessionTemplate.selectVertexList(vertex(Person.class, "p001", "p002")
                .count(Person::getName, "name_cnt"));

        assertNotNull(vertexList);
        assertEquals(1, vertexList.size());
        assertEquals(2L, vertexList.get(0).<Long>get("name_cnt"));

        final Model.E<String, String> edge = sessionTemplate.selectEdge(vertex(Person.class, "p001", "p002")
                .count(Person::getName, "name_cnt")
                .render());

        assertNotNull(edge);
        assertEquals(2L, edge.<Long>get("name_cnt"));

        final List<Model.E<String, String>> edgeList = sessionTemplate.selectEdgeList(vertex(Person.class, "p001", "p002")
                .count(Person::getName, "name_cnt")
                .render());

        assertNotNull(edgeList);
        assertEquals(1, edgeList.size());
        assertEquals(2L, edgeList.get(0).<Long>get("name_cnt"));
    }

    @Test
    public void should_fetch_edge_with_shadow_selection_2() {
        final Long cnt = sessionTemplate.selectOne(edge(Impact.class, "e001", "e002")
                .count(Impact::id, "rank_cnt"), Long.class);

        assertNotNull(cnt);
        assertEquals(2, cnt);

        final List<Long> resultList = sessionTemplate.selectList(edge(Impact.class, "e001", "e002")
                .count(Impact::id, "rank_cnt"), Long.class);

        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(2L, resultList.get(0));

        final Map<String, Object> result = sessionTemplate.selectMap(edge(Impact.class, "e001", "e002")
                .count(Impact::id, "rank_cnt"));

        assertNotNull(result);
        assertEquals(2L, result.get("rank_cnt"));

        final Map<String, Object> resultMap = sessionTemplate.selectMap(edge(Impact.class, "e001", "e002")
                .count(Impact::id, "rank_cnt"));

        assertNotNull(resultMap);
        assertEquals(2L, resultMap.get("rank_cnt"));

        final List<Map<String, Object>> resultMapList = sessionTemplate.selectMaps(edge(Impact.class, "e001", "e002")
                .count(Impact::id, "rank_cnt"));

        assertNotNull(resultMapList);
        assertEquals(1, resultMapList.size());
        assertEquals(2L, resultMapList.get(0).get("rank_cnt"));

        final Model.V<String> vertex = sessionTemplate.selectVertex(edge(Impact.class, "e001", "e002")
                .count(Impact::id, "rank_cnt")
                .render());

        assertNotNull(vertex);
        assertEquals(2L, vertex.<Long>get("rank_cnt"));

        final List<Model.V<String>> vertexList = sessionTemplate.selectVertexList(edge(Impact.class, "e001", "e002")
                .count(Impact::id, "rank_cnt")
                .render());

        assertNotNull(vertexList);
        assertEquals(1, vertexList.size());
        assertEquals(2L, vertexList.get(0).<Long>get("rank_cnt"));

        final Model.E<String, String> edge = sessionTemplate.selectEdge(edge(Impact.class, "e001", "e002")
                .count(Impact::id, "rank_cnt"));

        assertNotNull(edge);
        assertEquals(2L, edge.<Long>get("rank_cnt"));

        final List<Model.E<String, String>> edgeList = sessionTemplate.selectEdgeList(edge(Impact.class, "e001", "e002")
                .count(Impact::id, "rank_cnt"));

        assertNotNull(edgeList);
        assertEquals(1, edgeList.size());
        assertEquals(2L, edgeList.get(0).<Long>get("rank_cnt"));
    }

    @Test
    public void should_fetch_vertex_with_selection_count() {
        final Long cnt = sessionTemplate.selectOne(vertex(Person.class, "p001", "p002")
                .count("cnt")
                .count(Person::getName, "name_cnt"), Long.class);

        assertNotNull(cnt);
        assertEquals(2, cnt);

        final List<Long> resultList = sessionTemplate.selectList(vertex(Person.class, "p001", "p002")
                .count("cnt")
                .count(Person::getName, "name_cnt"), Long.class);

        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals(2L, resultList.get(0));
        assertEquals(2L, resultList.get(1));

        final Map<String, Object> resultMap = sessionTemplate.selectMap(vertex(Person.class, "p001", "p002")
                .count("cnt")
                .count(Person::getName, "name_cnt"));

        assertNotNull(resultMap);
        assertEquals(2L, resultMap.get("cnt"));
        assertEquals(2L, resultMap.get("name_cnt"));

        final List<Map<String, Object>> resultMapList = sessionTemplate.selectMaps(vertex(Person.class, "p001", "p002")
                .count("cnt")
                .count(Person::getName, "name_cnt"));

        assertNotNull(resultMapList);
        assertEquals(1, resultMapList.size());
        assertEquals(2L, resultMapList.get(0).get("name_cnt"));
        assertEquals(2L, resultMapList.get(0).get("cnt"));

        final Model.V<String> vertex = sessionTemplate.selectVertex(vertex(Person.class, "p001", "p002")
                .count("cnt")
                .count(Person::getName, "name_cnt"));

        assertNotNull(vertex);
        assertEquals(2L, vertex.<Long>get("name_cnt"));
        assertEquals(2L, vertex.<Long>get("cnt"));

        final List<Model.V<String>> vertexList = sessionTemplate.selectVertexList(vertex(Person.class, "p001", "p002")
                .count("cnt")
                .count(Person::getName, "name_cnt"));

        assertNotNull(vertexList);
        assertEquals(1, vertexList.size());
        assertEquals(2L, vertexList.get(0).<Long>get("name_cnt"));
        assertEquals(2L, vertexList.get(0).<Long>get("cnt"));

        final Model.E<String, String> edge = sessionTemplate.selectEdge(vertex(Person.class, "p001", "p002")
                .count("cnt")
                .count(Person::getName, "name_cnt")
                .render());

        assertNotNull(edge);
        assertEquals(2L, edge.<Long>get("name_cnt"));
        assertEquals(2L, edge.<Long>get("cnt"));

        final List<Model.E<String, String>> edgeList = sessionTemplate.selectEdgeList(vertex(Person.class, "p001", "p002")
                .count("cnt")
                .count(Person::getName, "name_cnt")
                .render());

        assertNotNull(edgeList);
        assertEquals(1, edgeList.size());
        assertEquals(2L, edgeList.get(0).<Long>get("name_cnt"));
        assertEquals(2L, edgeList.get(0).<Long>get("cnt"));
    }

    @Test
    public void should_fetch_edge_with_selection_count() {
        final Long cnt = sessionTemplate.selectOne(edge(Impact.class, "e001", "e002")
                .count(Impact::id, "rank_cnt"), Long.class);

        assertNotNull(cnt);
        assertEquals(2, cnt);

        final List<Long> resultList = sessionTemplate.selectList(edge(Impact.class, "e001", "e002")
                .count(Impact::id, "rank_cnt"), Long.class);

        assertNotNull(resultList);
        assertEquals(1, resultList.size());
        assertEquals(2L, resultList.get(0));

        final Map<String, Object> result = sessionTemplate.selectMap(edge(Impact.class, "e001", "e002")
                .count(Impact::id, "rank_cnt"));

        assertNotNull(result);
        assertEquals(2L, result.get("rank_cnt"));

        final Map<String, Object> resultMap = sessionTemplate.selectMap(edge(Impact.class, "e001", "e002")
                .count(Impact::id, "rank_cnt"));

        assertNotNull(resultMap);
        assertEquals(2L, resultMap.get("rank_cnt"));

        final List<Map<String, Object>> resultMapList = sessionTemplate.selectMaps(edge(Impact.class, "e001", "e002")
                .count(Impact::id, "rank_cnt"));

        assertNotNull(resultMapList);
        assertEquals(1, resultMapList.size());
        assertEquals(2L, resultMapList.get(0).get("rank_cnt"));

        final Model.V<String> vertex = sessionTemplate.selectVertex(edge(Impact.class, "e001", "e002")
                .count(Impact::id, "rank_cnt")
                .render());

        assertNotNull(vertex);
        assertEquals(2L, vertex.<Long>get("rank_cnt"));

        final List<Model.V<String>> vertexList = sessionTemplate.selectVertexList(edge(Impact.class, "e001", "e002")
                .count(Impact::id, "rank_cnt")
                .render());

        assertNotNull(vertexList);
        assertEquals(1, vertexList.size());
        assertEquals(2L, vertexList.get(0).<Long>get("rank_cnt"));

        final Model.E<String, String> edge = sessionTemplate.selectEdge(edge(Impact.class, "e001", "e002")
                .count(Impact::id, "rank_cnt"));

        assertNotNull(edge);
        assertEquals(2L, edge.<Long>get("rank_cnt"));

        final List<Model.E<String, String>> edgeList = sessionTemplate.selectEdgeList(edge(Impact.class, "e001", "e002")
                .count(Impact::id, "rank_cnt"));

        assertNotNull(edgeList);
        assertEquals(1, edgeList.size());
        assertEquals(2L, edgeList.get(0).<Long>get("rank_cnt"));
    }

    @Test
    public void should_fetch_vertex_with_selection_sum() {
        final Long sum = sessionTemplate.selectOne(vertex(Person.class, "p001", "p002")
                .sum(Person::getAge, "sum_age")
                .sum("age", "sum_age2"), Long.class);

        assertNotNull(sum);
        assertEquals(26+30, sum);

        final List<Long> resultList = sessionTemplate.selectList(vertex(Person.class, "p001", "p002")
                .sum(Person::getAge, "sum_age")
                .sum("age", "sum_age2"), Long.class);

        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals(26+30L, resultList.get(0));
        assertEquals(26+30L, resultList.get(1));

        final Map<String, Object> resultMap = sessionTemplate.selectMap(vertex(Person.class, "p001", "p002")
                .sum(Person::getAge, "sum_age")
                .sum("age", "sum_age2"));

        assertNotNull(resultMap);
        assertEquals(26+30L, resultMap.get("sum_age"));
        assertEquals(26+30L, resultMap.get("sum_age2"));

        final List<Map<String, Object>> resultMapList = sessionTemplate.selectMaps(vertex(Person.class, "p001", "p002")
                .sum(Person::getAge, "sum_age")
                .sum("age", "sum_age2"));

        assertNotNull(resultMapList);
        assertEquals(1, resultMapList.size());
        assertEquals(26+30L, resultMapList.get(0).get("sum_age"));
        assertEquals(26+30L, resultMapList.get(0).get("sum_age2"));

        final Model.V<String> vertex = sessionTemplate.selectVertex(vertex(Person.class, "p001", "p002")
                .sum(Person::getAge, "sum_age")
                .sum("age", "sum_age2"));

        assertNotNull(vertex);
        assertEquals(26+30L, vertex.<Long>get("sum_age"));
        assertEquals(26+30L, vertex.<Long>get("sum_age2"));

        final List<Model.V<String>> vertexList = sessionTemplate.selectVertexList(vertex(Person.class, "p001", "p002")
                .sum(Person::getAge, "sum_age")
                .sum("age", "sum_age2"));

        assertNotNull(vertexList);
        assertEquals(1, vertexList.size());
        assertEquals(26+30, vertexList.get(0).<Long>get("sum_age"));
        assertEquals(26+30, vertexList.get(0).<Long>get("sum_age2"));

        final Model.E<String, String> edge = sessionTemplate.selectEdge(vertex(Person.class, "p001", "p002")
                .sum(Person::getAge, "sum_age")
                .sum("age", "sum_age2")
                .render());

        assertNotNull(edge);
        assertEquals(26+30L, edge.<Long>get("sum_age"));
        assertEquals(26+30L, edge.<Long>get("sum_age2"));

        final List<Model.E<String, String>> edgeList = sessionTemplate.selectEdgeList(vertex(Person.class, "p001", "p002")
                .sum(Person::getAge, "sum_age")
                .sum("age", "sum_age2")
                .render());

        assertNotNull(edgeList);
        assertEquals(1, edgeList.size());
        assertEquals(26+30L, edgeList.get(0).<Long>get("sum_age"));
        assertEquals(26+30L, edgeList.get(0).<Long>get("sum_age2"));
    }

    @Test
    public void should_match_vertex_with_selection_limit() {
        final List<Person> personList = sessionTemplate.selectList(vertex(Person.class, "p001", "p002", "p003")
                .select(Person::getName)
                .limit(1, 2), Person.class);

        assertNotNull(personList);
        assertEquals(2, personList.size());
        assertEquals("bofa", personList.get(0).getName());
    }

    @Test
    public void should_match_edge_with_selection_limit() {
        final List<Impact> impactList = sessionTemplate.selectList(edge(Impact.class, "e001", "e002")
                .select(Impact::getType)
                .limit(1, 2), Impact.class);

        assertNotNull(impactList);
        assertEquals(1, impactList.size());
        assertEquals("type2", impactList.get(0).getType());
    }
}