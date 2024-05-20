FROM openjdk:11

COPY pom.xml /equipo4-chatbot/

COPY src /equipo4-chatbot/src/

WORKDIR /equipo4-chatbot/

EXPOSE 8080

COPY target/chatbot-0.0.1-SNAPSHOT.jar chatbot.jar

ENTRYPOINT ["java","-jar","chatbot.jar"]