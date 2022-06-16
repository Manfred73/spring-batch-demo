# spring-batch-demo

This services reads and processes a file with contact details using spring-batch.
Each record read is posted to another contact service which stores the data.
The records in the file are in a Cobol/copybook format (fixed length lines) and need to be parsed and offered to the
contact service in JSON format.

## Configuration

### Environment

| name                          | example                                    | description                                                                                              |
|-------------------------------|--------------------------------------------|----------------------------------------------------------------------------------------------------------|
| CONTACTSPROCESSOR_APIKEY      | your_contactprocessor_apikey               | The apikey that needs to be provided in the header when communicating with the contactprocessor service. |
| CONTACTS_BASEURL              | http://contacts:8081/contacts              | The URL for the contacts service.                                                                        |
| CONTACTS_APIKEY               | your your_contacts_apikey                  | The apikey that needs to be provided in the header when communicating with the contacts service.         |
| POSTGRES_USERNAME             | postgres                                   | The username to use to connect to the postgres database.                                                 |
| POSTGRES_PASSWORD             | your_postgres_password                     | The password needed to connect to the postgres database.                                                 |
| POSTGRES_DB                   | postgres                                   | The postgres database name to use.                                                                       |
| SPRING_DATASOURCE_URL         | jdbc:postgresql://PostgreSQL:5432/postgres | The spring datasource url to use.                                                                        |
| SPRING_DATASOURCE_USERNAME    | postgres                                   | The spring datasource username to use.                                                                   |
| SPRING_DATASOURCE_PASSWORD    | your_postgres_password                     | The spring datasource password to use.                                                                   |
| SPRING_JPA_HIBERNATE_DDL_AUTO | update                                     | Hibernate feature for ddl generation. Possible values: none, validate, update, create-drop.              |

## Build & Deploy with Docker
1. This application depends on the contact service which needs to be build first and started first. Follow the steps in the README for that service first.
2. Run `mvn clean install` to build the application.
3. The configuration for the application is defined in environment variables. Create three env files:
   1. .env_contactprocessor: will contain the following (values are examples and can be changed to your liking):
      * `CONTACTSPROCESSOR_APIKEY=8c5fac73-0969-492e-b275-d96acc06a495`
      * `CONTACTS_BASEURL=http://localhost:8080`
      * `CONTACTS_APIKEY=cb4e56e7-712d-4899-a8ed-a5f7d9b938ca`
      * `SPRING_DATASOURCE_URL=jdbc:postgresql://PostgreSQL:5432/postgres`
      * `SPRING_DATASOURCE_USERNAME=postgres`
      * `SPRING_DATASOURCE_PASSWORD=postgres`
      * `SPRING_JPA_HIBERNATE_DDL_AUTO=update`
   2. .env_contacts: will contain the following (values are examples and can be changed to your liking):
      * `CONTACTS_BASEURL=http://localhost:8080`
      * `CONTACTS_APIKEY=cb4e56e7-712d-4899-a8ed-a5f7d9b938ca`
   3. .env_postgres: will contain the following (values are examples and can be changed to your liking):
      * `POSTGRES_USERNAME=postgres`
      * `POSTGRES_PASSWORD=postgres`
      * `POSTGRES_DB=postgres`
5. Run `docker-compose build` to create a containerized image.
6. Run the container in detached mode with `docker-compose up -d`.