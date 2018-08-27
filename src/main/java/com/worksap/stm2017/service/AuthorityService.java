package com.worksap.stm2017.service;

import java.util.Optional;

import com.worksap.stm2017.model.Authority;


/**
 * Authority 服务接口.
 * 
 */
public interface AuthorityService {
	/**
	 * 根据ID查询 Authority
	 * @param id
	 * @return
	 */
	Optional<Authority> getAuthorityById(Long id);
	//Authority getAuthorityById(Long id);
}
