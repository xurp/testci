package com.worksap.stm2017.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import com.worksap.stm2017.model.SetDates;
import com.worksap.stm2017.model.Shift;
import com.worksap.stm2017.model.ShiftForSchedule;
import com.worksap.stm2017.model.ShiftGroup;
import com.worksap.stm2017.model.User;
import com.worksap.stm2017.model.LaborLaw;
import com.worksap.stm2017.model.SaveSchedule;
import com.worksap.stm2017.model.WorkingHoursCount;
import com.worksap.stm2017.service.ShiftGroupService;
import com.worksap.stm2017.service.ShiftService;
import com.worksap.stm2017.service.LaborLawService;
import com.worksap.stm2017.service.SaveScheduleService;
import com.worksap.stm2017.util.ConstraintViolationExceptionHandler;
import com.worksap.stm2017.vo.Menu;
import com.worksap.stm2017.vo.Response;

/**
 * 后台管理控制器.
 * 
 */
// @SessionAttributes(value={"starttime","endtime","excludesDates"})
@Controller
@RequestMapping("/schedule")
public class ScheduleController {
	@Autowired
	ShiftService shiftService;

	@Autowired
	ShiftGroupService shiftGroupService;

	@Autowired
	SaveScheduleService saveScheduleService;
	
    @Autowired
    LaborLawService laborLawService;
	/**
	 * 获取后台管理主页面
	 * 
	 * @return
	 */
	@GetMapping
	public ModelAndView beginToSetDate(@RequestParam(value = "async", required = false) boolean async, Model model) {
		model.addAttribute("setDates", new SetDates(null, null, null));
		model.addAttribute("shiftGroups", shiftGroupService.listShiftGroups());
		model.addAttribute("errormessage", "");
		return new ModelAndView(async == true ? "schedule/setdate :: #mainContainerRepleace" : "schedule/setdate",
				"userModel", model);
	}

	@GetMapping("/view")
	public ModelAndView viewWorkShift(@RequestParam(value = "async", required = false) boolean async,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize, Model model) {
		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		Page<SaveSchedule> page= saveScheduleService.listSaveSchedule(pageable);
		List<SaveSchedule> list=page.getContent();
        model.addAttribute("page", page);
		model.addAttribute("workshiftList", list);
		return new ModelAndView(async == true ? "schedule/scheduleList :: #mainContainerRepleace" : "schedule/scheduleList",
				"userModel", model);
	}

