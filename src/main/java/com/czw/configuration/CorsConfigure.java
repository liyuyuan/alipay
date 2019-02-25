package com.czw.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/*
 * web 跨域问题
 * author 
 */
@Configuration
public class CorsConfigure extends WebMvcConfigurerAdapter {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("**").allowedOrigins("*").allowedMethods("POST, GET, HEAD, OPTIONS").allowCredentials(true)
				.allowedHeaders(
						"Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,Pragma")
				.exposedHeaders("Access-Control-Allow-Origin,Access-Control-Allow-Headers,Access-Control-Allow-Credentials,Pragma").maxAge(10);
	}

	@Bean
	public FilterRegistrationBean corsFilter() {
		System.out.println("join configur");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		// 设置你要允许的网站域名，如果全允许则设为 *
		config.addAllowedOrigin("*");
		// 如果要限制 HEADER 或 METHOD 请自行更改
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		// 这个顺序很重要哦，为避免麻烦请设置在最前
		bean.setOrder(0);
		return bean;
	}


	/*
	 * @Override public void addCorsMappings(CorsRegistry registry) {
	 * registry.addMapping("/api/**").allowedOrigins("http://192.168.1.97").
	 * allowedMethods("GET", "POST") .allowCredentials(false).maxAge(3600); }
	 */

}
