package org.tim.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import java.util.concurrent.TimeUnit;

@Configuration
public class InMemoryElasticsearchConfig {

	@Bean
	public EmbeddedElastic createEmbeddedElasticsearch() {
		return EmbeddedElastic.builder()
				.withElasticVersion("6.4.3")
				.withSetting(PopularProperties.TRANSPORT_TCP_PORT, 9350)
				.withSetting(PopularProperties.CLUSTER_NAME, "in-memory-cluster")
				.withStartTimeout(1, TimeUnit.MINUTES)
				.build();
	}

}
