# Kalah

This project implements a Java RestFul web application that enables two users to play a 6-stone Kalah game.

The Game is  implemented as a *Spring Boot v2.2.2* Standalone application with an  embedded (in memory) *Mongo Database v3.4* as persistence mechanism and an embedded *Apache Tomcat v9.0.29*. 

## Installation

For successful  installation the minimum requirements are *Maven  v3.3+* and *Java v8* and above.

In the project root directory  **kalahApp** execute

```bash
mvn dockerfile:build
mvn clean install
```

The project will first download its dependencies run tests and finally produce the project artifact (*kalah-0.0.1-SNAPSHOT.jar*).

On the first run it will take some time especially due to [Embedded MongoDB](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo) dependency. This dependency is used both during test phase as well as main program's execution phase.

After successful build, one can run the application via 

```bash
mvn spring-boot:run
```

> By default it runs the application on Port 8080 . You can change the listening port of the embedded Tomcat Server via `application.properties` 
>
> or passing the respective Port parameter
> 
> ```bash
> mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8080
> ```
>
>  or  running the jar file directly with parameters
>
> ```bash
> java -jar -Dserver.port=8080 target\kalah-0.0.1-SNAPSHOT.jar
> ```
>

Application logs are written in file :

```bash
"\logs\app.log"
```

In order to run  the  generated Docker image of the project just execute the following:

```bash
docker run  -p 8080:8080 dideliba/kalah:0.0.1-SNAPSHOT

```



## Configuration

The configuration is done via the `application.properties`  file:

```bash
"src\main\resources\application.properties"
```

The current configuration is:

```properties
# logging level
logging.level.org.springframework=INFO
logging.level.com.backbase=DEBUG
#disable logging of embedded mongodb (too verbose)
logging.level.org.springframework.boot.autoconfigure.mongo=ERROR
logging.level.org.mongodb.driver=ERROR 
logging.file=logs/app.log
#logging.file=${java.io.tmpdir}/app.log
logging.pattern.file=%d %p %c{1.} [%t] %m%n
logging.pattern.console=%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n
#mongo configuration
spring.data.mongodb.host=127.0.0.1
spring.data.mongodb.port=27018
spring.data.mongodb.database=gamesDB
#enable shutdown of the application via endpoint
management.endpoints.web.exposure.include=*
management.endpoint.shutdown.enabled=true
endpoints.shutdown.enabled=true
```



## Usage

After the application has been deployed,  games can be created and modified to the database after a specific move takes place.

All games will be removed when application is undeployed.

- In order to add a new game call the following service:

```java
curl --header "Content-Type: application/json" --request POST http://localhost:8080/games
```

- A move of a user can be performed by calling:

```java
curl --header "Content-Type: application/json" --request PUT http://localhost:8080/games/{gameId}/pits/{pitId}
```

> where:
> gameId: unique identifier of a game
> pitId: id of the pit selected to make a move. Pits are numbered from 1 to 14 where 7 and 14 are the 	kalah (or house) of each player

- In order to display all created games the following service can be called:

  ```java
  curl  --request GET http://localhost:8080/games
  ```

  or just hit the above url to a web browser

- The  application can be stopped by simply shutting down the process in terminal (i.e. hitting ctr+c) or

via the exposed shutdown endpoint:

```java
curl -X POST http://localhost:8080/actuator/shutdown  
```



## Design

The Game rules are based on related [wikipedia article](https://en.wikipedia.org/wiki/Kalah) .

The design adheres to [SOLID](https://en.wikipedia.org/wiki/SOLID) principles thus the application is readable, extensible and testable. 

It is modeled as a finite state machine using the [State design pattern](https://en.wikipedia.org/wiki/State_pattern). The system can be in one of three different states each time. It is either  the first player's or second player's turn to play or the Game has ended having a specific player as the winner. If a user tries to perform an Illegal action the appropriate message is displayed. Throughout the application the exceptions are handled by the [ControllerAdvice](https://docs.spring.io/spring/docs/5.2.2.RELEASE/javadoc-api/org/springframework/web/bind/annotation/ControllerAdvice.html) **GlobalExceptionHandler**  located in:  

```bash
"src\main\java\com\backbase\games\kalah\web\rest\controller\GlobalExceptionHandler.java"
```

For logging purposes a custom AOP aspect has been implemented to log the start and end of a method and the exceptions that might be thrown in them. 

```bash
"src\main\java\com\backbase\games\kalah\aspect\LoggingAspect.java"
```



Bellow is a UML representation of the state related classes:

![kalah model](https://user-images.githubusercontent.com/60351395/73185472-cb15fd00-4126-11ea-89b6-fb24fb023f66.png)

In addition the Game has been separated in three layers. The **DAO layer** which is an interface that abstracts access to the underlying persistence technology

(we can easily change the underlying persistence technology in this way). The DAO layer's implementation converts **Game** objects to persistence technology specific objects.

In this implementation the DAO layer uses a [mongoDB Spring Data repository](https://docs.spring.io/spring-data/mongodb/docs/2.2.3.RELEASE/reference/html/#mongo.repositories)  that uses **GameDTO**  entities that represent a particular game in mongoDB. 

The **Service layer** use methods provided by the DAO layer's interface for performing CRUD operations on Game's data store.

Finally the **Web layer** uses  methods provided by the Service layer's interface.

Bellow is a UML representation of the class diagram:

![kalah services](https://user-images.githubusercontent.com/60351395/73185803-6b6c2180-4127-11ea-8de3-602a4710e0dc.png)



## Improvements

- There may occur a race condition issue when  two players  perform a move at the same time for a specific Game. This can be easily solved by a locking mechanism on the move service method based on Game ID level. For example a [ConcurentHashMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html) having as key the Game ID and as value a [Semaphore](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Semaphore.html) that each thread acquires before executing a move. When the move has been successfully performed (or an exception is occurred) the specific lock is released. This way the Thread will block until the other Thread has completed its move. 
- More unit tests can be added 
