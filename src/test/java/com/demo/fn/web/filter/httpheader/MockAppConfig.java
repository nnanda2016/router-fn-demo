package com.demo.fn.web.filter.httpheader;

import com.demo.fn.exception.GlobalExceptionHandler;
import com.demo.fn.web.filter.HttpHeaderFilter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
@Configuration
@EnableWebFlux
public class MockAppConfig {
	@Bean
	public HttpHeaderFilter httpHeaderFilterBean() {
		return new HttpHeaderFilter("From,Authorization");
	}
	
	@Bean
	public GlobalExceptionHandler globalExceptionHandler(final ApplicationContext applicationContext,
			final ServerCodecConfigurer serverCodecConfigurer) {
		return new GlobalExceptionHandler(applicationContext, serverCodecConfigurer);
	}
	
	@Bean("httpHeaderFilterTestRoutes")
	public RouterFunction<ServerResponse> httpHeaderFilterTestRoutes() {
		// Dummy route configuration for testing HttpHeaderFilter
		return RouterFunctions.route(RequestPredicates.GET("/httpHeaders/123"), req -> ServerResponse.ok().build())
				.filter(httpHeaderFilterBean());
	}
}
