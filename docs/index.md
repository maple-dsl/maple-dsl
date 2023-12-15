# Welcome to MapleDSL Documentation

## Purpose
!!! note 
    It inspired by [cypher-dsl](https://github.com/neo4j-contrib/cypher-dsl),
    But the difference is that maple-dsl focuses more on traversal-style and lambda-style on development.

The Maple-DSL has been developed with the needs of most graph database client(e.g. neo4j, nebula-graph-java, redisgraph, etc).
We wanted to avoid string concatenations in our query generation and decided do go with a builder approach, much like we find with jOOQ or in the relational module of Spring Data JDBC.

## Getting Started

### Prepare dependencies
=== "Cypher"
    Inclusion of the Cypher-DSL in a Maven project
    ```xml
    <dependency>
        <groupId>io.github.maple-dsl</groupId>
        <artifactId>maple-dsl-cypher</artifactId>
        <version>1.0.1</version>
    </dependency>
    ```

=== "NebulaGraph"
    Inclusion of the Nebula-DSL in a Maven project
    ```xml
    <dependency>
        <groupId>io.github.maple-dsl</groupId>
        <artifactId>maple-dsl-nebula</artifactId>
        <version>1.0.1</version>
    </dependency>
    ```

### Examples
!!! example "List all Tom Hanks movies released in the 1990s"
    ```java title="constant"
    G.traverse(G.vertex("Person").eq("name", "Tom Hanks"))
        .outE("ACTED_IN")
        .outV("tomHanksMovies", "Movie", it -> it
            .gte("released", 1990)
            .lt("released", 2000)
            .selectAs("released", "movie_released")
            .selectAs("name", "movie_name"))
        .render()   
    ```

    ```java title="lambda function"
    @Label("Person")    Person(String name) extends Model.V
    @Label("Movie")     Movie(String name, int released)  extends Model.V
    @Label("ACTED_IN")  ActedIn(int type)   extends Model.E

    G.traverse(G.vertex(Person.class)
        .eq(Person::getName, "Tom Hanks"))
        .outE(ActedIn.class)
        .outV("tomHanksMovies", Movie.class, it ->it
            .gte(Movie::getReleased, 1990)
            .lt(Movie::getReleased, 2000)
            .selectAs(Movie::getName, "movie_name")
            .selectAs(Movie::getReleased, "movie_released"))
        .render()
    ```
    === "Cypher"
        ```sql
        MATCH (tom:`Person` {name: 'Tom Hanks'})-[:`ACTED_IN`]->(tomHanksMovies)
        WHERE tomHanksMovies.released >= 1990 AND tomHanksMovies.released < 2000
        RETURN tomHanksMovies.name as movie_name, tomHanksMovies.released as movie_released
        ```
    === "NebulaGraph"
        ```sql
        LOOKUP ON `Person` WHERE Person.name == 'Tom Hanks' YIELD id(vertex) as _dst
        | GO FROM $-._dst OVER ACTED_IN WHERE $$.Movie.released >= 1990 AND $$.Movie.released < 2000
        YIELD $$.Movie.name as movie_name, $$.Movie.released as movie_released
        ```
    ```java title=" via vertex ID"
    G.traverse("person_001")
        .outE(ActedIn.class)
        .outV("tomHanksMovies", Movie.class, it -> it
            .gte(Movie::getReleased, 1990)
            .lt(Movie::getReleased, 2000)
            .selectAs(Movie::getName, "movie_name")
            .selectAs(Movie::getReleased, "movie_released"))
        .render()
    ```
    
    === "Cypher"
        ```sql
        MATCH (tom:`Person`)-[:`ACTED_IN`]->(tomHanksMovies)
        WHERE id(tom) == 'person_001' AND tomHanksMovies.released >= 1990 AND tomHanksMovies.released < 2000
        RETURN tomHanksMovies.name as movie_name, tomHanksMovies.released as movie_released
        ```
    === "NebulaGraph"
        ```sql
        GO FROM "person_001" OVER ACTED_IN WHERE $$.Movie.released >= 1990 AND $$.Movie.released < 2000
        YIELD $$.Movie.name as movie_name, $$.Movie.released as movie_released
        ```

??? abstract "List all Tom Hanks movies released in the 1990s, then find out the other person which acted together"
    ```java
    G.traverse("person_001")
        .outE(ActedIn.class)
        .outV("tomHanksMovies", Movie.class, it ->it
            .gte(Movie::getReleased, 1990)
            .lt(Movie::getReleased, 2000)
            .selectAs(Movie::getName, "movie_name")
            .selectAs(Movie::getReleased, "movie_released"))
        .inE(ActedIn.class)    
        .outV("other", Person.class, it -> it
            .ne(Person::id, "person_001")
            .selectAs(Person::getName, "actor_name"))
        .render()
    ```
    === "Cypher"
        ```sql
        MATCH (tom:`Person`-[:`ACTED_IN`]->(tomHanksMovies)<-[:`ACTED_IN`]-(other:`Person`)
        WHERE id(tom) == 'person_001' AND tomHanksMovies.released >= 1990 AND tomHanksMovies.released < 2000 AND id(other) != 'person_001'
        RETURN tomHanksMovies.name as movie_name, tomHanksMovies.released as movie_released, others.name as actor_name
        ```
    === "NebulaGraph"
        ```sql
        GO FROM "person_001" OVER ACTED_IN WHERE $$.Movie.released >= 1990 AND $$.Movie.released < 2000
        YIELD id($$) AS _dst, $$.Movie.name AS movie_name, $$.Movie.released AS movie_released
        | GO FROM $-._dst OVER ACTED_IN REVERSELY WHERE id($$) != 'person_001' 
        YIELD $$.Person.name AS actor_name, $-.movie_name AS movie_name, $-.movie_release AS movie_release
        ```
