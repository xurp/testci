package com.worksap.stm2017.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worksap.stm2017.model.LaborLaw;
import com.worksap.stm2017.model.SaveSchedule;


public interface LaborLawRepository extends JpaRepository<LaborLaw, Long> {
	
}
