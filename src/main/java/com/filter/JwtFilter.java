package com.filter;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jwt.TokenGenerator;
import com.service.CustomUserDetailService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{

	@Autowired
	CustomUserDetailService userDetailService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		TokenGenerator tokenGenerator=new TokenGenerator();
		
		 response.setHeader("Access-Control-Allow-Origin", "*");
	        response.setHeader("Access-Control-Allow-Methods", 
	        		"GET, POST, PUT, DELETE, OPTIONS");
	        response.setHeader("Access-Control-Allow-Headers",
	        		"Authorization, Content-Type, Accept, Origin, User-Agent,"
	        		+ " DNT, Cache-Control, X-Mx-ReqToken, Keep-Alive,"
	        		+ " X-Requested-With, If-Modified-Since");
	        response.setHeader("Access-Control-Allow-Credentials", "true");
	        
	        if((request.getRequestURI().equals("/register") && 
	        		request.getMethod().equals("POST"))||(request.getRequestURI().equals("/api/auth") && 
	    	        		request.getMethod().equals("POST")))
			{
				System.out.println("filter invoked");
				filterChain.doFilter(request, response);
				return;
			}
	        else
	        {
	        	System.out.println("filter invoked 1");
	        	String authdata=request.getHeader("Authorization");
	        	System.out.println(authdata);
	        	if(authdata!=null && authdata.startsWith("Bearer"))
	        	{
	        		userDetailService=new CustomUserDetailService();
	        		String original_token=authdata.substring(7);
	        		System.out.println(original_token);
	        		try {
	        		if(tokenGenerator.validate(original_token))
	        		{ 
	        			System.out.println("validated");
	        			 Claims claims = Jwts.parser().
	        					 setSigningKey(tokenGenerator.getSecretkey()).
	        					 parseClaimsJws(original_token).getBody();
	        			 String userId = claims.get("userid", String.class);
	                     String role = claims.get("role", String.class);
	                     //UserDetails userdetails=userDetailService.loadUserByUsername(userId);
						 SecurityContextHolder.getContext().
						 setAuthentication(new
								 UsernamePasswordAuthenticationToken(role,
		 userId, Collections.singleton(new SimpleGrantedAuthority(role))));
						 filterChain.doFilter(request, response);
						 return;
	        		}
	        		
	        		
	        	
	        	else
	        	{
	        		System.out.println("issue with token");
	        		return;
	        	}
	        	}
	        	catch(Exception e) 
	        	{
	        		System.out.println(e);
	        	}
	        }
	}
		// TODO Auto-generated method stub
		
	}

}
