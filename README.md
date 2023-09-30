<p align="center">
  <img width="200px" height="100" src="https://maple-dsl.github.io/maple-dsl/icon/logo.svg"/>
  <br>A powerful, flexible, lightning-fast graph query DSL<br>
</p>

## Features
- Support popular graph query language (e.g. Cypher, nGQL, etc)
- Out-of-the-box dsl interfaces for operate graph database.
- Powerful and flexible predicate/selection/traversal condition wrapper.
- Lambda-style API Lambda

## Getting started

### Prepare dependency
Inclusion of the Cypher-DSL in a Maven project
```xml
<dependency>
  <groupId>com.maple</groupId>
  <artifactId>maple-cypher-dsl</artifactId>
  <version>0.1.0</version>
</dependency>
```

Inclusion of the Nebula-DSL in a Maven project
```xml
<dependency>
  <groupId>com.maple</groupId>
  <artifactId>maple-nebula</artifactId>
  <version>0.1.0</version>
</dependency>
```

### How to use
```java
// i.e. List all Tom Hanks movies released in the 1990s
var statement = G.traverse(G.vertex("Person").eq("name", "Tom Hanks"))
    .outE("ACTED_IN")
    .outV("tomHanksMovies", "Movie", it -> it
    .gte("released", 1990)
    .lt("released", 2000)
    .selectAs("released", "movie_released")
    .selectAs("name", "movie_name"))
    .render();

var result = sessionTemplate.selectMaps(statement);
```
More on [Wiki](https://maple-dsl.github.io/maple-dsl/).
