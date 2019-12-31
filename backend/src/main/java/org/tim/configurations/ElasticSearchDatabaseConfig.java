package org.tim.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@Profile("develop")
@EnableElasticsearchRepositories
public class ElasticSearchDatabaseConfig {
}
