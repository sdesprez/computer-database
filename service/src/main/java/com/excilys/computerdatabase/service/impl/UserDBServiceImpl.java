package com.excilys.computerdatabase.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.excilys.computerdatabase.domain.User;
import com.excilys.computerdatabase.repositories.UserRepository;
import com.excilys.computerdatabase.service.UserDBService;

/**
 * Spring Data JPA implementation for the UserDBService
 */
@Service
public class UserDBServiceImpl implements UserDBService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//Find a user with the Username
		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}

		//Create a GrantedAuthority list based on the role String from the database
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(user.getRole()));

		//return a springframework user
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(), user.getPassword(), user.isEnabled(), true,
				true, true, authorities);

	}

}
