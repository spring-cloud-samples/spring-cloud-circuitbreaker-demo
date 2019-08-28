/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.cloud.circuitbreaker.demo.reactive;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Flux;

import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ryan Baxter
 */
@RestController
public class RxJava2DemoController {

	Logger LOG = LoggerFactory.getLogger(RxJava2DemoController.class);

	private ReactiveCircuitBreakerFactory circuitBreakerFactory;
	private RxJava2HttpBinService httpBin;

	public RxJava2DemoController(ReactiveCircuitBreakerFactory circuitBreakerFactory, RxJava2HttpBinService rxJava2HttpBinService) {
		this.circuitBreakerFactory = circuitBreakerFactory;
		this.httpBin = rxJava2HttpBinService;
	}

	@GetMapping("/rxjava2/get")
	public Flux<Map> get() {
		return RxJava2Adapter.flowableToFlux(httpBin.get());
	}

	@GetMapping("/rxjava2/delay/{seconds}")
	public Flux<Map> delay(@PathVariable int seconds) {
		return circuitBreakerFactory.create("rxjava2delay").run(RxJava2Adapter.flowableToFlux(httpBin.delay(seconds)), t -> {
			LOG.warn("delay call failed error", t);
			Map<String, String> fallback = new HashMap<>();
			fallback.put("hello", "world");
			return Flux.just(fallback);
		});
	}
}
