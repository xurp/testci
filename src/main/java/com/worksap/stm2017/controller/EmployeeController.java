package com.worksap.stm2017.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.worksap.stm2017.model.Shift;
import com.worksap.stm2017.model.Type;
import com.worksap.stm2017.model.User;
import com.worksap.stm2017.service.ShiftService;
import com.worksap.stm2017.service.TypeService;
import com.worksap.stm2017.service.UserService;
import com.worksap.stm2017.vo.Menu;


/**
 * 后台管理控制器.
 * 
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {
 
	@Autowired
	private UserService userService;
	
	@Autowired
	private ShiftService shiftService;
	
	@Autowired
	private TypeService typeService;
	
	/**
	 * 获取后台管理主页面
	 * @return
	 */
	@GetMapping
	public ModelAndView listUsers(Model model) {
		List<Menu> list = new ArrayList<>();
		list.add(new Menu("user management", "/users/employee"));
		list.add(new Menu("view shift information", "/shift"));
		list.add(new Menu("view work shift", "/schedule/view"));
		list.add(new Menu("apply for changing shift", "/changeshift/1"));
		list.add(new Menu("agree to change shift", "/changeshift/2"));
		model.addAttribute("list", list);
		return new ModelAndView("employee/index", "model", model);
	}
	
    
}