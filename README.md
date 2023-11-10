# Show Me What You Got
Movie and series recommendation application

## Naming conventions
### Rest controller HTTP method, path and function

| HTTP   | Path                          | Function name             |
|--------|-------------------------------|---------------------------|
| PUT    | /entity                       | create()                  |
| GET    | /entity/{id}                  | read(id)                  |
| GET    | /entity                       | readAll()                 |
| POST   | /entity/{id}                  | update(id)                |
| DELETE | /entity/{id}                  | delete(id)                |
| PUT    | /entity/{eId}/subEntity/{sId} | addSubEntity(eId, sId)    |
| DELETE | /entity/{eId}/subEntity/{sId} | removeSubEntity(eId, sId) |

## To do

- Set all variable final if possible
- Add @NonNull wherever possible
- Review javadoc param alignment