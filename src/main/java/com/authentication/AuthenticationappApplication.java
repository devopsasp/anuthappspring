package com.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.filter.JwtFilter;
import com.service.CustomUserDetailService;
@SpringBootApplication(scanBasePackages="com.controller")
@EntityScan("com.model")
@EnableJpaRepositories("com.dao")
public class AuthenticationappApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationappApplication.class, args);
	}
	
	@Bean
	@DependsOn("userDetailsService")
	public DaoAuthenticationProvider daoAuthenticationProvider() {
	    CustomUserDetailService service=userDetailsService();

	  
	    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	    
	    provider.setUserDetailsService(service);
	    provider.setPasswordEncoder(passwordEncoder());
	    return provider;
	}

	@Bean
	public  CustomUserDetailService userDetailsService() {
		
	 
	    return new CustomUserDetailService(); // Implement your own user details service
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder(); // Use a password encoder of your choice
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception
	{
		
	httpSecurity.csrf().disable()
		.authorizeRequests().requestMatchers("/register").permitAll()
		.requestMatchers("/api/auth").permitAll()
		
	.requestMatchers("/getmessage").hasRole("USER")
	.anyRequest().authenticated()
	     .and()
      .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class)
   .sessionManagement().disable()
     .authenticationManager(new ProviderManager(daoAuthenticationProvider()));
//	
	
		return httpSecurity.build();
		
	}
}
