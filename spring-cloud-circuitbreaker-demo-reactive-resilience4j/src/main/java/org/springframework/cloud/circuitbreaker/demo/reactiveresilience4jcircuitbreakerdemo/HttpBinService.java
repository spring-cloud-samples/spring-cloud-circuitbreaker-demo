/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.cloud.circuitbreaker.demo.reactiveresilience4jcircuitbreakerdemo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Ryan Baxter
 */
@Service
public class HttpBinService {

	private WebClient rest;

	public HttpBinService(WebClient.Builder builder) {
		this.rest = builder.baseUrl("https://httpbin.org").build();
	}

	public Mono<Map> get() {
		return rest.get().uri("/get").retrieve().bodyToMono(Map.class);
	}

	public Mono<Map> delay(int seconds) {
		return rest.get().uri("/delay/{seconds}", seconds).retrieve().bodyToMono(Map.class);
	}

	public Supplier<Mono<Map>> delaySuppplier(int seconds) {
		return () -> this.delay(seconds);
	}

	public Flux<String> fluxDelay(int seconds) {
		return Flux.just("1", "2", "3").delayElements(Duration.ofSeconds(seconds));
	}
}
