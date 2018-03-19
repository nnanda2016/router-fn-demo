package com.demo.fn.web.filter;

import com.demo.fn.exception.AppException;
import com.demo.fn.exception.Exceptions;
import com.demo.fn.web.model.ResourceDetail;
import com.demo.fn.web.util.WebUtilsFunctions;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
public class ResourceParsingFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
		final HttpMethod httpMethod = request.method();
		final String requestPath = request.path();

		// Collect the resource id and type from path
		final ResourceDetail resourceDetail = WebUtilsFunctions.FN_GET_RESOURCE_DETAIL_FROM_PATH.apply(requestPath);

		switch (httpMethod) {
		case GET:
			if (!request.queryParam("q").isPresent() && StringUtils.isBlank(resourceDetail.getResourceId())) {
				return Mono.error(
						new AppException(Exceptions.APP_400002.exceptionCode(), 
								WebUtilsFunctions.FN_FORMAT_STRING.apply(Exceptions.APP_400002.exceptionMessage(), new String[] {httpMethod.name()})));
			}
			break;
		case PUT:
		case DELETE:
			if (StringUtils.isBlank(resourceDetail.getResourceId())) {
				return Mono.error(
						new AppException(Exceptions.APP_400002.exceptionCode(), 
								WebUtilsFunctions.FN_FORMAT_STRING.apply(Exceptions.APP_400002.exceptionMessage(), new String[] {httpMethod.name()})));
			}
			break;
		case POST:
			// TODO: We have to implement validation for search.
			break;
		default:
			break;
		}

		// Store resourceDetail in request
		request.attributes().put("RESOURCE_DETAIL", resourceDetail);

		return next.handle(request);
	}
	
}
