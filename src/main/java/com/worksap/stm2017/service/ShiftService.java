package com.worksap.stm2017.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.model.Person;
import com.worksap.stm2017.model.Shift;
import com.worksap.stm2017.model.ShiftGroup;
import com.worksap.stm2017.model.User;


/**
 * Authority 服务接口.
 * 
 */
public interface ShiftService {
	Page<Shift> findAll(Pageable pageable);
	void saveOrUpateShift(Shift shiftGroup);
	void removeShift(Long id);
	Optional<Shift> getShiftById(Long id);
	Optional<Shift> getShiftByName(String name);
	Page<Shift> listShiftsByNameLike(String name,Pageable pageable);
	List<Shift> findShiftByGroupIds(String[] ids);
}
