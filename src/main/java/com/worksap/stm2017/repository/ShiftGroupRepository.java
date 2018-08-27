package com.worksap.stm2017.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.model.Person;
import com.worksap.stm2017.model.Shift;
import com.worksap.stm2017.model.ShiftGroup;


/**
 * Authority 仓库.
 * 
 */
public interface ShiftGroupRepository extends JpaRepository<ShiftGroup, Long> {
	
}
