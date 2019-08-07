package org.springframework.cloud.circuitbreaker.demo.reactive;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

/**
 * @author Ryan Baxter
 */
@Service
public class CompletableFutureHttpBinService {

	private Client client;

	public CompletableFutureHttpBinService() {
		this.client = ClientBuilder.newClient();
	}

	public CompletableFuture<Map> get() {
		WebTarget getService = client.target("http://httpbin.org/get");
		return getService.request().accept(MediaType.APPLICATION_JSON)
				.rx().get(Map.class).toCompletableFuture();

	}

	public CompletableFuture<Map> delay(int seconds) {
		WebTarget delayService = client.target("http://httpbin.org/delay/" + seconds);
		return delayService.request().rx().get(Map.class).toCompletableFuture();
	}
}
