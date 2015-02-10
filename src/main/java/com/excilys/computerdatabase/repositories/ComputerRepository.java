package com.excilys.computerdatabase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excilys.computerdatabase.domain.Computer;

public interface ComputerRepository extends JpaRepository<Computer, Long> {

	void deleteByCompanyId(Long id);
}
