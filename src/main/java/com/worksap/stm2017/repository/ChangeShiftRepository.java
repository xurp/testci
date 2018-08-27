package com.worksap.stm2017.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.worksap.stm2017.model.ChangeShift;
import com.worksap.stm2017.model.LaborLaw;
import com.worksap.stm2017.model.SaveSchedule;
import com.worksap.stm2017.model.User;


public interface ChangeShiftRepository extends JpaRepository<ChangeShift, Long> {
	Page<ChangeShift> findByUseridOrChangeid(Long userid, Long changeid,Pageable pageable);
	Page<ChangeShift> findByUserid(Long userid,Pageable pageable);
	Page<ChangeShift> findByChangeid(Long changeid,Pageable pageable);
	Page<ChangeShift> findByIsAgreedAndUseridOrIsAgreedAndChangeid(String isagreed1,Long userid, String isagreed2,Long changeid,Pageable pageable);
	Page<ChangeShift> findByIsAgreedAndUserid(String isagreed1,Long userid, Pageable pageable);
	Page<ChangeShift> findByIsAgreedAndChangeid(String isagreed2,Long changeid,Pageable pageable);
	Page<ChangeShift> findByIsChangedAndUseridOrIsChangedAndChangeid(String ischanged1,Long userid, String ischanged2,Long changeid,Pageable pageable);
	Page<ChangeShift> findByIsChangedAndUserid(String ischanged1,Long userid, Pageable pageable);
	Page<ChangeShift> findByIsChangedAndChangeid(String ischanged2,Long changeid,Pageable pageable);
	Page<ChangeShift> findByIsChangedAndIsAgreedAndUseridOrIsChangedAndIsAgreedAndChangeid(String ischanged1,String isagreed1,Long userid, String ischanged2,String isagreed2,Long changeid,Pageable pageable);
	Page<ChangeShift> findByIsChangedAndIsAgreedAndUserid(String ischanged1,String isagreed1,Long userid, Pageable pageable);
	Page<ChangeShift> findByIsChangedAndIsAgreedAndChangeid(String ischanged2,String isagreed2,Long changeid,Pageable pageable);
	Page<ChangeShift> findByIsAgreed(String isAgreed,Pageable pageable);
	Page<ChangeShift> findByIsChanged(String isChanged,Pageable pageable);
	Page<ChangeShift> findByIsAgreedAndIsChanged(String isagreed,String isChanged,Pageable pageable);
	
}
