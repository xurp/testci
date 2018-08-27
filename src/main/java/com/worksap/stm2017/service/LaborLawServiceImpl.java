package com.worksap.stm2017.service;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.model.LaborLaw;
import com.worksap.stm2017.model.Person;
import com.worksap.stm2017.model.Shift;
import com.worksap.stm2017.model.ShiftGroup;
import com.worksap.stm2017.model.SaveSchedule;
import com.worksap.stm2017.repository.AuthorityRepository;
import com.worksap.stm2017.repository.LaborLawRepository;
import com.worksap.stm2017.repository.PersonRepository;
import com.worksap.stm2017.repository.ShiftGroupRepository;
import com.worksap.stm2017.repository.SaveScheduleRepository;
@Service
public class LaborLawServiceImpl implements LaborLawService {

	
	@Autowired
	private LaborLawRepository laborLawRepository;
	@Transactional
	@Override
	public void saveOrUpateLaborLaw(LaborLaw laborLaw) {
		laborLawRepository.save(laborLaw);
		
	}

	
	@Override
	public Optional<LaborLaw> getLaborLawById(Long id) {
		return laborLawRepository.findById(id);
	}
	
	
  
	
}
