package com.excilys.computerdatabase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excilys.computerdatabase.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

}
