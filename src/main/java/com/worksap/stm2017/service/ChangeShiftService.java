package com.worksap.stm2017.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.model.ChangeShift;
import com.worksap.stm2017.model.LaborLaw;
import com.worksap.stm2017.model.Person;
import com.worksap.stm2017.model.Shift;
import com.worksap.stm2017.model.ShiftGroup;
import com.worksap.stm2017.model.SaveSchedule;
import com.worksap.stm2017.model.User;


public interface ChangeShiftService {
	Page<ChangeShift> findAll(Pageable pageable,String arg);
	Page<ChangeShift> findByUseridOrChanged(Long userid,Long changeid,Pageable pageable,String arg,Long id);
	void saveOrUpateChangeShift(ChangeShift changeShift);
	//void removeShiftGroup(Long id);
	void removeChangeShift(Long id);
	Optional<ChangeShift> getChangeShiftById(Long id);
	//List<ShiftGroup> listShiftGroups();
}
