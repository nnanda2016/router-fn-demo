package com.demo.fn.web.filter.resourceparsing;

import com.demo.fn.exception.GlobalExceptionHandler;
import com.demo.fn.web.filter.ResourceParsingFilter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * TODO: Add a description
 * 
 * @author Niranjan Nanda
 */
@Configuration
@EnableWebFlux
public class MockAppConfig {
	@Bean
	public ResourceParsingFilter resourceParsingFilterBean() {
		return new ResourceParsingFilter();
	}
	
	@Bean
	public GlobalExceptionHandler globalExceptionHandler(final ApplicationContext applicationContext,
			final ServerCodecConfigurer serverCodecConfigurer) {
		return new GlobalExceptionHandler(applicationContext, serverCodecConfigurer);
	}
}
