package com.demo.fn.web.filter.resourceparsing;

import com.demo.fn.web.util.WebUtilsFunctions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testng.annotations.Test;

import demo.test.config.MockResourceParsingRouterConfig;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
@Test(groups = "resourceParsingFilterTests")
@WebFluxTest
@Import({MockAppConfig.class, MockResourceParsingRouterConfig.class})
public class ResourceParsingFilterTest extends AbstractTestNGSpringContextTests {
	private static final String WITHOUT_ID_JSON_RESPONSE = "{\"code\"=\"APP_400002\", \"error\"=\"Bad Request\", \"debugMessage\"=\"Resource ID must be provided in the request path for '%s' operation.\"}";
	
	@Autowired
	private WebTestClient webTestClient;
	
//	====== Passing tests
	@Test
	public void get_withoutId() {
		webTestClient.get().uri("/resourceParsing/")
			.exchange() // Fire http request
			.expectStatus() // Assert on HTTP Status
			.isBadRequest()
			.expectHeader() // Assert Headers
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody()
			.json(WebUtilsFunctions.FN_FORMAT_STRING.apply(WITHOUT_ID_JSON_RESPONSE, new String[] {HttpMethod.GET.name()}))
			;
	}
	
	@Test(dependsOnMethods = {"get_withoutId"})
	public void get_withoutId_searchQueryParamPresent() {
		webTestClient.get().uri("/resourceParsing/?q=id:345")
			.exchange() // Fire http request
			.expectStatus() // Assert on HTTP Status
			.isOk()
			;
	}
	
	// ====== Failing tests
	@Test(dependsOnMethods = {"get_withoutId_searchQueryParamPresent"})
	public void put_withoutId() {
		webTestClient.put().uri("/resourceParsing/")
			.exchange() // Fire http request
			.expectStatus() // Assert on HTTP Status
			.isBadRequest()
			.expectHeader() // Assert Headers
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody()
			.json(WebUtilsFunctions.FN_FORMAT_STRING.apply(WITHOUT_ID_JSON_RESPONSE, new String[] {HttpMethod.PUT.name()}))
			;
	}
//	
//	@Test
//	public void delete_withoutId() {
//		webTestClient.delete().uri("/resourceParsing/")
//			.exchange() // Fire http request
//			.expectStatus() // Assert on HTTP Status
//			.isBadRequest()
//			.expectHeader() // Assert Headers
//			.contentType(MediaType.APPLICATION_JSON_UTF8)
//			.expectBody()
//			.json(WebUtilsFunctions.FN_FORMAT_STRING.apply(WITHOUT_ID_JSON_RESPONSE, new String[] {HttpMethod.DELETE.name()}))
//			;
//	}
	
//	@Test
//	public void put_withoutId() {
//		webTestClient.put().uri("/resourceParsing/")
//			.exchange() // Fire http request
//			.expectStatus() // Assert on HTTP Status
//			.isBadRequest()
//			.expectHeader() // Assert Headers
//			.contentType(MediaType.APPLICATION_JSON_UTF8)
//			;
//	}
	
}
