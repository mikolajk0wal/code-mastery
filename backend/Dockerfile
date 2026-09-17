FROM eclipse-temurin:21 AS builder
WORKDIR /builder

COPY target/code-mastery-0.0.1-SNAPSHOT.jar application.jar

RUN java -Djarmode=layertools -jar application.jar extract --destination extracted


FROM eclipse-temurin:21-jre
WORKDIR /app

RUN useradd -r -M -U nonroot

COPY --from=builder /builder/extracted/dependencies/ ./
COPY --from=builder /builder/extracted/spring-boot-loader/ ./
COPY --from=builder /builder/extracted/snapshot-dependencies/ ./
COPY --from=builder /builder/extracted/application/ ./

USER nonroot:nonroot

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]