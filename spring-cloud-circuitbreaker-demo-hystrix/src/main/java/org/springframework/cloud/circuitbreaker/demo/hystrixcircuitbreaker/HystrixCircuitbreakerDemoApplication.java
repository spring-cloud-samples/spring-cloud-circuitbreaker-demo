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
package org.springframework.cloud.circuitbreaker.demo.hystrixcircuitbreaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.netflix.hystrix.HystrixCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

@SpringBootApplication
public class HystrixCircuitbreakerDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(HystrixCircuitbreakerDemoApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplateBuilder().build();
	}

	@Bean
	public Customizer<HystrixCircuitBreakerFactory> defaultConfig() {
		return factory -> factory.configureDefault(id -> HystrixCommand.Setter
				.withGroupKey(HystrixCommandGroupKey.Factory.asKey(id))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(3000)));
	}

}
