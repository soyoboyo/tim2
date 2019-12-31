package org.tim.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;

@Configuration
public class MongoDatabaseConfig {

	private static final String MONGO_DB_URL = "localhost";
	private static final String MONGO_DB_NAME = "embeded_db";

	@Bean
	public MongoTemplate mongoTemplate() throws IOException {
		cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean mongo = new cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean();
		mongo.setBindIp(MONGO_DB_URL);
		MongoClient mongoClient = mongo.getObject();
		MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, MONGO_DB_NAME);
		return mongoTemplate;
	}
}
