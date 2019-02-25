package com.czw.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
	@Autowired
	private RestTemplateBuilder build; 
//
//	@Autowired
//	CloseableHttpClient httpClient;
//
//	@Bean
//	public RestTemplate restTemplate() {
//		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
//		return restTemplate;
//	}
//
//	@Bean
//	public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
//		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//		clientHttpRequestFactory.setHttpClient(httpClient);
//		return clientHttpRequestFactory;
//	}
//
//	@Bean
//	public TaskScheduler taskScheduler() {
//		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//		scheduler.setThreadNamePrefix("poolScheduler");
//		scheduler.setPoolSize(50);
//		return scheduler;
//	}
//
//	
	@Bean
    public RestTemplate restTemplate(){
        return build.build();
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);//单位为ms
        factory.setConnectTimeout(5000);//单位为ms
        return factory;
    }
    
}
