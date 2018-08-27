package com.worksap.stm2017.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.model.Person;
import com.worksap.stm2017.model.User;
import com.worksap.stm2017.service.AuthorityService;
import com.worksap.stm2017.service.PersonService;
import com.worksap.stm2017.service.UserService;


/**
 * 主页控制器.
 */
@Controller
public class MainController {

	private static final Long ROLE_USER_AUTHORITY_ID = 2L; // 用户权限

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthorityService  authorityService;
	
	@Autowired
	private PersonService personService;
	
	@GetMapping("/")
	public String root() {
		return "redirect:/index";
	}
	
	@GetMapping("/index")
	public String index(Model model) {
		//Optional<Person> persons=personService.getPersonById(1L);
		//System.out.println(persons.get().getName());
		Object obj=SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		if(obj!=null&&!(obj instanceof String)){
		UserDetails userDetails = (UserDetails) obj;
		model.addAttribute("login", true);
		model.addAttribute("getusername", userDetails.getUsername());		
		}
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/readme")
	public String readme() {
		return "readme";
	}
	
	@GetMapping("/usermanual")
	public String usermanual() {
		return "usermanual";
	}

	@GetMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		model.addAttribute("errorMsg", "login failed. wrong username or password.");
		return "login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}

	/**
	 * 注册用户
	 * @param user
	 * @return
	 */
    @PostMapping("/register")
    public String registerUser(User user) {
		List<Authority> authorities = new ArrayList<>();
		authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID).get());
		//authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID));
		user.setAuthorities(authorities);
        userService.registerUser(user);
        return "redirect:/login";
    }
	
}
