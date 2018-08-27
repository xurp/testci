package com.worksap.stm2017.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.worksap.stm2017.model.Authority;


/**
 * Authority 仓库.
 * 
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	
}