	@GetMapping("/view/{id}")
	public ModelAndView viewById(@PathVariable("id") Long id,HttpSession session,
			@RequestParam(value = "async", required = false) boolean async, Model model) {
		Optional<SaveSchedule> opt= saveScheduleService.getSaveScheduleById(id);
		if(opt.isPresent()){
		    SaveSchedule s=opt.get();
			Map<String, Map<String, List<String>>> shiftview = new TreeMap<>();// <日期<班次名,人员名列表>
		    Map<String, Map<String, List<String>>> peopleview = new TreeMap<>();// <日期<人名,班次列表>
			String[] dates=s.getDates().split(",");
			String[] shifts=s.getShifts().split(",");
			String[] saves=s.getSaves().split(",");
			
			for(int i=0;i<dates.length;i++){
                Map<String, List<String>> shiftmap=new TreeMap<>();
			    for(int j=0;j<shifts.length;j++){
					    List<String> ulist=new ArrayList<>();
					    if(i*shifts.length+j>=saves.length){//sometimes the last of save is ,,,,,,,,
					    	break;
					    }
						String usersString=saves[i*shifts.length+j];
						if(!usersString.equals("")){//存在某天某个班没人的情况，因为这天这个班不参与排班
						String[] users=usersString.replaceAll(String.valueOf((char)160), " ").split("\\s+ ");
						for(int k=0;k<users.length;k++){
							//if(!users[k].equals("")) last may be empty?
							System.out.print(users[k]+";");
						    ulist.add(users[k]);
						}
						shiftmap.put(shifts[j],ulist);
						}
				}
				shiftview.put(dates[i],shiftmap);
			}
			for (Entry<String, Map<String, List<String>>> e : shiftview.entrySet()) {
			String datename = e.getKey();
			Map<String, List<String>> usershiftmap = e.getValue();
			Map<String, List<String>> reversemap = new TreeMap<>();
			for (Entry<String, List<String>> ex : usershiftmap.entrySet()) {
				String shiftname = ex.getKey();
				List<String> userlist1 = ex.getValue();
				for (String u : userlist1) {
					List<String> shiftlist1 = reversemap.getOrDefault(u, new ArrayList<String>());
					shiftlist1.add(shiftname);
					reversemap.put(u, shiftlist1);
				}
			}
			peopleview.put(datename, reversemap);
		}
		String group=s.getSelectedshiftgroup();
		String[] groups=group.split(",");
		List<Shift> shiftlist = shiftService.findShiftByGroupIds(groups);
		//count
		Map<String, List<String>> shiftdaymap=new HashMap<>();//TreeMap?
		for (Entry<String, Map<String, List<String>>> e : shiftview.entrySet()) {
			String datekey=e.getKey();
			Map<String, List<String>> shiftvalue = e.getValue();
			for (Entry<String, List<String>> ex : shiftvalue.entrySet()) {
				List<String> dlist=shiftdaymap.getOrDefault(ex.getKey(), new ArrayList<String>());
				dlist.add(datekey);
				shiftdaymap.put(ex.getKey(),dlist);
			}
		}
		int totalPeople = 0;
		for (int i = 0; i < groups.length; i++) {
			List<Shift> thegroupshiftlist = shiftGroupService.getShiftGroupById(Long.parseLong(groups[i])).get().getShifts();
			//sales has employees, but no shifts. so if sales's group is selected, these employees should not be included
			if(thegroupshiftlist!=null&&thegroupshiftlist.size()>0){
			Optional<ShiftGroup> sgroup = shiftGroupService.getShiftGroupById(Long.parseLong(groups[i]));
			if (sgroup.isPresent())
				totalPeople += sgroup.get().getType().getUsers().size();
		}
			}
				//add user
		List<User> totaluserlist=new ArrayList<>();
		for(int i=0;i<groups.length;i++){
		List<User> userlist = shiftGroupService.getShiftGroupById(Long.parseLong(groups[i])).get().getType().getUsers();
		totaluserlist.addAll(userlist);
		}
		List<WorkingHoursCount> usertotalhour = new ArrayList<>();
		for(int i=0;i<totaluserlist.size();i++){
			usertotalhour.add(new WorkingHoursCount(totaluserlist.get(i).getName(),0.0,0));
		}
		for (Entry<String, Map<String, List<String>>> e : peopleview.entrySet()) {
			for (Entry<String,List<String>> ex : e.getValue().entrySet()){
				int thisuser=0;
				for(int i=0;i<usertotalhour.size();i++){
					if(usertotalhour.get(i).getName().equals(ex.getKey())){
						thisuser=i;
						break;
					}
				}
				List<String> usersshift=ex.getValue();
				   for(String shiftname:usersshift){
					   System.out.println(shiftname);
					   Shift shift=shiftService.getShiftByName(shiftname).get();
					   String shiftendtime = shift.getEndtime();
						String[] endarr = shiftendtime.split("\\:");
						String shiftstarttime = shift.getStarttime();
						String[] startarr = shiftstarttime.split("\\:");
						int endhour = Integer.parseInt(endarr[0]);
						int endminute = Integer.parseInt(endarr[1]);
						int starthour = Integer.parseInt(startarr[0]);
						int startminute = Integer.parseInt(startarr[1]);
						double workhour = 0.0;
						if (endhour > starthour) {
							int starthm = starthour * 60 + startminute;
							int endhm = endhour * 60 + endminute;
							workhour = (endhm - starthm) / 60.0;
						} else {
							int starthm = starthour * 60 + startminute;
							int endhm = (endhour + 24) * 60 + endminute;
							workhour = (endhm - starthm) / 60.0;
						}
						workhour = (double) Math.round(workhour * 10) / 10;
					   WorkingHoursCount u=usertotalhour.get(thisuser);
					   u.setHours(u.getHours()+workhour);
					   u.setShift(u.getShift()+1);
				   }
			}
		}
		double totalHours=0.0;
		int totalShifts=0;
		for(WorkingHoursCount u:usertotalhour){
			totalHours+=u.getHours();
			totalShifts+=u.getShift();
		}
		
		double shiftsperemp = totalShifts / (totalPeople + 0.0);
		shiftsperemp = (double) Math.round(shiftsperemp * 10) / 10;
		double shiftsperempday = shiftsperemp / shiftview.size();
		shiftsperempday = (double) Math.round(shiftsperempday * 10) / 10;
		System.out.println(totalShifts + " " + shiftsperemp + " " + shiftsperempday);
		double hoursperemp = totalHours / (totalPeople + 0.0);
		hoursperemp = (double) Math.round(hoursperemp * 10) / 10;
		double hoursperempday = hoursperemp / shiftview.size();
		hoursperempday = (double) Math.round(hoursperempday * 10) / 10;
        model.addAttribute("shiftlist", shiftlist);
		model.addAttribute("userlist", totaluserlist);
		model.addAttribute("shiftview", shiftview);
		model.addAttribute("peopleview", peopleview);
		model.addAttribute("totalHours", totalHours);
		model.addAttribute("hoursperemp", hoursperemp);
		model.addAttribute("hoursperempday", hoursperempday);
		model.addAttribute("totalShifts",totalShifts);
		model.addAttribute("shiftsperemp", shiftsperemp);
		model.addAttribute("shiftsperempday", shiftsperempday);
		model.addAttribute("userhour", usertotalhour);
		
		LaborLaw lb=laborLawService.getLaborLawById(1L).get();
		if(lb.getUse().equals("true")){
			double oneweekhour=hoursperemp*7.0/shiftview.size();
			oneweekhour=(double) (Math.round(oneweekhour) * 10) / 10;
			if(oneweekhour-lb.getHour()>0){
				model.addAttribute("laborlaw", "Average employee working hours per week: " +oneweekhour+". You violates the labor law("
			+((double) (Math.round(lb.getHour()) * 10) / 10)+"). Maybe you should add employees to work.");
			}
			else{
				model.addAttribute("laborlaw", "false");
			}
			
		}
		else{
			model.addAttribute("laborlaw", "false");
		}
		
		}
		model.addAttribute("saveoredit", "edit");
		model.addAttribute("editid", id);
		Object obj=SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		if(obj!=null&&!(obj instanceof String)){
		UserDetails userDetails = (UserDetails) obj;
		model.addAttribute("getusername", userDetails.getUsername());		
		}
		return new ModelAndView("schedule/viewWorkShift", "userModel", model);
	}
	
