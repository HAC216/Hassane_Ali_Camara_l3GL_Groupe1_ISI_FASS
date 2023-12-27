FROM docker.io/library/openjdk:11-jdk@sha256:99bac5bf83633e3c7399aed725c8415e7b569b54e03e4599e580fc9cdb7c21ab

WORKDIR /app

COPY . /app

# Téléchargez le pilote MySQL Connector/J
ADD https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.26/mysql-connector-java-8.0.26.jar /app/libs/

# Compilez votre application
RUN javac -d . $(find . -name "*.java")

CMD ["java", "-cp", "/app:/app/libs/*", "Main.Main"]
