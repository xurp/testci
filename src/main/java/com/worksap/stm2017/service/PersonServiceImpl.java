package com.worksap.stm2017.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.model.Person;
import com.worksap.stm2017.repository.AuthorityRepository;
import com.worksap.stm2017.repository.PersonRepository;
@Service
public class PersonServiceImpl implements PersonService {

	
	@Autowired
	private PersonRepository personRepository;

	@Override
	public Optional<Person> getPersonById(Long id) {
		return personRepository.findById(id);
	}

}
