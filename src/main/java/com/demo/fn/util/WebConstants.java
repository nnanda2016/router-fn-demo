package com.demo.fn.util;

import com.google.common.collect.Sets;

import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
public final class WebConstants {
	private WebConstants() {}
	
	public static final String REQUIRED_HEADER_NAMES_DELIMITER = ",";
	
	public static final Set<String> DEFAULT_REQUIRED_HEADERS_SET = Sets.newHashSet(HttpHeaders.FROM, HttpHeaders.AUTHORIZATION);
	
	public static final String ERROR_CODE_KEY = "code";
	public static final String ERROR_REASON_PHRASE_KEY = "error";
	public static final String DEBUG_MESSAGE_KEY = "debugMessage";
	public static final String HTTP_STATUS_KEY = HttpStatus.class.getCanonicalName();
}
