# About

This repo contains a collection of apps that demonstrate how to using Spring Cloud CircuitBreaker
with various circuit breaker implementations.

# Non-Reactive Samples

There are two non-reactive samples in this repo, `spring-cloud-circuitbreaker-hystrix` and
`spring-cloud-circuitbreaker-resilience4j`.

When these apps are running there are two endpoints available for testing

1.  `/get` - This endpoint makes a request to [httpbin's `/get` endpoint](http://httpbin.org/#/HTTP_Methods/get_get) and returns the data

2. `/delay/{seconds}` - This endpoint makes a request to [httpbin's `/delay` endpoint](http://httpbin.org/#/Dynamic_data/get_delay__delay_) passing
the delay in the response in seconds.  Since this request can take a significant amount
of time it is wrapped in a circuit breaker.  If the `seconds` parameter is greater than or
equal to 3 the circuit breaker will time out and return the response `{"hello": "world"}`.

# Reactive Samples

There are three reactive samples in this repo, `spring-cloud-circuitbreaker-demo-reactive`,
`spring-cloud-circuitbreaker-demo-reactive-hystrix`, and `spring-cloud-circuibreaker-demo-reactive-resilience4j`.

## Hystrix and Resilience4J Reactive Samples

Both of these samples contain the same two endpoints as the non-reactive samples 
(which in this case return a `Mono`), but also
contain an additional endpoint to demonstrate the use of `Flux`.  This endpoint can be 
accessed by calling `/fluxdelay/{seconds}`.  It has the same functionality as `/delay/{seconds}`
but the fallback returns a `Flux`.

## spring-cloud-circuitbreaker-demo-reactive

This sample demonstrates how to using Spring Cloud CircuitBreaker with reactive types from
RxJava2 and CompletableFuture.  There are four endpoints available for testing in this app

1. `/rxjava2/get` 
2. `/rxjava2/delay/{seconds}`
3. `/completablefuture/get`
4. `/completablefuture/delay/{seconds}`

Each of these have the same functionality as the other endpoints in the other apps.

# Resilience4J and Metrics

With Spring Cloud CircuitBreaker and Resilience4J you can easily collect metrics about
the circuit breakers in your app.  This is demoed in both `spring-cloud-circuitbreaker-demo-resilience4j`
and `spring-cloud-circuitbreaker-demo-reactive-resilience4j`.  You can see the metrics available
by hitting `/actuator/metrics`.

NOTE: The code wrapped by the circuit breaker needs to be executed before any metrics appear

You can also have these metrics collected by Prometheus and visualized in Grafana.  To demonstrate
this, the repo contains a Docker Compose file that will start Prometheus and Grafana locally
and scrape the metrics being surfaces at `/actuator/prometheus`.  To see it in action

1.  Start either `spring-cloud-circuitbreaker-demo-resilience4j` or `spring-cloud-circuitbreaker-demo-reactive-resilience4j`.
2.  `cd` into the `grafana` directory in the root of the repo
3.  Run `docker-compose up`
4.  Go to http://localhost:3000 and login with the username `admin` and the password `admin`
5.  There will be a datasource pointing to the docker container running Prometheus and dashboard already configured to visualize the Resilience4J metrics
6.  Make some requests to the `/delay` endpoint.  You can easily do this with a tool like `watch`.
For example `watch -n 1 http :8080/delay/5`.
7.  If you refresh the Grafana dashboard or set its automatic refresh you should see the graphs begin to change
as requests are made.