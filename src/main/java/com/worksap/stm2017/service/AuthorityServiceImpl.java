/**
 * 
 */
package com.worksap.stm2017.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.repository.AuthorityRepository;



/**
 * Authority 服务接口的实现.
 * 
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

	@Autowired
	private AuthorityRepository authorityRepository;

	@Override
	public Optional<Authority> getAuthorityById(Long id) {
	//public Authority getAuthorityById(Long id) {
		return authorityRepository.findById(id);
		//return authorityRepository.findOne(id);
	}

}
