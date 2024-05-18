# Mutual TLS Tester

Mutual TLS testing tool written in Java & Spring Boot.
The tool consist of two parts:
- TLS Client (in `client` folder)
- TLS Server (in `server` folder)

Both client and server can be config via `application.properties` under resources folder.

## Project Build (Before Run)

```
mvn clean build
```

### Start Server

```
cd server
mvn spring-boot:run
```

### Start Client

```
cd client
mvn spring-boot:run
```
