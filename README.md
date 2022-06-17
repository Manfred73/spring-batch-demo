# spring-batch-demo

This services reads and processes a file with contact details using spring-batch.
Each record read is posted to another contact service which stores the data.
The records in the file are in a Cobol/copybook format (fixed length lines) and need to be parsed and offered to the
contact service in JSON format.

## Configuration

### Environment

| name                          | example                                                       | description                                                                                               |
|-------------------------------|---------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|
| CONTACTSPROCESSOR_APIKEY      | your_contactsprocessor_apikey                                 | The apikey that needs to be provided in the header when communicating with the contactsprocessor service. |
| CONTACTS_BASEURL              | http://contacts:8081/contacts                                 | The URL for the contacts service.                                                                         |
| CONTACTS_APIKEY               | your your_contacts_apikey                                     | The apikey that needs to be provided in the header when communicating with the contacts service.          |
| POSTGRES_USERNAME             | postgres                                                      | The username to use to connect to the postgres database.                                                  |
| POSTGRES_PASSWORD             | your_postgres_password                                        | The password needed to connect to the postgres database.                                                  |
| POSTGRES_DB                   | contactsprocessor                                             | The postgres database name to use.                                                                        |
| SPRING_DATASOURCE_URL         | jdbc:postgresql://contactsprocessor_db:5432/contactsprocessor | The spring datasource url to use.                                                                         |
| SPRING_DATASOURCE_USERNAME    | postgres                                                      | The spring datasource username to use.                                                                    |
| SPRING_DATASOURCE_PASSWORD    | your_postgres_password                                        | The spring datasource password to use.                                                                    |
| SPRING_JPA_HIBERNATE_DDL_AUTO | update                                                        | Hibernate feature for ddl generation. Possible values: none, validate, update, create-drop.               |

## Build & Deploy with Docker
1. This application depends on the contacts service which needs to be build first and started first. Follow the steps in the README for that service first.
2. Run `mvn clean install` to build the application.
3. The configuration for the application is defined in environment variables. For this demo they are defined in two separate files.
   Normally these files should be ignored and not be pushed in version control, but for the purpose of this demo they are so you don't have to create them.
   1. .env_contactsprocessor: will contain the following (values are examples and can be changed to your liking):
      * `CONTACTSPROCESSOR_APIKEY=8c5fac73-0969-492e-b275-d96acc06a495`
      * `CONTACTS_BASEURL=http://localhost:8080`
      * `CONTACTS_APIKEY=cb4e56e7-712d-4899-a8ed-a5f7d9b938ca`
      * `SPRING_DATASOURCE_URL=jdbc:postgresql://contactsprocessor_db:5432/contactsprocessor`
      * `SPRING_DATASOURCE_USERNAME=postgres`
      * `SPRING_DATASOURCE_PASSWORD=postgres`
      * `SPRING_JPA_HIBERNATE_DDL_AUTO=update`
   3. .env_postgres: will contain the following (values are examples and can be changed to your liking):
      * `POSTGRES_USERNAME=postgres`
      * `POSTGRES_PASSWORD=postgres`
      * `POSTGRES_DB=contactsprocessor`
4. Run `docker-compose build` to create a containerized image.
5. Run the container in detached mode with `docker-compose up -d`.

## OpenApi documentation
The openapi documentation for this service can be found under `src/main/resources/openapi/contactsprocessor-openapi.html`.

## The database
This service uses a postgres database with the name contactsprocessor.
Internally it uses the default port 5432 and for remote port 3000 is exposed.
You can connect remotely using the jdbc url: `jdbc:postgresql://localhost:3000/contactsprocessor` with username postgres and password postgres.
