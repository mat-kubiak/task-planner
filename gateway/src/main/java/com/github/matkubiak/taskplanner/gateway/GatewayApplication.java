/*
 * Task Planner
 * Copyright (C) 2024 Mateusz Kubiak
 *
 * Licensed under the GNU General Public License v3.
 * See LICENSE or visit <https://www.gnu.org/licenses/>.
 */

package com.github.matkubiak.taskplanner.gateway;

import com.github.matkubiak.taskplanner.gateway.filters.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

	@Autowired
	private GatewayFilter jwtAuthorizationFilter;

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("auth-health", r -> r
						.path("/api/auth/health")
						.filters(f -> f.setPath("/health"))
						.uri("http://auth-service:8080"))

				.route("auth", r -> r
						.path("/api/auth/**")
						.filters(f -> f.rewritePath("/api/auth/(?<segment>.*)", "/${segment}"))
						.uri("http://auth-service:8080/"))

				.route("user-health", r -> r
						.path("/api/users/health")
						.filters(f -> f.setPath("/health"))
						.uri("http://user-service:8080"))

				.route("user", r -> r
						.path("/api/users/**")
						.filters(f -> f
								.rewritePath("/api/users/(?<segment>.*)", "/${segment}")
								.filter(jwtAuthorizationFilter))
						.uri("http://user-service:8080/"))

				.route("task-health", r -> r
					.path("/api/tasks/health")
					.filters(f -> f.setPath("/health"))
					.uri("http://task-service:8080"))

				.route("task", r -> r
						.path("/api/tasks/**")
						.filters(f -> f
								.rewritePath("/api/tasks/(?<segment>.*)", "/${segment}")
								.filter(jwtAuthorizationFilter))
						.uri("http://task-service:8080"))

				.build();
	}

}
