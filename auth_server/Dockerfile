FROM adoptopenjdk/openjdk11
COPY ./target/AuthServer-0.0.1-SNAPSHOT.jar /usr/source/tim-oauth.jar
WORKDIR /usr/source/
CMD java -Dspring.profiles.active=h2 -Dserver.port=$PORT -Xms512m -Xmx512m -Xss512k -jar tim-oauth.jar
