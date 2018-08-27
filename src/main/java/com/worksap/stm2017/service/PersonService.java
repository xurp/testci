package com.worksap.stm2017.service;

import java.util.Optional;

import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.model.Person;


/**
 * Authority 服务接口.
 * 
 */
public interface PersonService {
	/**
	 * 根据ID查询 Authority
	 * @param id
	 * @return
	 */
	Optional<Person> getPersonById(Long id);
}
