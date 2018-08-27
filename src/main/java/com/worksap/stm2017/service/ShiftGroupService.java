package com.worksap.stm2017.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.model.Person;
import com.worksap.stm2017.model.Shift;
import com.worksap.stm2017.model.ShiftGroup;
import com.worksap.stm2017.model.Type;
import com.worksap.stm2017.model.User;


/**
 * Authority 服务接口.
 * 
 */
public interface ShiftGroupService {
	Page<ShiftGroup> findAll(Pageable pageable);
	void saveOrUpateShiftGroup(ShiftGroup shiftGroup);
	void removeShiftGroup(Long id);
	void removeShift(Long shiftid,Long shiftgroupid);
	Optional<ShiftGroup> getShiftGroupById(Long id);
	List<ShiftGroup> listShiftGroups();
}
