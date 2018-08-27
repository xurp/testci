package com.worksap.stm2017.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.model.LaborLaw;
import com.worksap.stm2017.model.Person;
import com.worksap.stm2017.model.Shift;
import com.worksap.stm2017.model.ShiftGroup;
import com.worksap.stm2017.model.SaveSchedule;
import com.worksap.stm2017.model.User;


public interface LaborLawService {
	//Page<ShiftGroup> findAll(Pageable pageable);
	void saveOrUpateLaborLaw(LaborLaw laborLaw);
	//void removeShiftGroup(Long id);
	//void removeShift(Long shiftid,Long shiftgroupid);
	Optional<LaborLaw> getLaborLawById(Long id);
	//List<ShiftGroup> listShiftGroups();
}
