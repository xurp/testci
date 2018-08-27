package com.worksap.stm2017.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.worksap.stm2017.model.LaborLaw;
import com.worksap.stm2017.model.Type;
import com.worksap.stm2017.service.LaborLawService;
import com.worksap.stm2017.util.ConstraintViolationExceptionHandler;
import com.worksap.stm2017.vo.Response;

@Controller
@RequestMapping("/laborlaw")
public class LaborLawController {
	@Autowired
	LaborLawService laborLawService;
	
	@GetMapping
	public ModelAndView list(@RequestParam(value = "async", required = false) boolean async,Model model) {
		LaborLaw lb=laborLawService.getLaborLawById(1L).get();
		model.addAttribute("lb", lb);
		//return new ModelAndView(async == true ? "laborlaw/edit :: #mainContainerRepleace" : "laborlaw/edit", "userModel",model);
		return new ModelAndView("laborlaw/edit", "userModel", model);
	}
	@PostMapping
    public ResponseEntity<Response> saveOrUpateType(LaborLaw laborLaw) {  
        try {
        	laborLawService.saveOrUpateLaborLaw(laborLaw);
        }  catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }

        return ResponseEntity.ok().body(new Response(true, "处理成功"));
    }
}
