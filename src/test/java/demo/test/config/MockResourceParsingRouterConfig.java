package demo.test.config;

import com.demo.fn.web.filter.ResourceParsingFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class MockResourceParsingRouterConfig {
	@Autowired
	private ResourceParsingFilter resourceParsingFilter;
	
	@Bean("resourceParsingFilterRoutes")
	public RouterFunction<ServerResponse> resourceParsingFilterRoutes() {
		// Dummy route configuration for testing ResourceParsingFilter
		return RouterFunctions.route(RequestPredicates.GET("/resourceParsing/{id}"), req -> ServerResponse.ok().build())
				.andRoute(RequestPredicates.PUT("/resourceParsing/{id}"), req -> ServerResponse.ok().build())
				.andRoute(RequestPredicates.DELETE("/resourceParsing/{id}"), req -> ServerResponse.ok().build())
				.andRoute(RequestPredicates.GET("/resourceParsing/"), req -> ServerResponse.ok().build()) // GET Search URL
				.filter(resourceParsingFilter);
	}
}

