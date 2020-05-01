# Music Albums (Maven Project with MySQL Database and Hibernate ORM)
Used a persistence unit in order to map entities to database  
Implemented a Singleton for EntityManagerFactory  
Used a generic AbstractRepostory to simplify the creation of repositories  
Generated fake entries with JavaFaker  
Implemented the Hopcroft-Karp algorithm, which determines the maximum cardinality matching in a bipartite graph, in order to find the largest set of albums such that no two albums have the same artist or belong to the same genre  
Added tests using JUnit for the matching  
Use the following command to open the application after creating the JAR:  
```
java -jar MusicAlbumsJPA-1.0-jar-with-dependencies.jar
```
