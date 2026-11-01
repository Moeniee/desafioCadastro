FROM eclipse-temurin:25-jre

RUN groupadd --system app && useradd --system --gid app --home-dir /app app
WORKDIR /app

COPY target/sistema-cadastro-pets-1.0.0.jar app.jar
COPY formulario.txt formulario.txt

RUN mkdir petsCadastrados && chown -R app:app /app
USER app

ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-Dstdout.encoding=UTF-8", "-jar", "app.jar"]