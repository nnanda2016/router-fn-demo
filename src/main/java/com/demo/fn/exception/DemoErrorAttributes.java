package com.demo.fn.exception;

import com.demo.fn.util.WebConstants;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
public class DemoErrorAttributes implements ErrorAttributes {
	
	private static final Logger logger = LoggerFactory.getLogger(DemoErrorAttributes.class);
	
	public static final String CLASS_NAME = DemoErrorAttributes.class.getCanonicalName();
	
	public static final String ERROR_ATTRIBUTE_KEY = "PDPErrorAttributes.ERROR";
	public static final Pattern HTTP_STATUS_CODE_WITHIN_ERROR_CODES_PATTERN = Pattern.compile("_(1|2|3|4|5){1}\\d{2}");
	public static final AppException DEFAULT_EXCEPTION = new AppException("_500000", "Something went wrong while processing your request. Please contact support.");
	
	private final boolean includeException;
	
	/**
	 * 
	 */
	public DemoErrorAttributes() {
		this.includeException = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> getErrorAttributes(final ServerRequest request, final boolean includeStackTrace) {
		final AppException appException = (AppException) getError(request);
		
		final Map<String, Object> errorAttributesMap = Maps.newLinkedHashMap();
		errorAttributesMap.put(WebConstants.ERROR_CODE_KEY, appException.getErrorCode());
		
		final HttpStatus httpStatus = extractHttpStatus(appException.getErrorCode());
		
		// Store the HttpStatus in the map
		errorAttributesMap.put(WebConstants.HTTP_STATUS_KEY, httpStatus);

		// Populate "error"
		errorAttributesMap.put(WebConstants.ERROR_REASON_PHRASE_KEY, httpStatus.getReasonPhrase());
		
		// Populate debug message from appException
		errorAttributesMap.put(WebConstants.DEBUG_MESSAGE_KEY, appException.getErrorMessage());
		
		return errorAttributesMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Throwable getError(final ServerRequest request) {
		return (Throwable) request.attribute(ERROR_ATTRIBUTE_KEY).orElse(DEFAULT_EXCEPTION);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void storeErrorInformation(final Throwable error, final ServerWebExchange exchange) {
		if (error == null) {
			exchange.getAttributes().putIfAbsent(ERROR_ATTRIBUTE_KEY, DEFAULT_EXCEPTION);
			return;
		} 
		
		AppException exceptionToStore = null;
		if (error instanceof AppException) {
			exceptionToStore = (AppException) error;
		} else if (error.getCause() != null && error.getCause() instanceof AppException) {
			exceptionToStore = (AppException) error.getCause();
		} else {
			exceptionToStore = DEFAULT_EXCEPTION;
		}

		exchange.getAttributes().putIfAbsent(ERROR_ATTRIBUTE_KEY, exceptionToStore);
	}

	/**
	 * Returns the value of includeException.
	 *
	 * @return the includeException
	 */
	public boolean isIncludeException() {
		return includeException;
	}
	
	private HttpStatus extractHttpStatus(final String errorCode) {
		try {
			final Matcher matcher = HTTP_STATUS_CODE_WITHIN_ERROR_CODES_PATTERN.matcher(errorCode);
			if (matcher.find()) {
				final int httpStatusCode = Integer.valueOf(StringUtils.substring(matcher.group(0), 1));
				logger.debug("====> Http Status code from error code '{}' -> {}", errorCode, httpStatusCode);
				return HttpStatus.resolve(httpStatusCode);
			}
		} catch (final Exception e) {
			logger.error("Exception while extracting http status from exception code '{}'", errorCode);
		}
		
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
