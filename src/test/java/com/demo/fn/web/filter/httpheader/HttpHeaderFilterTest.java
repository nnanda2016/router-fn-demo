package com.demo.fn.web.filter.httpheader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testng.annotations.Test;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
@Test(groups = "httpHeaderFilterTests")
@WebFluxTest
@Import({MockAppConfig.class})
public class HttpHeaderFilterTest extends AbstractTestNGSpringContextTests {
	private static final String REQUEST_URI = "/httpHeaders/123";
	private static final String FROM_HEADER_MISSING_JSON_RESPONSE = "{\"code\"=\"APP_400001\", \"error\"=\"Bad Request\", \"debugMessage\"=\"Required header 'From' is missing in the request.\"}";
	private static final String AUTHORIZATION_HEADER_MISSING_JSON_RESPONSE = "{\"code\"=\"APP_400001\", \"error\"=\"Bad Request\", \"debugMessage\"=\"Required header 'Authorization' is missing in the request.\"}";
	
	@Autowired
	private WebTestClient webTestClient;
	
	@Test
	public void bothHeadersMissing() {
		webTestClient.get().uri(REQUEST_URI)
			.exchange() // Fire http request
			.expectStatus() // Assert on HTTP Status
			.isBadRequest()
			.expectHeader() // Assert Headers
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			;
	}
	
	@Test(dependsOnMethods = {"bothHeadersMissing"})
	public void fromHeadersMissing() {
		webTestClient.get().uri(REQUEST_URI)
			.header(HttpHeaders.AUTHORIZATION, "abcd")
			.exchange() // Fire http request
			.expectStatus() // Assert on HTTP Status
			.isBadRequest()
			.expectHeader() // Assert Headers
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody()
			.json(FROM_HEADER_MISSING_JSON_RESPONSE)
			;
	}
	
	@Test(dependsOnMethods = {"fromHeadersMissing"})
	public void authorizationHeadersMissing() {
		webTestClient.get().uri(REQUEST_URI)
			.header(HttpHeaders.FROM, "1234")
			.exchange() // Fire http request
			.expectStatus() // Assert on HTTP Status
			.isBadRequest()
			.expectHeader() // Assert Headers
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody()
			.json(AUTHORIZATION_HEADER_MISSING_JSON_RESPONSE)
			;
	}
	
	@Test(dependsOnMethods = {"authorizationHeadersMissing"})
	public void bothHeadersPresent() {
		webTestClient.get().uri(REQUEST_URI)
			.header(HttpHeaders.FROM, "1234")
			.header(HttpHeaders.AUTHORIZATION, "abcd")
			.exchange() // Fire http request
			.expectStatus() // Assert on HTTP Status
			.isOk()
			;
	}
}
