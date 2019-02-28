package com.worksap.stm2017.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import com.worksap.stm2017.model.GoodsInfo;

@Component
public interface GoodsRepository extends ElasticsearchRepository<GoodsInfo,Long> {
	Page<GoodsInfo> findByNameLike(String name, Pageable pageable);
}