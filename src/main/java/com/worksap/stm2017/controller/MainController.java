package com.worksap.stm2017.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.worksap.stm2017.model.Authority;
import com.worksap.stm2017.model.GoodsInfo;
import com.worksap.stm2017.model.Person;
import com.worksap.stm2017.model.User;
import com.worksap.stm2017.repository.GoodsRepository;
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
		save();
		Pageable pageable = PageRequest.of(0, 100);
		//注意：这里查商品，即使是商1也能查出来，因为是打分的
    	List<GoodsInfo> list=goodsRepository.findByNameLike("商品", pageable).getContent();
    	model.addAttribute("goodslist", list);
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
    
    //ES测试
    @Autowired
    private GoodsRepository goodsRepository;

    //http://localhost:8888/save
    @GetMapping("save")
    public String save(){
        GoodsInfo goodsInfo = new GoodsInfo(System.currentTimeMillis(),
                "商1"+System.currentTimeMillis(),"这是一个测试商1");
        goodsRepository.save(goodsInfo);
        return "index";
    }

    //http://localhost:8888/delete?id=1525415333329
    @GetMapping("delete")
    public String delete(long id){
        goodsRepository.deleteById(id);
        return "index";
    }

    //http://localhost:8888/update?id=1525417362754&name=修改&description=修改
    @GetMapping("update")
    public String update(long id,String name,String description){
        GoodsInfo goodsInfo = new GoodsInfo(id,
                name,description);
        goodsRepository.save(goodsInfo);
        return "index";
    }

    //http://localhost:8888/getOne?id=1525417362754
    @GetMapping("getOne")
    public GoodsInfo getOne(long id){
        GoodsInfo goodsInfo = goodsRepository.findById(id).get();
        return goodsInfo;
    }


}
