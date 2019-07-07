FROM adoptopenjdk/openjdk11
COPY ./target/tim-0.0.1-SNAPSHOT.jar /usr/source/tim.jar
WORKDIR /usr/source/
CMD java -Dspring.profiles.active=develop -Dserver.port=$PORT -Xms512m -Xmx512m -Xss512k -jar tim.jar
