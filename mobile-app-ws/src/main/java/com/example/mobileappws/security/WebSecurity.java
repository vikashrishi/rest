package com.example.mobileappws.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.mobileappws.repository.UserRepository;
import com.example.mobileappws.service.UserService;

@EnableGlobalMethodSecurity(securedEnabled=true,prePostEnabled=true)
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{
	
	private final UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserRepository userRepository;
	
	 public WebSecurity(UserService userService,BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository ) {
		 this.userService = userService;
		 this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		 this.userRepository = userRepository;
	}

	 @Override
		protected void configure(HttpSecurity http) throws Exception {
		    http
		    .cors().and()
		    .csrf().disable()
		        .authorizeRequests()
		        .antMatchers(HttpMethod.POST,SecurityConstants.SIGN_UP_URL)
		        .permitAll()
		        .antMatchers("/v2/api-docs","/configuration/**","/swagger*/**","/webjars/**")
				      .permitAll()
//				      .antMatchers(HttpMethod.DELETE,"/users/**").hasRole("ADMIN")
		        .anyRequest().authenticated().and()
		        .addFilter(getAuthenticationFilter())
		        		.addFilter(new AuthorizationFilter(authenticationManager(),userRepository))
		        		.sessionManagement()
		        		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		    
		    
		}
	 
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
	   
		auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
	}
	
	public AuthenticationFilter getAuthenticationFilter() throws Exception {
		
		final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
		filter.setFilterProcessesUrl("/users/login");
		return filter;
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSources() {
		
		final CorsConfiguration configuration = new CorsConfiguration();
		
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET","POST","DELETE","PUT","OPTIONS"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(Arrays.asList("*"));
		
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		
		return source;
	}

}
