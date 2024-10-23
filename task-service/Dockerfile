FROM maven:3.8.5-openjdk-17

WORKDIR /task-service
COPY . .

# TODO: make tests work and remove this
RUN mvn clean install -DskipTests

CMD mvn spring-boot:run