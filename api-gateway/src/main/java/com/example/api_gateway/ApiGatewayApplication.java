package com.example.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

//	@Bean
//	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//		return builder.routes()
//				.route("user-service", r -> r.path("/users/**")
//						.uri("lb://user-service"))
//				.route("installation-service", r -> r.path("/installations/**")
//						.uri("lb://hydrogen-installation-service"))
//				.route("monitoring-service", r -> r.path("/monitoring/**")
//						.uri("lb://monitoring-service"))
//				.route("notification-service", r -> r.path("/notifications/**")
//						.uri("lb://notification-service"))
//				.build();
//	}
}
