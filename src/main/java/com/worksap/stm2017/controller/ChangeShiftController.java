package com.worksap.stm2017.controller;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.worksap.stm2017.model.ChangeShift;
import com.worksap.stm2017.model.Shift;
import com.worksap.stm2017.model.ShiftGroup;
import com.worksap.stm2017.model.User;
import com.worksap.stm2017.service.ChangeShiftService;
import com.worksap.stm2017.service.SaveScheduleService;
import com.worksap.stm2017.service.ShiftGroupService;
import com.worksap.stm2017.service.ShiftService;
import com.worksap.stm2017.service.UserService;
import com.worksap.stm2017.util.ConstraintViolationExceptionHandler;
import com.worksap.stm2017.vo.Menu;
import com.worksap.stm2017.vo.Response;


/**
 * 后台管理控制器.
 * 
 */
@Controller
@RequestMapping("/changeshift")
public class ChangeShiftController {
	@Autowired
    ShiftService shiftService;
	
	@Autowired
    ShiftGroupService shiftGroupService;
	
	@Autowired
    ChangeShiftService changeShiftService;
	
	@Autowired
    SaveScheduleService saveScheduleService;
	
	@Autowired
    UserService userService;
	/**
	 * 获取后台管理主页面
	 * @return
	 */
	@GetMapping
	public ModelAndView listChangeShifts(@RequestParam(value = "async", required = false) boolean async,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(value = "arg", required = false, defaultValue = "") String arg,Model model) {
		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		if(SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal() instanceof String)
		return new ModelAndView("/index", "userModel",model);
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		if(userDetails.getUsername().equals("admin")){
			Page<ChangeShift> page = changeShiftService.findAll(pageable,arg);
			List<ChangeShift> list = page.getContent(); // 当前所在页面数据列表
			model.addAttribute("page", page);
			model.addAttribute("changeshiftList", list);
		}
		/*else{
			Page<ChangeShift> page = changeShiftService.findByUseridOrChanged(userService.getByUserName(userDetails.getUsername()).getId(),userService.getByUserName(userDetails.getUsername()).getId(),pageable,arg);
			List<ChangeShift> list = page.getContent(); // 当前所在页面数据列表
			model.addAttribute("page", page);
			model.addAttribute("changeshiftList", list);
		}*/
		
		return new ModelAndView(async == true ? "changeshift/list :: #mainContainerRepleace" : "changeshift/list", "userModel",
				model);
	}
	
	@GetMapping(value = "/{id}")
	public ModelAndView listChangeShifts(@PathVariable("id") Long id,@RequestParam(value = "async", required = false) boolean async,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(value = "arg", required = false, defaultValue = "all") String arg,Model model) {
		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		if(SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal() instanceof String)
		return new ModelAndView("/index", "userModel",model);
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		if(userDetails.getUsername().equals("admin")){
			Page<ChangeShift> page = changeShiftService.findAll(pageable,arg);
			List<ChangeShift> list = page.getContent(); // 当前所在页面数据列表
			model.addAttribute("page", page);
			model.addAttribute("changeshiftList", list);
		}
		if(!userDetails.getUsername().equals("admin")){
			System.out.println(userDetails.getUsername());
			Page<ChangeShift> page = changeShiftService.findByUseridOrChanged(userService.getByUserName(userDetails.getUsername()).getId(),userService.getByUserName(userDetails.getUsername()).getId(),pageable,arg,id);
			List<ChangeShift> list = page.getContent(); // 当前所在页面数据列表
			model.addAttribute("page", page);
			model.addAttribute("changeshiftList", list);
		}
		model.addAttribute("changetype", id.longValue()+"");
		List<User> userlist=userService.listUsersByNameLike("",PageRequest.of(0,1000)).getContent();
		Map<String,String> namemap=new HashMap<>();
		for(User u:userlist){
			namemap.put(u.getId()+"", u.getName());
		}
		model.addAttribute("namemap",namemap);
		return new ModelAndView(async == true ? "changeshift/list :: #mainContainerRepleace" : "changeshift/list", "userModel",
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
		if(SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal() instanceof String)
		return new ModelAndView("/index", "userModel",model);
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		model.addAttribute("changeshift", new ChangeShift(userService.getByUserName(userDetails.getUsername()).getId(), null,userService.getByUserName(userDetails.getUsername()).getUsername(), 
				null,"false", "false",null,null,null,null,null,null));
		List<User> userlist=userService.getByUserName(userDetails.getUsername()).getType().getUsers();
		List<User> userlistminusone=new ArrayList<>();
		for(User u:userlist){
			if(u.getId().longValue()!=userService.getByUserName(userDetails.getUsername()).getId().longValue())
				userlistminusone.add(u);
		}
		model.addAttribute("userlist",userlistminusone);
		List<ShiftGroup> shiftgroups=shiftGroupService.listShiftGroups();
		List<Shift> shiftlist=new ArrayList<>();
		for(ShiftGroup sg:shiftgroups){
			if(sg.getType().getId().longValue()==userService.getByUserName(userDetails.getUsername()).getType().getId().longValue()){
				shiftlist.addAll(sg.getShifts());//if a type has more than 1 shift groups
			}
		}
		model.addAttribute("shiftlist",shiftlist);
		model.addAttribute("filelist",saveScheduleService.listSaveSchedule(PageRequest.of(0, 1000)).getContent());
		return new ModelAndView("changeshift/add", "userModel", model);
	}
	@PostMapping("/agree")
    public ResponseEntity<Response> agree(ChangeShift changeshift) {
    	
        try {        	 
        	changeshift.setIsAgreed("true");
        	changeShiftService.saveOrUpateChangeShift(changeshift);
        }  catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }

        return ResponseEntity.ok().body(new Response(true, "处理成功", changeshift));
    }
	@PostMapping("/approve")
    public ResponseEntity<Response> approve(ChangeShift changeshift) {
    	
        try {        	 
        	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        	changeshift.setIsChanged("true");        	
        	changeshift.setOperTime(new Timestamp(new Date().getTime()));
        	changeShiftService.saveOrUpateChangeShift(changeshift);
        }  catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }

