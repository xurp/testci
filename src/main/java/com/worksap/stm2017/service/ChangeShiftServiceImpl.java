package com.worksap.stm2017.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.model.ChangeShift;
import com.worksap.stm2017.model.LaborLaw;
import com.worksap.stm2017.model.Person;
import com.worksap.stm2017.model.Shift;
import com.worksap.stm2017.model.ShiftGroup;
import com.worksap.stm2017.model.SaveSchedule;
import com.worksap.stm2017.repository.AuthorityRepository;
import com.worksap.stm2017.repository.ChangeShiftRepository;
import com.worksap.stm2017.repository.LaborLawRepository;
import com.worksap.stm2017.repository.PersonRepository;
import com.worksap.stm2017.repository.ShiftGroupRepository;
import com.worksap.stm2017.repository.SaveScheduleRepository;

@Service
public class ChangeShiftServiceImpl implements ChangeShiftService {

	@Autowired
	private ChangeShiftRepository changeShiftRepository;

	@Transactional
	@Override
	public void saveOrUpateChangeShift(ChangeShift changeShift) {
		changeShiftRepository.save(changeShift);

	}

	@Override
	public Optional<ChangeShift> getChangeShiftById(Long id) {
		return changeShiftRepository.findById(id);
	}

	@Override
	public Page<ChangeShift> findAll(Pageable pageable, String arg) {
		if (arg.equals("all"))
			return changeShiftRepository.findAll(pageable);
		else if (arg.equals("agreed"))
			return changeShiftRepository.findByIsAgreed("true", pageable);
		else if (arg.equals("notyetagreed"))
			return changeShiftRepository.findByIsAgreed("false", pageable);
		else if (arg.equals("approved"))
			return changeShiftRepository.findByIsChanged("true", pageable);
		else if (arg.equals("notyetapproved"))
			return changeShiftRepository.findByIsChanged("false", pageable);
		else if (arg.equals("agreedbutnotyetapproved"))
			return changeShiftRepository.findByIsAgreedAndIsChanged("true", "false", pageable);
		else if (arg.equals("refuse"))
			return changeShiftRepository.findByIsChanged("refuse", pageable);
		return changeShiftRepository.findAll(pageable);
	}

	@Override
	public Page<ChangeShift> findByUseridOrChanged(Long userid, Long changeid, Pageable pageable, String arg, Long id) {
		if (arg.equals("all") && id.longValue() == 1)
			return changeShiftRepository.findByUserid(userid, pageable);
		else if (arg.equals("all") && id.longValue() == 2)
			return changeShiftRepository.findByChangeid(changeid, pageable);
		else if (arg.equals("agreed") && id.longValue() == 1)
			return changeShiftRepository.findByIsAgreedAndUserid("true", userid,pageable);
		else if (arg.equals("agreed") && id.longValue() == 2)
			return changeShiftRepository.findByIsAgreedAndChangeid("true", changeid,pageable);
		else if (arg.equals("notyetagreed")&& id.longValue() == 1)
			return changeShiftRepository.findByIsAgreedAndUserid("false", userid,pageable);
		else if (arg.equals("notyetagreed")&& id.longValue() == 2)
			return changeShiftRepository.findByIsAgreedAndChangeid("false", changeid,pageable);
		else if (arg.equals("approved")&& id.longValue() == 1)
			return changeShiftRepository.findByIsChangedAndUserid("true", userid, pageable);
		else if (arg.equals("approved")&& id.longValue() == 2)
			return changeShiftRepository.findByIsChangedAndChangeid("true", changeid, pageable);
		else if (arg.equals("notyetapproved")&& id.longValue() == 1)
			return changeShiftRepository.findByIsChangedAndUserid("false", userid,  pageable);
		else if (arg.equals("notyetapproved")&& id.longValue() == 2)
			return changeShiftRepository.findByIsChangedAndChangeid("false", changeid,  pageable);
		else if (arg.equals("refuse")&& id.longValue() == 1)
			return changeShiftRepository.findByIsChangedAndUserid("refuse", userid,  pageable);
		else if (arg.equals("refuse")&& id.longValue() == 2)
			return changeShiftRepository.findByIsChangedAndChangeid("refuse", changeid,  pageable);
		else if (arg.equals("agreedbutnotyetapproved")&& id.longValue() == 1)
			return changeShiftRepository.findByIsChangedAndIsAgreedAndUserid("false","true", userid, pageable);
		else if (arg.equals("agreedbutnotyetapproved")&& id.longValue() == 2)
			return changeShiftRepository.findByIsChangedAndIsAgreedAndChangeid("false","true", changeid, pageable);
		return changeShiftRepository.findByUseridOrChangeid(userid, changeid, pageable);
	}
	@Transactional
	@Override
	public void removeChangeShift(Long id) {
		changeShiftRepository.deleteById(id);
		
	}

}
