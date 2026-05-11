FROM amazoncorretto:25-alpine-jdk

COPY target/gestion_inventario_cifpp-0.0.1-SNAPSHOT.jar /api-v1.jar 

ENTRYPOINT ["java", "-jar", "/api-v1.jar"]