	@GetMapping("/edit/{id}")
	public ModelAndView editById(@PathVariable("id") Long id,HttpSession session,
			@RequestParam(value = "async", required = false) boolean async, Model model) {
		Optional<SaveSchedule> opt= saveScheduleService.getSaveScheduleById(id);
		if(opt.isPresent()){
		    SaveSchedule s=opt.get();
		    model.addAttribute("filename",s.getName() );
		    model.addAttribute("groups",s.getSelectedshiftgroup() );
			Map<String, Map<String, List<String>>> shiftview = new TreeMap<>();// <日期<班次名,人员名列表>
		    Map<String, Map<String, List<String>>> peopleview = new TreeMap<>();// <日期<人名,班次列表>
			String[] dates=s.getDates().split(",");
			String[] shifts=s.getShifts().split(",");
			String[] saves=s.getSaves().split(",");
			
			for(int i=0;i<dates.length;i++){
                Map<String, List<String>> shiftmap=new TreeMap<>();
			    for(int j=0;j<shifts.length;j++){
					    List<String> ulist=new ArrayList<>();
					    if(i*shifts.length+j>=saves.length){//sometimes the last of save is ,,,,,,,,
					    	break;
					    }
						String usersString=saves[i*shifts.length+j];
						if(!usersString.equals("")){//存在某天某个班没人的情况，因为这天这个班不参与排班
						String[] users=usersString.replaceAll(String.valueOf((char)160), " ").split("\\s+ ");
						for(int k=0;k<users.length;k++){
							//if(!users[k].equals("")) last may be empty?
						    ulist.add(users[k]);
						}
						shiftmap.put(shifts[j],ulist);
						}
				}
				shiftview.put(dates[i],shiftmap);
			}
			for (Entry<String, Map<String, List<String>>> e : shiftview.entrySet()) {
			String datename = e.getKey();
			Map<String, List<String>> usershiftmap = e.getValue();
			Map<String, List<String>> reversemap = new TreeMap<>();
			for (Entry<String, List<String>> ex : usershiftmap.entrySet()) {
				String shiftname = ex.getKey();
				List<String> userlist1 = ex.getValue();
				for (String u : userlist1) {
					List<String> shiftlist1 = reversemap.getOrDefault(u, new ArrayList<String>());
					shiftlist1.add(shiftname);
					reversemap.put(u, shiftlist1);
				}
			}
			peopleview.put(datename, reversemap);
		}
		String group=s.getSelectedshiftgroup();
		String[] groups=group.split(",");
		List<Shift> shiftlist = shiftService.findShiftByGroupIds(groups);
		//count
		Map<String, List<String>> shiftdaymap=new HashMap<>();//TreeMap?
		for (Entry<String, Map<String, List<String>>> e : shiftview.entrySet()) {
			String datekey=e.getKey();
			Map<String, List<String>> shiftvalue = e.getValue();
			for (Entry<String, List<String>> ex : shiftvalue.entrySet()) {
				List<String> dlist=shiftdaymap.getOrDefault(ex.getKey(), new ArrayList<String>());
				dlist.add(datekey);
				shiftdaymap.put(ex.getKey(),dlist);
			}
		}
		
		//add user
		List<User> totaluserlist=new ArrayList<>();
		Map<String,List<User>> shiftgroupusersmap=new TreeMap<>();
		for(int i=0;i<groups.length;i++){
		List<User> userlist = shiftGroupService.getShiftGroupById(Long.parseLong(groups[i])).get().getType().getUsers();
		totaluserlist.addAll(userlist);
		userlist.add(0,new User("",null,null,null,null));
		shiftgroupusersmap.put(shiftGroupService.getShiftGroupById(Long.parseLong(groups[i])).get().getName(), userlist);
		
		}
		model.addAttribute("shiftgroupusersmap", shiftgroupusersmap);
        model.addAttribute("shiftlist", shiftlist);
		model.addAttribute("userlist", totaluserlist);
		model.addAttribute("shiftview", shiftview);
		model.addAttribute("peopleview", peopleview);
		
		
		}
		model.addAttribute("editid", id);
		model.addAttribute("saveoredit", "edit");
		return new ModelAndView("schedule/editWorkShift", "userModel", model);
	}
	
