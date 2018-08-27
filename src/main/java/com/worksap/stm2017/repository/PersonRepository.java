package com.worksap.stm2017.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.model.Person;


/**
 * Authority 仓库.
 * 
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
	
}
