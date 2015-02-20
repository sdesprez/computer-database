package com.excilys.computerdatabase.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.excilys.computerdatabase.domain.Computer;

/**
 * Repository for Computer
 */
public interface ComputerRepository extends JpaRepository<Computer, Long> {
	
	
	Page<Computer> findByNameContainingOrCompanyNameContaining(String name, String companyName, Pageable pageable);
	
	void deleteByCompanyId(Long id);
}
