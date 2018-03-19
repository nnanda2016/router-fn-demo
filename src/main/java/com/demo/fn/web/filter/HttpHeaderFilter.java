package com.demo.fn.web.filter;

import com.demo.fn.exception.AppException;
import com.demo.fn.exception.Exceptions;
import com.demo.fn.util.WebConstants;
import com.demo.fn.web.util.StreamUtil;
import com.demo.fn.web.util.WebUtilsFunctions;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

/**
 * Checks mandatory header
 * 
 * @author Niranjan Nanda
 */
public class HttpHeaderFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpHeaderFilter.class);
	/*
	 * Returning Optional from here allows me to chain further.
	 */
	private static final Function<String, Optional<Mono<ServerResponse>>> EXCEPTION_MAPPER = errorMessageArg -> {
		logger.error("Required http header '{}' is missing.", errorMessageArg);
		return Optional.of(Mono.error(
				new AppException(Exceptions.APP_400001.exceptionCode(), 
						WebUtilsFunctions.FN_FORMAT_STRING.apply(Exceptions.APP_400001.exceptionMessage(), new String[] {errorMessageArg})))
		);
	};
	
	private final Set<String> requiredHttpHeaders;
	
	public HttpHeaderFilter(final String requiredHttpHeaderNames) {
		if (StringUtils.isBlank(requiredHttpHeaderNames)) {
			requiredHttpHeaders = WebConstants.DEFAULT_REQUIRED_HEADERS_SET;
		} else {
			final String[] requiredHttpHeadersArray = StringUtils.split(requiredHttpHeaderNames, WebConstants.REQUIRED_HEADER_NAMES_DELIMITER);
			if (ArrayUtils.isEmpty(requiredHttpHeadersArray)) {
				requiredHttpHeaders = WebConstants.DEFAULT_REQUIRED_HEADERS_SET;
			} else {
				requiredHttpHeaders = StreamUtil.asStream(requiredHttpHeadersArray, false).collect(Collectors.toSet());
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
		logger.debug("Request path -> {}", request.path());
		
		return requiredHttpHeaders.stream()
			.filter(header -> CollectionUtils.isEmpty(request.headers().header(header))) // Checks if a mandatory header is missing 
			.findFirst() // Get the first header that passed the previous filter predicate
			.flatMap(EXCEPTION_MAPPER) // If such a header was found, convert that to Mono<ServerResponse> with error in it. (this is a way to throw validation exception
			.orElse(next.handle(request)) // If all mandatory headers are present, delegate to next filter.
			;
	}
	
}
