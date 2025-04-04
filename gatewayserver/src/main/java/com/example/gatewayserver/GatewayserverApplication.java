package com.example.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator buyItRouteConfig(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p
						.path("/buyit/users/**")
						.filters(f -> f.rewritePath("/buyit/users/(?<segment>.*)", "/${segment}")
								.addRequestHeader("X-Response_Time", LocalDateTime.now().toString()))
						.uri("lb://USERS")
				)
				.route(p -> p
						.path("/buyit/products/**")
						.filters(f -> f.rewritePath("/buyit/products/(?<segment>.*)", "/${segment}")
								.addRequestHeader("X-Response_Time", LocalDateTime.now().toString()))
						.uri("lb://PRODUCTS")
				)
				.route(p -> p
						.path("/buyit/media/**")
						.filters(f -> f.rewritePath("/buyit/media/(?<segment>.*)", "/${segment}")
								.addRequestHeader("X-Response_Time", LocalDateTime.now().toString()))
						.uri("lb://MEDIA")
				).build();
	}
}
