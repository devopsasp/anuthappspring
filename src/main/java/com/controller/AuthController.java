package com.controller;

import java.util.ArrayList;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import com.dao.RoleDAO;
import com.dao.UserDAO;

import com.jwt.TokenGenerator;
import com.model.Role;
import com.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

class AuthenticateUser
{ 
	private String userName;
	private String password;
	private String role;
	public AuthenticateUser() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
}

class JwtResponse
{
	private String token;
	
	public JwtResponse(String token) 
	{
		this.token=token;
	}
	public String getToken() { 
		return token;
	}
}


@RestController
public class AuthController {

	@Autowired
	UserDAO userDAO;
	
	@Autowired
	RoleDAO roleDAO;
	
	
	@Autowired
	DaoAuthenticationProvider provider;
	
	@Autowired
PasswordEncoder passwordEncoder;
	

	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) 
	{ 
		System.out.println(user.getUserId());
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		List<Role> roleList=new ArrayList<Role>();
		
		Role role=new Role();
		role.setRoleName("ROLE_USER");
		
		
		user.setRoles(roleList);
		role.setUser(user);
	    userDAO.save(user);
	    roleDAO.save(role);
	    
		
//		TokenGenerator tokenGenerator=new TokenGenerator();
//		tokenGenerator.generateToken(user.getUserId(), user.getPassword(),"ROLE_USER");		
//		
//		String token=tokenGenerator.getToken();
		return new ResponseEntity<>("user created",
				HttpStatus.OK);
	}
	
	
	
	@PostMapping("/api/auth")
	public ResponseEntity<?> authenticate(@RequestBody AuthenticateUser user)
	{
	  TokenGenerator jwtToken=new TokenGenerator();
		AuthenticationManager manager=new ProviderManager(provider);
		
		 Authentication authentication=manager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword()));
		
		 if(authentication.isAuthenticated())
		{
			 String username=user.getUserName();
			 String password=user.getPassword();
			 List<Role> roleList=userDAO.findById(username).get().getRoles();
             ResponseEntity res=new ResponseEntity(HttpStatus.BAD_REQUEST);
			 for(Role r:roleList)
		     {
		    	 if(user.getRole().equals(r.getRoleName()))
		    	 {
		    		 jwtToken.generateToken(username, password,user.getRole());
					  res= new ResponseEntity<JwtResponse>(new JwtResponse(jwtToken.getToken()),HttpStatus.ACCEPTED);
			
		    	 }
		    
		    	 return res;
		    	 
		     }
			 return res;
			 
		}
		else
		{
			return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
			

		}
		 	}

	
	
	
	
	@PostMapping("/getmessage")
	public String getMessage(@RequestBody User user) 
	{ 
		
		
		
			 return "Hello World";
		
	
		
	}
	

}
