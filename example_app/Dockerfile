FROM adoptopenjdk/openjdk11
COPY ./target/app-0.0.1-SNAPSHOT.war /usr/source/swa.jar
WORKDIR /usr/source/
CMD java -Dserver.port=$PORT -Xms512m -Xmx512m -Xss512k -jar swa.jar
