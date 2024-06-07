FROM eclipse-temurin:21-jre-ubi9-minimal
LABEL authors="eflerrr"

WORKDIR /app

COPY build/libs/ServiceManagement.jar .

EXPOSE 8080

CMD ["java", "-jar", "ServiceManagement.jar"]
