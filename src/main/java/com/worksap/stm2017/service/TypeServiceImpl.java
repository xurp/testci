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
import com.worksap.stm2017.model.Type;
import com.worksap.stm2017.model.User;
import com.worksap.stm2017.repository.AuthorityRepository;
import com.worksap.stm2017.repository.PersonRepository;
import com.worksap.stm2017.repository.TypeRepository;
@Service
public class TypeServiceImpl implements TypeService {

	
	@Autowired
	private TypeRepository typeRepository;

	@Transactional
	@Override
	public Type saveOrUpateType(Type type) {
		return typeRepository.save(type);
	}
	//@Transactional
	@Override
	public Page<Type> listTypes(Pageable pageable) {
		return typeRepository.findAll(pageable);
	}
	@Transactional
	@Override
	public void removeType(Long id) {
		typeRepository.deleteById(id);
		
	}
	@Override
	public Optional<Type> getTypeById(Long id) {
		
		return typeRepository.findById(id);
	}
	@Override
	public List<Type> listTypes() {
		return typeRepository.findAll();
	}
	@Transactional
	@Override
	public void removeUser(Long userid, Long typeid) {
		Optional<Type> type=typeRepository.findById(typeid);
		if(type.isPresent()){
			Type t=type.get();
			t.removeUser(userid);
			typeRepository.save(t);
		}
		
	}
	@Override
	public Type getSecondType() {
		List<Type> list=typeRepository.findAll();
		if(list!=null&&list.size()>1){
			return list.get(1);
		}
		return null;
	}

}
