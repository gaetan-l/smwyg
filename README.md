# Show Me What You Got
Movie and series recommendation application

## Naming conventions
### Rest controller HTTP method, path and function

| HTTP m. | Path                          | Function name             |
|---------|-------------------------------|---------------------------|
| PUT     | /entity                       | create()                  |
| GET     | /entity/{id}                  | read(id)                  |
| GET     | /entity                       | readAll()                 |
| POST    | /entity/{id}                  | update(id)                |
| DELETE  | /entity/{id}                  | delete(id)                |
| PUT     | /entity/{eId}/subEntity/{sId} | addSubEntity(eId, sId)    |
| DELETE  | /entity/{eId}/subEntity/{sId} | removeSubEntity(eId, sId) |

## Project prerequisites

- JDK17
- Lombok plugin
- WampServer
- Initialize database with mysql > `source path/to/smwyg-init.sql`
- Run SmwygApiApplication => application will run on localhost:8080
- Use Postman or client app to query API

## Build and run application

- Run MySQL database
- Run Gradle task bootJar => will generate application .jar
- Open command prompt and type the following command
- `java -jar path-to-project\build\libs\smwyg-api-0.0.1-SNAPSHOT.jar`  => application will run on localhost:8080
- Use Postman or client to query API

## Test examples

| Case                     | HTTP m. | URL                                               | JSON body              | Expected result                                   |
|--------------------------|---------|---------------------------------------------------|------------------------|---------------------------------------------------|
| Create user              | PUT     | http://localhost:8080/user                        | {"username": "John"}   | {"id":1,"username":"John","favorites":[]}         |
| Query user               | GET     | http://localhost:8080/user/1                      |                        | {"id":1,"username":"John","favorites":[]}         |
| Query all users          | GET     | http://localhost:8080/user                        |                        | [{"id":1,"username":"John","favorites":[]}]       |
| Update user              | POST    | http://localhost:8080/user/1                      | {"username": "Mark"}   | {"id":1,"username":"Mark","favorites":[]}         |
| Delete user              | DELETE  | http://localhost:8080/user/1                      |                        | {}                                                |
| Query all titles         | GET     | http://localhost:8080/title                       |                        | \[*Array of title objects*\]                      |
| Query title              | GET     | http://localhost:8080/title/901362                |                        | {*Title object*}                                  |
| Query similar titles     | GET     | http://localhost:8080/title/901362/similar        |                        | \[*Array of title objects*\]                      |
| Query titles with genres | GET     | http://localhost:8080/title?genres=16,28          |                        | \[*Array of title objects*\]                      |
| Simple title query       | GET     | http://localhost:8080/title/search?query=someText |                        | \[*Array of title objects*\]                      |
| Advanced title query     | GET     | http://localhost:8080/title                       | See example JSON below | \[*Array of title objects*\]                      |
| Add favorite             | PUT     | http://localhost:8080/user/1/favorite/901362      |                        | {"id":1,"username":"Mark","favorites":["901362"]} |
| Remove favorite          | DELETE  | http://localhost:8080/user/1/favorite/901362      |                        | {"id":1,"username":"Mark","favorites":[]}         |

Example JSON for advanced title query :
```
{
    "include_adult": true,
    "primary_release_year": 2010,
    "with_genres": ["Action", "Comedy"]
}
```

## To do

- Add `final` wherever possible
- Add `@NonNull` wherever possible