# Usar uma imagem base do JDK
FROM openjdk:17-jdk-slim

# Configurar diretório de trabalho
WORKDIR /app

# Copiar o arquivo JAR gerado pelo Maven ou Gradle
COPY target/payment-processing-system-1.0.0.jar app.jar

# Expor a porta da aplicação
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]