package com.worksap.stm2017.service;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.model.Person;
import com.worksap.stm2017.model.Shift;
import com.worksap.stm2017.model.ShiftGroup;
import com.worksap.stm2017.model.SaveSchedule;
import com.worksap.stm2017.repository.AuthorityRepository;
import com.worksap.stm2017.repository.PersonRepository;
import com.worksap.stm2017.repository.ShiftGroupRepository;
import com.worksap.stm2017.repository.SaveScheduleRepository;
@Service
public class SaveScheduleServiceImpl implements SaveScheduleService {

	
	@Autowired
	private SaveScheduleRepository saveScheduleRepository;
	
	@Override
	public Optional<SaveSchedule> getSaveScheduleById(Long id) {	
		return saveScheduleRepository.findById(id);
	}
	  
    @Transactional
	@Override
	public void saveOrUpateSaveSchedule(SaveSchedule saveSchedule){		
    	saveScheduleRepository.save(saveSchedule);
	}

	@Override
	public Page<SaveSchedule> listSaveSchedule(Pageable pageable) {
		return saveScheduleRepository.findAll(pageable);
	}
	@Transactional
	@Override
	public void removeSchedule(Long id) {
		saveScheduleRepository.deleteById(id);
		
	}
  
	
}
