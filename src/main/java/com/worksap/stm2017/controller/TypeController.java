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
import com.worksap.stm2017.model.Type;
import com.worksap.stm2017.model.User;
import com.worksap.stm2017.service.TypeService;
import com.worksap.stm2017.util.ConstraintViolationExceptionHandler;
import com.worksap.stm2017.vo.Menu;
import com.worksap.stm2017.vo.Response;


/**
 * 后台管理控制器.
 * 
 */
@Controller
@RequestMapping("/type")
public class TypeController {
    @Autowired
    TypeService typeService;
    
	/**
	 * 获取后台管理主页面
	 * @return
	 */
	@GetMapping
	public ModelAndView listTypes(@RequestParam(value = "async", required = false) boolean async,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,Model model) {
		//List<Menu> list = new ArrayList<>();
		//list.add(new Menu("用户管理", "/users"));
		//model.addAttribute("list", list);
		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		Page<Type> page = typeService.listTypes(pageable);		
		List<Type> list = page.getContent(); // 当前所在页面数据列表
		model.addAttribute("page", page);
		model.addAttribute("typeList", list);
		return new ModelAndView(async == true ? "type/typelist :: #mainContainerRepleace" : "type/typelist", "userModel",
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
		model.addAttribute("type", new Type(null));
		return new ModelAndView("type/add", "userModel", model);
	}

    /**
     * 保存或者修改用户
     * @param user
     * @param authorityId
     * @return
     */
    @PostMapping
    public ResponseEntity<Response> saveOrUpateType(Type type) {
    	
		
        try {
        	 
           typeService.saveOrUpateType(type);
        }  catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }

        return ResponseEntity.ok().body(new Response(true, "处理成功", type));
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
			//删除type时，首先要把相关的user的typeid换掉（如果是级联all的话，就是直接删了user了）
			//不过这里暂时注释掉，又报了那个managed flush [Batch update returned unexpected row count from update [0]; actual row count: 0; expected: 1]
			//没考虑事务，以后研究
			/*List<User> list=typeService.getTypeById(id).get().getUsers();
			if(list!=null&&list.size()>0){
				list.forEach(u -> u.setType(typeService.getSecondType()));
			}*/
			typeService.removeType(id);
		} catch (Exception e) {//e.getMessage()
			return ResponseEntity.ok().body(new Response(false, "There may be users belong to this type, you should change users' types. Another possible reason is that there may be shift groups belong to this type."));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功"));
	}
	
	@DeleteMapping(value = "/123/{id}")
	public ResponseEntity<Response> delete123(@PathVariable("id") Long id, Model model) {
		
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
		Optional<Type> type = typeService.getTypeById(id);
		//User user = userService.getUserById(id);
		model.addAttribute("type", type.get());		
		return new ModelAndView("type/edit", "userModel", model);
	}
	

}