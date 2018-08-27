package com.worksap.stm2017.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.model.Person;
import com.worksap.stm2017.model.Shift;
import com.worksap.stm2017.model.ShiftGroup;
import com.worksap.stm2017.model.User;
import com.worksap.stm2017.repository.AuthorityRepository;
import com.worksap.stm2017.repository.PersonRepository;
import com.worksap.stm2017.repository.ShiftGroupRepository;
import com.worksap.stm2017.repository.ShiftRepository;
@Service
public class ShiftServiceImpl implements ShiftService {

	
	@Autowired
	private ShiftRepository shiftRepository;
	
	@Autowired
	private ShiftGroupRepository shiftGroupRepository;
	
	@Override
	public Page<Shift> findAll(Pageable pageable) {	
		return shiftRepository.findAll(pageable);
	}

	@Override
	public void saveOrUpateShift(Shift shift) {
		shiftRepository.save(shift);
		
	}

	@Override
	public void removeShift(Long id) {
		shiftRepository.deleteById(id);
		
	}

	@Override
	public Optional<Shift> getShiftById(Long id) {
		return shiftRepository.findById(id);
	}

	@Override
	public Page<Shift> listShiftsByNameLike(String name,Pageable pageable) {
		 name = "%" + name + "%";
	        Page<Shift> shifts = shiftRepository.findByNameLike(name, pageable);
	        return shifts;
	}

	@Override
	public List<Shift> findShiftByGroupIds(String[] ids) {
		List<Shift> res=new ArrayList<>();
		for(int i=0;i<ids.length;i++){
			Optional<ShiftGroup> sg=shiftGroupRepository.findById(Long.parseLong(ids[i]));
			if(sg.isPresent()){
				ShiftGroup shifts=sg.get();
				res.addAll(shifts.getShifts());				
			}
		}
		return res;
	}

	@Override
	public Optional<Shift> getShiftByName(String name) {
		return shiftRepository.findByName(name);
	}


}
