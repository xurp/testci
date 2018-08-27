package com.worksap.stm2017.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.worksap.stm2017.model.Shift;


/**
 * Authority 仓库.
 * 
 */
public interface ShiftRepository extends JpaRepository<Shift, Long> {
	Page<Shift> findByNameLike(String name, Pageable pageable);
	Optional<Shift> findByName(String name);
}
