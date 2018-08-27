package com.worksap.stm2017.service;

import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.worksap.stm2017.model.User;
import com.worksap.stm2017.repository.UserRepository;


/**
 * 用户服务接口实现.
 *
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	@Override
	public User saveOrUpateUser(User user) {
		return userRepository.save(user);
	}

	@Transactional
	@Override
	public User registerUser(User user) {
		return userRepository.save(user);
	}
	@Transactional
	@Override
	public void removeUser(Long id) {
		//userRepository.delete(id);
		userRepository.deleteById(id);
	}

	@Override
	public Optional<User> getUserById(Long id) {
	//public User getUserById(Long id) {
		//return userRepository.findOne(id);
		return userRepository.findById(id);
	}

	@Override
	public Page<User> listUsersByNameLike(String name, Pageable pageable) {
		
        // 模糊查询
        name = "%" + name + "%";
        Page<User> users = userRepository.findByNameLike(name, pageable);
        return users;
	}
 
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//method of UserDetailsService
		return userRepository.findByUsername(username);
	}
	
	@Override
	public Page<User> findByUserName(String username, Pageable pageable) {
		return userRepository.findByUsernameLike(username, pageable);
	}
	
	@Override
	public User getByUserName(String username) {
		return userRepository.findByUsername(username);
	}
}
