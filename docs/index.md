# Welcome to MapleDSL Documentation

## Purpose
> It inspired by [cypher-dsl](https://github.com/neo4j-contrib/cypher-dsl) like below:
> 
> But the difference is that maple-dsl focuses more on traversal-style and lambda-style on development.

The Maple-DSL has been developed with the needs of most graph database client(e.g. neo4j, nebula-graph-java, redisgraph, etc).
We wanted to avoid string concatenations in our query generation and decided do go with a builder approach, much like we find with jOOQ or in the relational module of Spring Data JDBC.

## Quick Started

### Prepare dependencies
Please use a dependency management system. We recommend either Maven or Gradle.

#### Cypher configuration
Inclusion of the Cypher-DSL in a Maven project
```xml
<dependency>
    <groupId>com.maple</groupId>
    <artifactId>maple-cypher-dsl</artifactId>
    <version>0.1.0</version>
</dependency>
```

#### NebulaGraph configuration
Inclusion of the Nebula-DSL in a Maven project
```xml
<dependency>
    <groupId>com.maple</groupId>
    <artifactId>maple-nebula</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Examples
> List all Tom Hanks movies

### Skipped vertex&edge structure defined
```java
G.traverse(G.vertex("Person").eq("name", "Tom Hanks"))
    .outE("ACTED_IN")
    .outV("tomHanksMovies", "Movie", it -> it.selectAs("name", "movie_name"));

// Cypher: 
// MATCH (tom:`Person` {name: 'Tom Hanks'})-[:`ACTED_IN`]->(tomHanksMovies)
// RETURN tomHanksMovies.name as movie_name
        
// If The vertex ID of Tom Hanks is: person_001, you could try like below:
G.traverse("person_001")
    .outE("ACTED_IN")
    .outV("tomHanksMovies", "Movie", it -> it.selectAs("name", "movie_name"));

// Cypher: 
// MATCH (tom:`Person`)-[:`ACTED_IN`]->(tomHanksMovies)
// WHERE id(tom) == 'person_001'
// RETURN tomHanksMovies.name as movie_name
```

### After Defined vertex:Person, vertex:Movie, edge:ACTED_IN
```java
@Label("Person")
class Person extends Model.V {
    String name;
}

@Label("Movie")
class Movie extends Model.V {
    String name;
}

@Label("ACTED_IN")
class ActedIn extends Model.E {
    int type;
}

G.traverse(G.vertex(Person.class).eq(Person::getName, "Tom Hanks"))
    .outE(ActedIn.class)
    .outV("tomHanksMovies", Movie.class, it -> it.selectAs(Movie::getName, "movie_name"));

// If The vertex ID of Tom Hanks is: person_001, you could try like below:
G.traverse("person_001")
    .outE(ActedIn.class)
    .outV("tomHanksMovies", Movie.class, it -> it.selectAs(Movie::getName, "movie_name"));
```

> List all Tom Hanks movies, then find out the other person which acted together.
```java
G.traverse("person_001")
    .outE(ActedIn.class)
    .outV("tomHanksMovies", Movie.class, it -> it.selectAs(Movie::getName, "movie_name"))
    .inE(ActedIn.class)    
    .outV("others", Person.class, it -> it.selectAs(Person::getName, "actor_name"))

// Cypher: 
// MATCH (tom:`Person` {name: 'Tom Hanks'})-[:`ACTED_IN`]->(tomHanksMovies)<-[:`ACTED_IN`]-(others:`Person)
// RETURN tomHanksMovies.name as movie_name, others.name as actor_name
```