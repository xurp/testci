package com.worksap.stm2017.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.model.ShiftGroup;
import com.worksap.stm2017.model.Type;
import com.worksap.stm2017.model.User;
import com.worksap.stm2017.service.ShiftGroupService;
import com.worksap.stm2017.service.TypeService;
import com.worksap.stm2017.util.ConstraintViolationExceptionHandler;
import com.worksap.stm2017.vo.Menu;
import com.worksap.stm2017.vo.Response;


/**
 * 后台管理控制器.
 * 
 */
@Controller
@RequestMapping("/shiftgroup")
public class ShiftGroupController {
    @Autowired
    ShiftGroupService shiftGroupService;
    
    @Autowired
    TypeService typeService;
    
	/**
	 * 获取后台管理主页面
	 * @return
	 */
	@GetMapping
	public ModelAndView listShiftGroups(@RequestParam(value = "async", required = false) boolean async,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,Model model) {
		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		Page<ShiftGroup> page = shiftGroupService.findAll(pageable);		
		List<ShiftGroup> list = page.getContent(); // 当前所在页面数据列表
		model.addAttribute("page", page);
		model.addAttribute("shiftGroupList", list);
		return new ModelAndView(async == true ? "shiftGroup/list :: #mainContainerRepleace" : "shiftGroup/list", "userModel",
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
		model.addAttribute("shiftGroup", new ShiftGroup(null));
		model.addAttribute("typelist",typeService.listTypes());
		return new ModelAndView("shiftGroup/add", "userModel", model);
	}

    /**
     * 保存或者修改
     * @param user
     * @param authorityId
     * @return
     */
    @PostMapping
    public ResponseEntity<Response> saveOrUpateShiftGroup(ShiftGroup shiftGroup) {
    	
		
        try {        	 
        	shiftGroupService.saveOrUpateShiftGroup(shiftGroup);
        }  catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }

        return ResponseEntity.ok().body(new Response(true, "处理成功", shiftGroup));
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
			shiftGroupService.removeShiftGroup(id);
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功"));
	}
	    

	/**
	 * 获取修改班次组的界面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping(value = "edit/{id}")
	public ModelAndView modifyForm(@PathVariable("id") Long id, Model model) {
		Optional<ShiftGroup> type = shiftGroupService.getShiftGroupById(id);
		model.addAttribute("shiftGroup", type.get());		
		model.addAttribute("typelist",typeService.listTypes());
		return new ModelAndView("shiftGroup/edit", "userModel", model);
	}
	

}