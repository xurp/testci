package com.worksap.stm2017.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.worksap.stm2017.kafka.KafkaSender;
import com.worksap.stm2017.model.Shift;
import com.worksap.stm2017.model.Type;
import com.worksap.stm2017.model.User;
import com.worksap.stm2017.mq.MQSender;
import com.worksap.stm2017.mq.SeckillMessage;
import com.worksap.stm2017.service.ShiftService;
import com.worksap.stm2017.service.TypeService;
import com.worksap.stm2017.service.UserService;
import com.worksap.stm2017.vo.Menu;



/**
 * 后台管理控制器.
 * 
 */
@Controller
@RequestMapping("/admins")
public class AdminController {
 
	@Autowired
	private UserService userService;
	
	@Autowired
	private ShiftService shiftService;
	
	@Autowired
	private TypeService typeService;
	
	@Autowired
    private MQSender mqSender;
	
	@Autowired
	private KafkaSender kafkaSender;
	
	public Logger logger=LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 获取后台管理主页面
	 * @return
	 */
	@GetMapping
	public ModelAndView listUsers(Model model) {
		//rabbitmq
		logger.info("ADMIN日志");
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal(); 
		mqSender.sendSeckillMessage(new SeckillMessage(1L,userDetails.getUsername()));
		mqSender.send();
		//kafka
		kafkaSender.send();
		List<Menu> list = new ArrayList<>();
		list.add(new Menu("user management", "/users"));
		list.add(new Menu("set type(user group)", "/type"));
		list.add(new Menu("set shift group", "/shiftgroup"));
		list.add(new Menu("set shift", "/shift"));
		list.add(new Menu("set labor law", "/laborlaw"));
		list.add(new Menu("begin to schedule", "/schedule"));
		list.add(new Menu("view work shift", "/schedule/view"));
		list.add(new Menu("change shift", "/changeshift/3"));
		model.addAttribute("list", list);
		return new ModelAndView("admins/index", "model", model);
	}
	
    
}