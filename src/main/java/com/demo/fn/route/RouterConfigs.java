package com.demo.fn.route;

import com.demo.fn.web.filter.HttpHeaderFilter;
import com.demo.fn.web.filter.ResourceParsingFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.*;


/**
 * Configures the web routes.
 * 
 * @author Niranjan Nanda
 */
@Configuration
@EnableWebFlux
public class RouterConfigs {
	
	@Autowired
	private UserApiHandler userApiHandler;
	
	@Autowired
	private StreamHandler streamHandler;
	
	@Autowired
	private HttpHeaderFilter httpHeaderFilter;
	
	@Autowired
	private ResourceParsingFilter resourceParsingFilter;
	
	@Bean
	public RouterFunction<ServerResponse> routes() {
		return RouterFunctions
			.route(healthCheckPredicate(), request -> ServerResponse.ok().build())
			.andNest(streamPathPredicate(), nestedStreamRoutes())
			.andNest(emptyPathPredicate(), nestedBaseRoutes())
			;
	}

	private RequestPredicate healthCheckPredicate() {
		return RequestPredicates.GET("/health/check");
	}
	
	private RequestPredicate emptyPathPredicate() {
		return RequestPredicates.path("");
	}
	
	private RequestPredicate streamPathPredicate() {
		return RequestPredicates.path("/stream");
	}
	
	private RouterFunction<ServerResponse> nestedBaseRoutes() {
		return RouterFunctions
				.route(getByIdPredicate(), userApiHandler::getById)
				.andRoute(addResourcePredicate(), userApiHandler::addUser)
				.filter(httpHeaderFilter
						.andThen(resourceParsingFilter))
				;
	}
	
	private RequestPredicate getByIdPredicate() {
		return RequestPredicates.GET("/*/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8))
				;
	}
	
	private RequestPredicate addResourcePredicate() {
		return RequestPredicates.POST("/*/")
				.and(RequestPredicates.contentType(MediaType.APPLICATION_JSON_UTF8))
				.and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8));
	}
	
	private RouterFunction<ServerResponse> nestedStreamRoutes() {
		return RouterFunctions
				.route(streamGetByIdPredicate(), streamHandler::stream)
				.filter(httpHeaderFilter
						.andThen(resourceParsingFilter))
				;
	}
	
	private RequestPredicate streamGetByIdPredicate() {
		return RequestPredicates
				.GET("/*/{id}")
				.and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8))
				;
	}
}
