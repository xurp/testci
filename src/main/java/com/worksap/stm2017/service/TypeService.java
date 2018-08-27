package com.worksap.stm2017.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.model.Person;
import com.worksap.stm2017.model.Type;
import com.worksap.stm2017.model.User;


/**
 * TypeService 服务接口.
 * 
 */
public interface TypeService {
	 /**
     * 新增、编辑、保存type
     * @param type
     * @return
     */
    Type saveOrUpateType(Type type);
    
    /**
     * 删除type
     * @param id
     * @return
     */
    void removeType(Long id);
    void removeUser(Long userid,Long typeid);
    
    /**
     * 展示type列表
     * @param pageable
     * @return
     */
    Page<Type> listTypes(Pageable pageable);
    List<Type> listTypes();
    
    
    /**
     * 根据id获取type
     * @param id
     * @return
     */
    Optional<Type> getTypeById(Long id);
    Type getSecondType();
}
