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

import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import io.reactivex.Flowable;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvokerProvider;
import org.glassfish.jersey.client.rx.rxjava2.RxFlowableInvoker;
import org.glassfish.jersey.client.rx.rxjava2.RxFlowableInvokerProvider;

import org.springframework.stereotype.Service;
/**
 * @author Ryan Baxter
 */
@Service
public class RxJava2HttpBinService {

	private Client client;

	public RxJava2HttpBinService() {
		this.client = ClientBuilder.newClient();
		this.client.register(RxObservableInvokerProvider.class);
		this.client.register(RxFlowableInvokerProvider.class);
	}

	public Flowable<Map> get() {
		WebTarget getService = client.target("http://httpbin.org/get");
		return getService.request().accept(MediaType.APPLICATION_JSON)
				.rx(RxFlowableInvoker.class).get(Map.class);

	}

	public Flowable<Map> delay(int seconds) {
		WebTarget delayService = client.target("http://httpbin.org/delay/" + seconds);
		return delayService.request().rx(RxFlowableInvoker.class).get(Map.class);
	}

}
