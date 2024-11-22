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
				.route("auth", r -> r
						.path("/api/auth/**")
						.filters(f -> f.rewritePath("/api/auth/(?<segment>.*)", "/${segment}"))
						.uri("http://user-service:8080/"))

				.route("tasks", r -> r
						.path("/api/tasks/**")
						.filters(f -> f
								.rewritePath("/api/tasks/(?<segment>.*)", "/${segment}")
								.filter(jwtAuthorizationFilter))
						.uri("http://task-service:8080"))

				.build();
	}

}
