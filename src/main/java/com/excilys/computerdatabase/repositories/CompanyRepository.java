package com.excilys.computerdatabase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.excilys.computerdatabase.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

}
