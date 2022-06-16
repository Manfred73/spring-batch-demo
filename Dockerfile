FROM openjdk:11
ADD target/contactsprocessor.jar contactsprocessor.jar
ENTRYPOINT ["java", "-jar","contactsprocessor.jar"]
EXPOSE 8080
