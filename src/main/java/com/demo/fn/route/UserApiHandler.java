package com.demo.fn.route;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
@Component
public class UserApiHandler {
	
	public Mono<ServerResponse> getById(final ServerRequest request) {
		return ServerResponse
				.ok()
				.body(BodyInserters.fromObject("Sample response"));
	}
	
	public Mono<ServerResponse> addUser(final ServerRequest request) {
		
		
		return Mono.empty();
	}
}
