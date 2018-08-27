package com.worksap.stm2017.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

import com.worksap.stm2017.model.Shift;
import com.worksap.stm2017.model.ShiftGroup;
import com.worksap.stm2017.model.User;
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
@RequestMapping("/shift")
public class ShiftController {
	@Autowired
    ShiftService shiftService;
	
	@Autowired
    ShiftGroupService shiftGroupService;
	
	@Autowired
    UserService userService;
	/**
	 * 获取后台管理主页面
	 * @return
	 */
	@GetMapping
	public ModelAndView listShifts(@RequestParam(value = "async", required = false) boolean async,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(value = "name", required = false, defaultValue = "") String name,Model model) {
		if(SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal() instanceof String)
		return new ModelAndView("/index", "userModel",model);
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		if(userDetails.getUsername().equals("admin")){
		Page<Shift> page = shiftService.listShiftsByNameLike(name, pageable);
		
		List<Shift> list = page.getContent(); // 当前所在页面数据列表
		model.addAttribute("page", page);
		model.addAttribute("shiftList", list);
		}
		else{
			List<ShiftGroup> shiftgroups=shiftGroupService.listShiftGroups();
			List<Shift> shiftlist=new ArrayList<>();
			for(ShiftGroup sg:shiftgroups){
				if(sg.getType().getId().longValue()==userService.getByUserName(userDetails.getUsername()).getType().getId().longValue()){
					shiftlist.addAll(sg.getShifts());//if a type has more than 1 shift groups
				}
			}
			Page<Shift> page=new PageImpl(shiftlist,pageable,shiftlist.size());
			model.addAttribute("page", page);
			model.addAttribute("shiftList", shiftlist);
		}
		return new ModelAndView(async == true ? "shift/list :: #mainContainerRepleace" : "shift/list", "userModel",
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
		model.addAttribute("shift", new Shift(null,null,null,0,null));
		model.addAttribute("shiftgrouplist",shiftGroupService.listShiftGroups());
		return new ModelAndView("shift/add", "userModel", model);
	}

    /**
     * 保存或者修改
     * @param user
     * @param authorityId
     * @return
     */
    @PostMapping
    public ResponseEntity<Response> saveOrUpateShift(Shift shift) {
    	
        try {        	 
        	//shiftService.saveOrUpateShift(shift);
        	//直接用一方的save来save多方
        	ShiftGroup shiftGroup=shiftGroupService.getShiftGroupById(shift.getShiftgroup().getId()).get();
        	shiftGroup.addShift(shift);
        	shiftGroupService.saveOrUpateShiftGroup(shiftGroup);
        }  catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }

        return ResponseEntity.ok().body(new Response(true, "处理成功", shift));
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
		//多方的删除（存疑）
		try {			
			shiftGroupService.removeShift(id, shiftService.getShiftById(id).get().getShiftgroup().getId());
			shiftService.removeShift(id);
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功"));
	}
	    

	/**
	 * 获取修改班次的界面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "edit/{id}")
	public ModelAndView modifyForm(@PathVariable("id") Long id, Model model) {
		Optional<Shift> shift = shiftService.getShiftById(id);
		model.addAttribute("shift", shift.get());
		model.addAttribute("shiftgrouplist",shiftGroupService.listShiftGroups());
		return new ModelAndView("shift/edit", "userModel", model);
	}
}