        return ResponseEntity.ok().body(new Response(true, "处理成功", changeshift));
    }
	
	@PostMapping("/refuse")
    public ResponseEntity<Response> refuse(ChangeShift changeshift) {
    	
        try {        	 
        	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        	changeshift.setIsChanged("refuse");        	
        	changeshift.setOperTime(new Timestamp(new Date().getTime()));
        	changeShiftService.saveOrUpateChangeShift(changeshift);
        }  catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }

        return ResponseEntity.ok().body(new Response(true, "处理成功", changeshift));
    }

    /**
     * 保存或者修改
     * @param user
     * @param authorityId
     * @return
     */
    @PostMapping
    public ResponseEntity<Response> saveOrUpateShift(ChangeShift changeshift) {
    	
        try {        	 
        	changeshift.setChangename(userService.getUserById(changeshift.getChangeid()).get().getUsername());
        	changeShiftService.saveOrUpateChangeShift(changeshift);
        }  catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }

        return ResponseEntity.ok().body(new Response(true, "处理成功", changeshift));
    }

	/**
	 * 删除
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response> delete(@PathVariable("id") Long id, Model model) {
		try {			
			changeShiftService.removeChangeShift(id);
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功"));
	}
	    

	/**
	 * 
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "edit/{id}")
	public ModelAndView modifyForm(@PathVariable("id") Long id, Model model) {
		if(SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal() instanceof String)
		return new ModelAndView("/index", "userModel",model);
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		Optional<ChangeShift> changeshift = changeShiftService.getChangeShiftById(id);
		model.addAttribute("changeshift", changeshift.get());
		model.addAttribute("userlist",userService.getByUserName(userDetails.getUsername()).getType().getUsers());
		List<ShiftGroup> shiftgroups=shiftGroupService.listShiftGroups();
		List<Shift> shiftlist=new ArrayList<>();
		for(ShiftGroup sg:shiftgroups){
			if(sg.getType().getId().longValue()==userService.getByUserName(userDetails.getUsername()).getType().getId().longValue()){
				shiftlist.addAll(sg.getShifts());
			}
		}
		model.addAttribute("shiftlist",shiftlist);
		model.addAttribute("filelist",saveScheduleService.listSaveSchedule(PageRequest.of(0, 1000)).getContent());
		List<User> userlist=userService.listUsersByNameLike("",PageRequest.of(0,1000)).getContent();
		Map<String,String> namemap=new HashMap<>();
		for(User u:userlist){
			namemap.put(u.getId()+"", u.getName());
		}
		model.addAttribute("namemap",namemap);
		return new ModelAndView("changeshift/edit", "userModel", model);
	}
}