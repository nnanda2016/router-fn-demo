package com.demo.fn.web.util;

import com.demo.fn.exception.AppException;
import com.demo.fn.exception.Exceptions;
import com.demo.fn.web.model.ResourceDetail;

import java.util.MissingFormatArgumentException;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
public class WebUtilsFunctions {
	private WebUtilsFunctions() {}
	
	public static final BiFunction<String, Object[], String> FN_FORMAT_STRING = (message, args) -> {
		if (StringUtils.isBlank(message)) {
			return StringUtils.EMPTY;
		}
		
		try {
			return String.format(message, args);
		} catch (final MissingFormatArgumentException e) {
			
			// Return the incoming message as-is.
			return message;
		}
	};
	
	public static final Function<String, ResourceDetail> FN_GET_RESOURCE_DETAIL_FROM_PATH = requestPath -> {
		final String[] pathElementsArray = StringUtils.split(requestPath, "/");
		final int pathElementCount = pathElementsArray.length;
		
		if (pathElementCount <= 0) {
			throw new AppException(Exceptions.APP_400002.exceptionCode(), FN_FORMAT_STRING.apply(Exceptions.APP_400002.exceptionMessage(), new String[] {requestPath}));
		}
		
		final ResourceDetail resourceDetail = new ResourceDetail();
		if (pathElementCount >= 2) {
			resourceDetail.setResourceId(pathElementsArray[pathElementCount-1]);
			resourceDetail.setResourceType(pathElementsArray[pathElementCount-2]);
		} else {
			resourceDetail.setResourceType(pathElementsArray[0]);
		}
		
		return resourceDetail;
	};
	
	
	
}
