package com.ricardo.taller.app.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ricardo.taller.app.util.CorsFilter;
import com.ricardo.taller.app.util.CustomBasicAuthenticationEntryPoint;
import com.ricardo.taller.app.util.Http403UnauthorizedEntryPoint;
import com.ricardo.taller.app.util.UtilConstants;

/**
 * 
 * @author ricardo
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private static String REALM = "SITIO_TALLER";

	@Autowired
	Http403UnauthorizedEntryPoint authenticationEntryPoint;

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(UtilConstants.USERNAME_APP).password("{noop}"+UtilConstants.PASSWORD_APP).roles("ADMIN");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		.antMatchers("/producto/**").hasRole("ADMIN")
		.antMatchers("/usuario/**").hasRole("ADMIN")
		.and().httpBasic().realmName(REALM)
				.authenticationEntryPoint(getBasicAuthEntryPoint()).and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.cors();
	}

	@Bean
	public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint() {
		return new CustomBasicAuthenticationEntryPoint();
	}


	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
		// setAllowCredentials(true) is important, otherwise:
		// The value of the 'Access-Control-Allow-Origin' header in the response must
		// not be the wildcard '*' when the request's credentials mode is
		// 'include'.
		configuration.setAllowCredentials(true);
		// setAllowedHeaders is important! Without it, OPTIONS preflight request
		// will fail with 403 Invalid CORS request
		configuration.setAllowedHeaders(
				Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Origin", "Accept", "Enctype"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}


	@Bean
	public FilterRegistrationBean corsRegistration() {
	    FilterRegistrationBean registration = new FilterRegistrationBean();
	    registration.setFilter(new CorsFilter());
	    registration.setOrder(0);
	    return registration;
	} 

}
