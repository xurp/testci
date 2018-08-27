package com.worksap.stm2017.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.data.domain.Sort.*;
import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.model.Type;
import com.worksap.stm2017.model.User;
import com.worksap.stm2017.service.AuthorityService;
import com.worksap.stm2017.service.TypeService;
import com.worksap.stm2017.service.UserService;
import com.worksap.stm2017.util.ConstraintViolationExceptionHandler;
import com.worksap.stm2017.vo.Response;


/**
 * User 控制器.
 * 
 */
@RestController
@RequestMapping("/users")
public class UserController {
	

	@Autowired
	private UserService userService;

	@Autowired
	private AuthorityService  authorityService;
	
	@Autowired
	private TypeService typeService;
	/**
	 * 查询所有用户
	 * 
	 * @param async
	 * @param pageIndex
	 * @param pageSize
	 * @param name
	 * @param model
	 * @return
	 */
	@GetMapping
	//@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')") // 指定角色权限才能操作方法
	public ModelAndView list(@RequestParam(value = "async", required = false) boolean async,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(value = "name", required = false, defaultValue = "") String name, Model model) {
		if(SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal() instanceof String)
		return new ModelAndView("/index", "userModel",model);
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal(); 
		Sort sortx = new Sort(new Sort.Order(Sort.Direction.ASC,"id"));//desc
		Pageable pageable = PageRequest.of(pageIndex, pageSize,sortx);
		//Pageable pageable = new PageRequest(pageIndex, pageSize);
		if(userDetails.getUsername().equals("admin")){		
		Page<User> page = userService.listUsersByNameLike(name, pageable);
		List<User> list = page.getContent(); // 当前所在页面数据列表
		model.addAttribute("page", page);
		model.addAttribute("userList", list);
		
		}
		else{
			Page<User> page = userService.findByUserName(userDetails.getUsername(), pageable);
			List<User> list = page.getContent(); // 当前所在页面数据列表
			model.addAttribute("page", page);
			model.addAttribute("userList", list);
			
		}		
		model.addAttribute("username", userDetails.getUsername());
		return new ModelAndView(async == true ? "users/list :: #mainContainerRepleace" : "users/list", "userModel",
				model);
	}
	
	@GetMapping("/employee")
	public ModelAndView listemployee(@RequestParam(value = "async", required = false) boolean async, Model model) {
		try{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		Pageable pageable = PageRequest.of(0, 1);
		Page<User> page = userService.findByUserName(userDetails.getUsername(), pageable);
		List<User> list = page.getContent(); 
		model.addAttribute("user", list.get(0));
		model.addAttribute("typelist",typeService.listTypes());
		model.addAttribute("admin", "user");
		}
		catch(Exception ex){
			
		}
		return new ModelAndView(async == true ? "users/list :: #mainContainerRepleace" : "users/list", "userModel",
				model);
	}

	/**
	 * 获取创建表单页面
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/add")
	public ModelAndView createForm(Model model) {
		//第5个null是type
		model.addAttribute("user", new User(null, null, null, null,null));
		model.addAttribute("typelist",typeService.listTypes());
		return new ModelAndView("users/add", "userModel", model);
	}

    /**
     * 保存或者修改用户
     * @param user
     * @param authorityId
     * @return
     */
    @PostMapping
    public ResponseEntity<Response> saveOrUpateUser(User user, Long authorityId) {
		Long id=-1L;
		if(user.getId()!=null)
			id=user.getId();
    	Optional<User> olduser = userService.getUserById(id);
		boolean flag=true;
		if(olduser.isPresent()){
		    if(user.getPassword().equals(olduser.get().getPassword())){
			    flag=false;//edit but not change the password
			}
		}
		List<Authority> authorities = new ArrayList<>();
		authorities.add(authorityService.getAuthorityById(authorityId).get());
		user.setAuthorities(authorities);
        try {
        	 if(flag){
			 BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
        	 user.setPassword(encoder.encode(user.getPassword().trim()));
			 }
             userService.saveOrUpateUser(user);
             //由于type和user是一个怪胎的多对一关系，所以添加user同时在type里加入user
             //注：好像用两个service的话，没法做到同时事务，但也有这样写的
             //此外这段有没有必要也存疑，因为如果是blog对comment关系，添加comment时是找到blog，然后blog.addcomment,存blog的同时自动存comment的
             //但这里已经手动存了user
             Type type=typeService.getTypeById(user.getType().getId()).get();
             type.addUser(user);
             typeService.saveOrUpateType(type);
        }  catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }
        
        return ResponseEntity.ok().body(new Response(true, "处理成功", user));
    }

	/**
	 * 删除用户
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response> delete(@PathVariable("id") Long id, Model model) {
		try {
			//是否需要，存疑，因为user和type关系比较怪
			typeService.removeUser(id, userService.getUserById(id).get().getType().getId());
			userService.removeUser(id);
			
			
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功"));
	}
    

	/**
	 * 获取修改用户的界面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "edit/{id}")
	public ModelAndView modifyForm(@PathVariable("id") Long id, Model model) {
		Optional<User> user = userService.getUserById(id);
		model.addAttribute("user", user.get());
		model.addAttribute("typelist",typeService.listTypes());
		//所有人都能访问admins的userlist，但只有admin能维护，所以针对普通用户，用admin传到前台，把图标隐藏了
		//当然防护开起来之后可能还要修改
		if(SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal() instanceof String)
		return new ModelAndView("/index", "userModel",model);
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		if(userDetails.getUsername().equals("admin")){
		model.addAttribute("admin", "admin");
		}
		else{
			model.addAttribute("admin", "user");
		}
		return new ModelAndView("users/edit", "userModel", model);
	}
	
	


}