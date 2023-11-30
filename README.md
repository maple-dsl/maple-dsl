<p align="center">
  <img width="200px" height="100" src="https://maple-dsl.github.io/maple-dsl/master/icon/logo_dark.svg"/>
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
  <version>1.0.0</version>
</dependency>
```

Inclusion of the Nebula-DSL in a Maven project
```xml
<dependency>
  <groupId>io.github.maple-dsl</groupId>
  <artifactId>maple-dsl-nebula</artifactId>
  <version>1.0.0</version>
</dependency>
```


### How to use

#### Set up domain entities(vertex&edge)
```java
@Label("actor")
class Actor extends Model.V {
  private String name;
}

@Label("movie")
class Movie extends Model.V {
  @Property("movie_name")
  private String name;
  @Property("movie_released")
  private Integer released;
}

@Label("acted_in")
class ActedIn extends Model.E {

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
      .selectAs(Movie::getReleased, "movie_released")
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
      .selectAs(Movie::getReleased, "movie_released")
      .selectAs(Movie::getName, "movie_name"))
    .inE(ActedIn.class)
    .outV("otherActors", Actor.class, TraversalStep.Step::selectAll)
    .render();

```
More on [Wiki](https://maple-dsl.github.io/maple-dsl/).
