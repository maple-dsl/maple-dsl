<p align="center">
  <img width="200px" height="100" src="https://maple-dsl.github.io/maple-dsl/master/icon/logo_dark.svg"/>
  <br>
  <a href="https://central.sonatype.com/search?q=g:io.github.maple-dsl%20%20a:maple-dsl&smo=true">
    <img alt="maven" src="https://img.shields.io/maven-metadata/v.svg?label=maven-central&metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fio%2Fgithub%2Fmaple-dsl%2Fmaple-dsl%2Fmaven-metadata.xml&style=flat-square">
  </a>
  <a href="https://www.apache.org/licenses/LICENSE-2.0">
    <img alt="code style" src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square">
  </a>
  <br>A powerful, flexible, lightning-fast graph query DSL<br>
</p>

## Features
- Support popular graph query language (e.g. Cypher, nGQL, etc)
- Out-of-the-box dsl interfaces for operate graph database.
- Powerful and flexible predicate/selection/traversal/mutation conditional wrapper.
- Lambda-style API Lambda

## Getting started

### Prepare dependency
Inclusion of the Cypher-DSL in a Maven project
```xml
<dependency>
  <groupId>io.github.maple-dsl</groupId>
  <artifactId>maple-dsl-cypher</artifactId>
  <version>1.0.3</version>
</dependency>
```

Inclusion of the Nebula-DSL in a Maven project
```xml
<dependency>
  <groupId>io.github.maple-dsl</groupId>
  <artifactId>maple-dsl-nebula</artifactId>
  <version>1.0.3</version>
</dependency>
```


### How to use

#### Set up domain entities(vertex&edge)
```java
@Label("actor")
class Actor extends Model.V<String> {
  private String name;
}

@Label("movie")
class Movie extends Model.V<String> {
  @Property("movie_name")
  private String name;
  @Property("movie_released")
  private Integer released;
}

@Label("acted_in")
class ActedIn extends Model.E<String,String> {

}
```
#### List all Tom Hanks movies released in the 1990s
```java
var match = G.vertex(Actor.class).eq(Actor::getName, "Tom Hanks");
var statement = G.traverse(match)
    .outE(ActedIn.class)
    .outV("tomHanksMovies", Movie.class, it -> it
      .gte(Movie::getReleased, 1990)
      .lt(Movie::getReleased, 2000)
      .select(Movie::getReleased)
      .selectAs(Movie::getName, "movie_name"))
    .render();
```

#### or List all the actors that acted with Tom Hanks movies released in the 1990s
```java
statement = G.traverse(match)
    .outE(ActedIn.class)
    .outV("tomHanksMovies", Movie.class, it -> it
      .gte(Movie::getReleased, 1990)
      .lt(Movie::getReleased, 2000)
      .select(Movie::getReleased)
      .selectAs(Movie::getName, "movie_name"))
    .inE(ActedIn.class)
    .outV("otherActors", Actor.class, TraversalStep.Step::selectAll)
    .render();

```
