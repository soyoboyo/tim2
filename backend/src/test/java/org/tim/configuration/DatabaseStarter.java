package org.tim.configuration;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;

import javax.annotation.PostConstruct;

@Service
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class DatabaseStarter {

	private final EmbeddedElastic embeddedElastic;

	@PostConstruct
	@SneakyThrows
	public void runDatabase() {
		embeddedElastic.start();
	}

}
