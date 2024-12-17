package com.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import com.dao.UserDAO;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	UserDAO userDAO;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		//List<com.model.User> userList=userDAO.findAll();
		
		com.model.User user=userDAO.findById(username).get();
		System.out.println(user.getRoles());
		Hibernate.initialize(user.getRoles());
		
 List<SimpleGrantedAuthority> authorities=
		 user.getRoles().stream().map(
				 role->new SimpleGrantedAuthority(role.getRoleName()))
		 .collect(Collectors.toList());
 
 org.springframework.security.core.userdetails.User dbuser
 =new
 org.springframework.security.core.userdetails.User(user.getUserId(), 
		 user.getPassword(), authorities);
		
		return dbuser;
	}

}