	@PostMapping("/editWorkShift/{id}")
	public ResponseEntity<Response> saveOrUpateWorkShift(@PathVariable("id") Long id,String[] dates,String[] shifts,String[] saves,String filename,
			String groups,HttpSession session) {
		

		if(filename==null||filename.equals(""))
          return ResponseEntity.ok().body(new Response(false, "please input file name"));
		StringBuilder datesString=new StringBuilder();
		StringBuilder shiftsString=new StringBuilder();
		StringBuilder savesString=new StringBuilder();
		for(int i=0;i<dates.length;i++)
          datesString.append((dates[i].split(" "))[0]).append(",");
        for(int i=0;i<shifts.length;i++)
          shiftsString.append(shifts[i]).append(",");
		for(int i=0;i<saves.length;i++){
		  String[] savesarr=saves[i].split(",");
		  if(i==0) System.out.println(savesarr.length);
		  if(savesarr.length<=1)
          savesString.append(",");
		  else{
			  StringBuilder sb=new StringBuilder();
			  for(int j=1;j<savesarr.length;j++){
				  if(!savesarr[j].equals(""))//delete user
				  sb.append(savesarr[j]).append("  ");
			  }
			  savesString.append(sb).append(",");
		  }
		}
		System.out.println(groups);
		System.out.println(datesString);
		System.out.println(shiftsString);
		System.out.println(savesString);
        //in usercontroller, saveupdate use th:object=user. Here, should find by id then save
		
		try{
			Optional<SaveSchedule> a=saveScheduleService.getSaveScheduleById(id);
			if(a.isPresent()){
				SaveSchedule newsave=a.get();
				if(newsave.getName().equals(filename)){
					newsave.setDates(datesString.toString());
					newsave.setSaves(savesString.toString());
					newsave.setSelectedshiftgroup(groups);
					newsave.setShifts(shiftsString.toString());
					saveScheduleService.saveOrUpateSaveSchedule(newsave);
				}
				else
        saveScheduleService.saveOrUpateSaveSchedule(new SaveSchedule(filename,datesString.toString(),shiftsString.toString(),savesString.toString(),groups));
			}
			else
				return ResponseEntity.ok().body(new Response(false,"save error"));
		}
		catch(Exception ex){
		return ResponseEntity.ok().body(new Response(false,ex.toString()));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功"));
		//,,,,,boxer7;boxer10;boxer8;boxer9;boxer1;boxer2;,boxer2;boxer1;boxer7;boxer10;boxer8;boxer9;,boxer4;boxer3;boxer2;boxer1;boxer7;boxer10;,zhangsan;,lisi;,boxer7;boxer8;boxer9;,boxer1;boxer2;boxer3;,boxer8;boxer9;boxer1;,,,,,,lisi;,zhangsan;,boxer10;boxer9;boxer8;,boxer4;boxer5;boxer6;,boxer2;boxer3;boxer4;,,,,,,wangwu;,zhangsan;,boxer10;boxer7;boxer6;,boxer9;boxer8;boxer10;,boxer5;boxer6;boxer9;,,,,,,wangwu;,lisi;,,,,boxer1;boxer2;boxer3;,boxer4;boxer5;boxer6;,,,,lisi;,wangwu;,boxer5;boxer4;boxer3;,boxer7;boxer6;boxer5;,boxer8;boxer10;boxer7;,,,,,,zhangsan;,wangwu;,,,,,,boxer3;boxer4;boxer5;boxer6;boxer9;boxer8;,boxer1;boxer2;boxer3;boxer4;boxer5;boxer6;,boxer8;boxer9;boxer1;boxer2;boxer3;boxer4;,zhangsan;,lisi;,,,,,,boxer10;boxer7;boxer6;boxer5;boxer4;boxer3;,boxer9;boxer8;boxer10;boxer7;boxer6;boxer5;,boxer5;boxer6;boxer9;boxer8;boxer10;boxer7;,lisi;,zhangsan;,boxer2;boxer1;boxer7;,boxer4;boxer3;boxer2;,boxer6;boxer5;boxer4;,,,,,,wangwu;,zhangsan;,boxer10;boxer8;boxer9;,boxer1;boxer7;boxer10;,boxer3;boxer2;boxer1;,,,,,,wangwu;,lisi;,
	}
    
	/**
	 * 保存或者修改
	 * 
	 * @param user
	 * @param authorityId
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Response> saveOrUpateSetDates1(SetDates setDates, String[] selectedshiftgroup,
			HttpSession session) {
		System.out.println(setDates.getExcludeDates());
		session.setAttribute("starttime", setDates.getStarttime());
		session.setAttribute("endtime", setDates.getEndtime());
		session.setAttribute("excludesDates", setDates.getExcludeDates());
		session.setAttribute("selectedshiftgroup", selectedshiftgroup);
		return ResponseEntity.ok().body(new Response(true, "处理成功"));
	}

	@GetMapping("/setShiftDates")
	public ModelAndView saveOrUpateSetDates(HttpSession session,
			@RequestParam(value = "async", required = false) boolean async, Model model) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		String[] selectedshiftgroup = (String[]) session.getAttribute("selectedshiftgroup");
		String starttime = (String) session.getAttribute("starttime");
		String endtime = (String) session.getAttribute("endtime");
		String excludesDates = (String) session.getAttribute("excludesDates");
		String[] exdates = excludesDates.split(",");
		if (exdates == null || exdates.length == 0)
			exdates = excludesDates.split("，");
		HashSet<String> set = new HashSet<String>();
		for (String d : exdates) {
			set.add(d);
		}
		List<String> shiftdates = new ArrayList<>();
		// List<Shift> shiftlist=shiftService.findAll(PageRequest.of(0,
		// 100)).getContent();
		List<Shift> shiftlist = shiftService.findShiftByGroupIds(selectedshiftgroup);
		try {
			Date startdate = format.parse(starttime);
			Date enddate = format.parse(endtime);
			Date[] exdate = new Date[exdates.length];
			String err="";
			if(enddate.before(startdate)){
				err+="ERROR. end date before start date. ";
			// 提示错误,例如格式不对，除去时间不在起始范围内等等,starttime>endtime
			// 目前问题：,与，如果输成2018-9-5不行
				model.addAttribute("setDates", new SetDates(null, null, null));
				model.addAttribute("shiftGroups", shiftGroupService.listShiftGroups());
				model.addAttribute("errormessage", err);
				return new ModelAndView("schedule/setdate",
						"userModel", model);
			}
			Date tomorrow = startdate;
			while (tomorrow.before(enddate)) {
				String f = format.format(tomorrow);
				if (!set.contains(f))
					shiftdates.add(f);
				c.setTime(tomorrow);
				c.add(Calendar.DAY_OF_MONTH, 1);
				tomorrow = c.getTime();
			}
			if (!set.contains(format.format(tomorrow))) {
				shiftdates.add(format.format(tomorrow));
			}

		} catch (Exception ex) {

		}
		session.setAttribute("shiftdates", shiftdates);
		model.addAttribute("shiftlist", shiftlist);
		model.addAttribute("shiftdates", shiftdates);
		return new ModelAndView("schedule/setShiftDates", "userModel", model);
		// return new ModelAndView(async == true ? "schedule/setShiftDates ::
		// #mainContainerRepleace" : "schedule/setShiftDates",
		// "userModel",model);

	}

	@PostMapping("/saveSchedule")
	public ResponseEntity<Response> saveSchedule(String[] dates, 
		String[] shifts,String[] saves,String filename,HttpSession session) {
		//2018-08-19 2018-08-20 size=a
		//a b c size=b
		//a;b;c; c; size=a*b
		List<Long> group=(List<Long>) session.getAttribute("realshiftgroup");
		String groups="";
		for(int i=0;i<group.size();i++){
		    groups+=group.get(i)+",";
		}

		if(filename==null||filename.equals(""))
          return ResponseEntity.ok().body(new Response(false, "please input file name"));
		StringBuilder datesString=new StringBuilder();
		StringBuilder shiftsString=new StringBuilder();
		StringBuilder savesString=new StringBuilder();
		for(int i=0;i<dates.length;i++)
          datesString.append((dates[i].split(" "))[0]).append(",");
        for(int i=0;i<shifts.length;i++)
          shiftsString.append(shifts[i]).append(",");
		for(int i=0;i<saves.length;i++)
          savesString.append(saves[i]).append(",");
		System.out.println(savesString);
        SaveSchedule a=new SaveSchedule(filename,datesString.toString(),shiftsString.toString(),savesString.toString(),groups);
		try{
        saveScheduleService.saveOrUpateSaveSchedule(a);
		}
		catch(Exception ex){
		return ResponseEntity.ok().body(new Response(false,ex.toString()));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功"));
	}

	@PostMapping("/beginSchedule")
	public ResponseEntity<Response> beginSchedule(String dates, HttpSession session) {
		System.out.println("begin");
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		String starttime = (String) session.getAttribute("starttime");
		String endtime = (String) session.getAttribute("endtime");
		String excludesDates = (String) session.getAttribute("excludesDates");
		String[] exdates = excludesDates.split(",");
		if (exdates == null || exdates.length == 0)
			exdates = excludesDates.split("，");
		HashSet<String> set = new HashSet<String>();
		for (String d : exdates) {
			set.add(d);
		}
		List<String> shiftdates = (List<String>) session.getAttribute("shiftdates");
		String[] selectedshiftgroup = (String[]) session.getAttribute("selectedshiftgroup");
		List<Shift> shiftlist = shiftService.findShiftByGroupIds(selectedshiftgroup);
		
		// List<Shift> shiftlist=shiftService.findAll(PageRequest.of(0,
		// 100)).getContent();
		System.out.println(dates);
		// [["0.0",true],["1.0",true],["2.0",true]]
		String[] pro1 = dates.split("],");
		Map<String, List<String>> shiftdaymap = new HashMap<>();
		for (int i = 0; i < pro1.length; i++) {
			String str = pro1[i];
			String[] pro2 = str.split(",");
			String[] pro3 = pro2[0].split("\\.");// \\.
			String dateid = "";
			String shiftid = "";
			boolean flag;
			if (i == 0) {
				dateid = pro3[0].substring(3);

			} else {
				dateid = pro3[0].substring(2);
			}
			shiftid = pro3[1].substring(0, pro3[1].length() - 1);
			if (pro2[1].charAt(0) == 't')
				flag = true;
			else
				flag = false;
			if (flag) {
				List<String> daylist = shiftdaymap.getOrDefault(shiftid, new ArrayList<>());
				daylist.add(dateid);
				shiftdaymap.put(shiftid, daylist);
			}
			if(!shiftdaymap.containsKey(shiftid))
				shiftdaymap.put(shiftid,new ArrayList<>() );//if a shift is in shift group but won't used to schedule
			//then,shift day map will be {5=[0, 1], 6=[0, 1], 7=[0, 1], 8=[0, 1], 9=[0, 1]}, which is wrong
		}
		
		// begin to schedule

		// shiftdates：本次日期 shiftdaymap:shiftid,applieddatesindex
		// shiftlist：参与班次
		// shiftdates.get(shiftdaymap.get(shiftid).遍历):该shiftid应用的日期
		int totalPeople = 0;
		for (int i = 0; i < selectedshiftgroup.length; i++) {
			List<Shift> thegroupshiftlist = shiftGroupService.getShiftGroupById(Long.parseLong(selectedshiftgroup[i])).get().getShifts();
			//sales has employees, but no shifts. so if sales's group is selected, these employees should not be included
			if(thegroupshiftlist!=null&&thegroupshiftlist.size()>0){
			Optional<ShiftGroup> sgroup = shiftGroupService.getShiftGroupById(Long.parseLong(selectedshiftgroup[i]));
			if (sgroup.isPresent())
				totalPeople += sgroup.get().getType().getUsers().size();
		}
			}

		// 计算总班量（人次）
		int totalShifts = 0;
		double totalHours = 0.0;
		List<Double> workhourlist = new ArrayList<>();
		for (int i = 0; i < shiftlist.size(); i++) {
			Shift shift = shiftlist.get(i);
			String shiftendtime = shift.getEndtime();
			String[] endarr = shiftendtime.split("\\:");
			String shiftstarttime = shift.getStarttime();
			String[] startarr = shiftstarttime.split("\\:");
			// case1: 08:10-13:00 case2: 22:00-02:10
			int endhour = Integer.parseInt(endarr[0]);
			int endminute = Integer.parseInt(endarr[1]);
			int starthour = Integer.parseInt(startarr[0]);
			int startminute = Integer.parseInt(startarr[1]);
			double workhour = 0.0;
			if (endhour > starthour) {
				int starthm = starthour * 60 + startminute;
				int endhm = endhour * 60 + endminute;
				workhour = (endhm - starthm) / 60.0;
			} else {
				int starthm = starthour * 60 + startminute;
				int endhm = (endhour + 24) * 60 + endminute;
				workhour = (endhm - starthm) / 60.0;
			}
			workhour = (double) Math.round(workhour * 10) / 10;
			workhourlist.add(workhour);
			int shiftsnum = shiftdaymap.get(i + "").size() * shift.getNum();
			totalShifts += shiftsnum;
			totalHours += shiftsnum * workhour;
		}
		double shiftsperemp = totalShifts / (totalPeople + 0.0);
		shiftsperemp = (double) Math.round(shiftsperemp * 10) / 10;
		double shiftsperempday = shiftsperemp / shiftdates.size();
		shiftsperempday = (double) Math.round(shiftsperempday * 10) / 10;
		System.out.println(totalShifts + " " + shiftsperemp + " " + shiftsperempday);
		double hoursperemp = totalHours / (totalPeople + 0.0);
		hoursperemp = (double) Math.round(hoursperemp * 10) / 10;
		double hoursperempday = hoursperemp / shiftdates.size();
		hoursperempday = (double) Math.round(hoursperempday * 10) / 10;
		System.out.println(totalHours + " " + hoursperemp + " " + hoursperempday);

		// 统计完成。这里已经可以提示问题了。
		Map<String, Map<String, List<String>>> shiftview = new TreeMap<>();// <日期<班次名,人员名列表>
		Map<String, Map<String, List<String>>> peopleview = new TreeMap<>();// <日期<人名,班次列表>
		List<WorkingHoursCount> usertotalhour = new ArrayList<>();// 用于计算每个人的总时长总班次
		List<User> totaluserlist = new ArrayList<User>();//用于peopleview展示真正参与的人员
		List<Long> realshiftgroup=new ArrayList<>();//selectedshiftgroup may have groupid that has no shifts.
		// 开始遍历每一个group，因为group的人分到group的组
		int shiftprocess=0;//判断现在处理到哪个group的第一个shift了，用于大约280行的标号
		for (String groupstring : (String[]) session.getAttribute("selectedshiftgroup")) {
			System.out.println("groupstring:"+groupstring);
			Long shiftgroupid = Long.parseLong(groupstring);
			List<Shift> thegroupshiftlist = shiftGroupService.getShiftGroupById(shiftgroupid).get().getShifts();
			//sales has employees, but no shifts. so if sales's group is selected, these employees should not be included
			if(thegroupshiftlist!=null&&thegroupshiftlist.size()>0){
            realshiftgroup.add(shiftgroupid);
			List<User> userlist = shiftGroupService.getShiftGroupById(shiftgroupid).get().getType().getUsers();
			totaluserlist.addAll(userlist);
			//List<Shift> thegroupshiftlist = shiftGroupService.getShiftGroupById(shiftgroupid).get().getShifts();
			
			List<WorkingHoursCount> userhour = new ArrayList<>();// 用于计算每组人的总时长总班次
			for (int i = 0; i < userlist.size(); i++)
				userhour.add(new WorkingHoursCount(userlist.get(i).getName(), 0.0, 0));
			// 下面要给每天的每个班次标号，班次名+日期，否则会出现一个人在一天的一个班次做两天的情况
			List<ShiftForSchedule> shiftsforschedule = new ArrayList<>();
			for (int i = 0; i < thegroupshiftlist.size(); i++) {
				int shiftnum = thegroupshiftlist.get(i).getNum();
				for (int j = 0; j < shiftdaymap.get((i+shiftprocess)+ "").size(); j++) {//shiftdaymap:shiftid 例：0-9 是頁面的排列順序 dateid 例：【012478】代表這幾個天
					//原理：首先把groupid按照顺序给用户选，用户选完了以后，显示的所有shift是按照group的顺序来的
					//所以这里在遍历每一个group时，也是按照顺序的，只要把前一个group读完了，去shiftdaymap里取接下去的这个groupid的shifts分别应用的date就行了
					ShiftForSchedule s = new ShiftForSchedule();
					// 1 boxer 1st 1235cap2018-08-20
					s.setName(thegroupshiftlist.get(i).getName() + "cap"
							+ shiftdates.get(Integer.parseInt(shiftdaymap.get((i+shiftprocess) + "").get(j))));
					s.setHour(workhourlist.get((i+shiftprocess)));//workhourlist:按照group的shift的顺序依次计算时间
					for (int k = 0; k < shiftnum; k++)
						shiftsforschedule.add(s);
				}
			}
            System.out.println(shiftsforschedule.size());
            shiftprocess+=thegroupshiftlist.size();
			// for(int i=0;i<shiftsforschedule.size();i++){
			while (shiftsforschedule.size() > 0) {
				// 倒序
				shiftsforschedule.sort((a, b) -> {
					double minus = b.getHour() - a.getHour();
					if (minus > 0)
						return 1;
					else if (minus < 0)
						return -1;
					else
						return 0;
				});
				ShiftForSchedule s = shiftsforschedule.get(0);
				userhour.sort((a, b) -> {
					double minus = a.getHours() - b.getHours();
					if (minus > 0)
						return 1;
					else if (minus < 0)
						return -1;
					else
						return 0;
				});
				int minandnotuse = 0;
				String[] shiftanddate = s.getName().split("cap");
				Map<String, List<String>> shiftusermap = shiftview.getOrDefault(shiftanddate[1],
						new TreeMap<String, List<String>>());// shiftname-userlist
				if (shiftusermap.size() > 0) {
					List<String> useduser = shiftusermap.getOrDefault(shiftanddate[0], new ArrayList<String>());
					while (userhour.size()-1>minandnotuse&&useduser.contains(userhour.get(minandnotuse).getName()))
						//the problem is that if a shift needs more people than employees total number
						minandnotuse++;

				}
				System.out.println("hour:"+s.getHour());
				userhour.get(minandnotuse)
						.setHours((double) (Math.round(userhour.get(minandnotuse).getHours() + s.getHour()) * 10) / 10);
				userhour.get(minandnotuse).setShift(userhour.get(minandnotuse).getShift() + 1);
				shiftsforschedule.remove(0);
				if (shiftusermap.size() > 0) {// 如果这天已经开始排了
					List<String> useduser = shiftusermap.getOrDefault(shiftanddate[0], new ArrayList<String>());
					useduser.add(userhour.get(minandnotuse).getName());
					shiftusermap.put(shiftanddate[0], useduser);
				} else {
					Map<String, List<String>> map1 = new TreeMap<>();
					List<String> list = new ArrayList<>();
					list.add(userhour.get(minandnotuse).getName());
					map1.put(shiftanddate[0], list);
					shiftview.put(shiftanddate[1], map1);
				}
			}
			usertotalhour.addAll(userhour);
			}
			// this group id finished
		}
		// usertotalhour.forEach(e->System.out.println(e.getName()+"
		// "+e.getHours()+" "+e.getShift()));
		for (Entry<String, Map<String, List<String>>> e : shiftview.entrySet()) {
			String datename = e.getKey();
			Map<String, List<String>> usershiftmap = e.getValue();
			Map<String, List<String>> reversemap = new TreeMap<>();
			for (Entry<String, List<String>> ex : usershiftmap.entrySet()) {
				String shiftname = ex.getKey();
				List<String> userlist1 = ex.getValue();
				for (String u : userlist1) {
					List<String> shiftlist1 = reversemap.getOrDefault(u, new ArrayList<String>());
					shiftlist1.add(shiftname);
					reversemap.put(u, shiftlist1);
				}
			}
			peopleview.put(datename, reversemap);
		}

		session.setAttribute("totalHours", totalHours);
		session.setAttribute("hoursperemp", hoursperemp);
		session.setAttribute("hoursperempday", hoursperempday);
		session.setAttribute("totalShifts", totalShifts);
		session.setAttribute("shiftsperemp", shiftsperemp);
		session.setAttribute("shiftsperempday", shiftsperempday);
		session.setAttribute("userhour", usertotalhour);
		session.setAttribute("shiftlist", shiftlist);
		session.setAttribute("userlist", totaluserlist);
		session.setAttribute("shiftview", shiftview);
		session.setAttribute("peopleview", peopleview);
		session.setAttribute("realshiftgroup", realshiftgroup);
		
		LaborLaw lb=laborLawService.getLaborLawById(1L).get();
		if(lb.getUse().equals("true")){
			double oneweekhour=hoursperemp*7.0/shiftview.size();
			oneweekhour=(double) (Math.round(oneweekhour) * 10) / 10;
			if(oneweekhour-lb.getHour()>0){
				session.setAttribute("laborlaw", "Average employee working hours per week:  " +oneweekhour+". You violate the labor law("
			+((double) (Math.round(lb.getHour()) * 10) / 10)+"). Maybe you should add employees to work.");
			}
			else{
				session.setAttribute("laborlaw", "false");
			}
			
		}
		else{
			session.setAttribute("laborlaw", "false");
		}
		//session.setAttribute("saveoredit", "save");
		return ResponseEntity.ok().body(new Response(true, "处理成功"));
	}

	@GetMapping("/viewWorkShift")
	public ModelAndView viewWorkShift(HttpSession session,
			@RequestParam(value = "async", required = false) boolean async, Model model) {
		//List<Shift> shiftlist = shiftService.findAll(PageRequest.of(0, 100)).getContent();
		//List<User> userlist = shiftGroupService.getShiftGroupById(1L).get().getType().getUsers();
		model.addAttribute("shiftlist", session.getAttribute("shiftlist"));
		model.addAttribute("userlist", session.getAttribute("userlist"));
		model.addAttribute("shiftview", session.getAttribute("shiftview"));
		model.addAttribute("peopleview", session.getAttribute("peopleview"));
		model.addAttribute("totalHours", session.getAttribute("totalHours"));
		model.addAttribute("hoursperemp", session.getAttribute("hoursperemp"));
		model.addAttribute("hoursperempday", session.getAttribute("hoursperempday"));
		model.addAttribute("totalShifts", session.getAttribute("totalShifts"));
		model.addAttribute("shiftsperemp", session.getAttribute("shiftsperemp"));
		model.addAttribute("shiftsperempday", session.getAttribute("shiftsperempday"));
		model.addAttribute("userhour", session.getAttribute("userhour"));
		model.addAttribute("laborlaw", session.getAttribute("laborlaw"));
		model.addAttribute("saveoredit", "save");
		return new ModelAndView("schedule/viewWorkShift", "userModel", model);
		// return new ModelAndView(async == true ? "schedule/setShiftDates ::
		// #mainContainerRepleace" : "schedule/setShiftDates",
		// "userModel",model);

	}
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response> delete(@PathVariable("id") Long id, Model model) {		
		try {		
			saveScheduleService.removeSchedule(id);
		} catch (Exception e) {//e.getMessage()
			return ResponseEntity.ok().body(new Response(false, "error"));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功"));
	}

	
}