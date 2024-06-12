# Usa una imagen de Maven para construir el proyecto
FROM --platform=linux/amd64 maven:3.8.1-openjdk-11-slim AS builder

# Establece el directorio de trabajo
WORKDIR /equipo4-chatbot

# Copia los archivos pom.xml y src al contenedor
COPY pom.xml .
COPY src ./src


# Establece la variable de entorno para TNS_ADMIN
ENV TNS_ADMIN_DIR=/equipo4-chatbot/creds

# Configura los secretos para acceder a la base de datos y wallet
RUN --mount=type=secret,id=db_user \
    --mount=type=secret,id=db_password \
    --mount=type=secret,id=wallet_secret \
    export DB_USER=$(cat /run/secrets/db_user) && \
    export DB_PASSWORD=$(cat /run/secrets/db_password) && \
    mkdir -p /equipo4-chatbot/creds && \
    tar -xzf /run/secrets/wallet_secret -C /equipo4-chatbot/creds && \
    mvn clean compile package -DskipTests

# Usa una imagen más ligera de OpenJDK para ejecutar la aplicación
FROM --platform=linux/amd64 openjdk:11-slim

# Crea el directorio de trabajo
WORKDIR /equipo4-chatbot

# Copia el jar construido del contenedor anterior
COPY --from=builder /equipo4-chatbot/target/chatbot-0.0.1-SNAPSHOT.jar ./chatbot.jar

# Expone el puerto en el que la aplicación se ejecutará
EXPOSE 8085

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "chatbot.jar"]