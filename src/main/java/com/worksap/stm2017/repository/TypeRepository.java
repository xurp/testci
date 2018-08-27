package com.worksap.stm2017.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.model.Person;
import com.worksap.stm2017.model.Shift;
import com.worksap.stm2017.model.Type;


/**
 * Type 仓库.
 * 
 */
public interface TypeRepository extends JpaRepository<Type, Long> {
	
}
