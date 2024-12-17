package com.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenGenerator {
	
	private String token;
	private static final String SECRETKEY="##XXXAACCCRR#123###AAA";
	
	public TokenGenerator()
	{ 
		
	}


	public void generateToken(String userid,String password,String role)
	{
		
		this.token=Jwts.builder()
				   .claim("userid", userid)
				   .claim("password",password)
				   .claim("role",role)
				   .signWith(SignatureAlgorithm.HS256, SECRETKEY)
				   .compact();
		
		
		
	}
	
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public static String getSecretkey() {
		return SECRETKEY;
	}
	
	
	public boolean validate(String token) 
	{
		  try {
		Jws<Claims> claims=
Jwts.parser().setSigningKey(SECRETKEY).parseClaimsJws(token);
	  return true;
		  }
		  catch(JwtException e)
		  {
			   return false;
		  }
	}
}








