# task-postgr-catalog




## Useful commands.

### Typescript.
tsc -p ./front

### Gradle.
gradle build
gradle run
./gradlew build
./gradlew run

### Java.
java -jar ./app/build/libs/app.jar
java -cp ./app/build/libs/app.jar catalog.App

### Docker.
docker build --tag java-docker .
docker images
docker run -p 8080:8080 -i -t java-docker
docker run -i -t java-docker /bin/bash
