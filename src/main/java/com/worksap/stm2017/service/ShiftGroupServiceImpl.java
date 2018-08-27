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
import com.worksap.stm2017.model.Type;
import com.worksap.stm2017.repository.AuthorityRepository;
import com.worksap.stm2017.repository.PersonRepository;
import com.worksap.stm2017.repository.ShiftGroupRepository;
import com.worksap.stm2017.repository.ShiftRepository;
@Service
public class ShiftGroupServiceImpl implements ShiftGroupService {

	
	@Autowired
	private ShiftGroupRepository shiftGroupRepository;
	
	@Override
	public Page<ShiftGroup> findAll(Pageable pageable) {	
		return shiftGroupRepository.findAll(pageable);
	}
    @Transactional
	@Override
	public void saveOrUpateShiftGroup(ShiftGroup shiftGroup) {		
    	shiftGroupRepository.save(shiftGroup);
	}
    @Transactional
	@Override
	public void removeShiftGroup(Long id) {
    	shiftGroupRepository.deleteById(id);		
	}
	@Override
	public Optional<ShiftGroup> getShiftGroupById(Long id) {
		return shiftGroupRepository.findById(id);
	}
	@Override
	public List<ShiftGroup> listShiftGroups() {
		return shiftGroupRepository.findAll();
	}
	@Transactional
	@Override
	public void removeShift(Long shiftid, Long shiftgroupid) {
		Optional<ShiftGroup> shiftgroup=shiftGroupRepository.findById(shiftgroupid);
		if(shiftgroup.isPresent()){
			ShiftGroup t=shiftgroup.get();
			t.removeShift(shiftid);
			shiftGroupRepository.save(t);
		}
		
	}
	
}
