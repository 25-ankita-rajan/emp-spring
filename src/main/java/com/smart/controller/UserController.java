package com.smart.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.smart.dao.UserEmpRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.entities.UserEmp;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository; 
	
	@Autowired
	private UserEmpRepository userEmpRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder; 
	
	
	//method for adding common data to response
	
	 @ModelAttribute 
	 public void addCommondata(Model model, Principal principal) {
	  
	  String username = principal.getName();
	  System.out.println("USERNAME "+username);
	  
	  User user = userRepository.getUserByUserName(username);
	  
	  System.out.println("USER "+user);
	  
	  model.addAttribute("user", user);
	  
	 }
	 
	
	// dashboard home
	 
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal)
	{
		model.addAttribute("title", "User Dashboard");
		
		
		/*
		 * String username = principal.getName();
		 * System.out.println("USERNAME "+username);
		 * 
		 * User user = userRepository.getUserByUserName(username);
		 * 
		 * System.out.println("USER "+user);
		 * 
		 * model.addAttribute("user", user);
		 */	
		return "normal/user_dashboard";
	}
	
	//open add form handler
	
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model)
	{
		
		model.addAttribute("title", "Add Contact");
		model.addAttribute("userEmp" , new UserEmp());
		
		return "normal/add_contact_form";
	}
	
	
	//processing add contact form
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute UserEmp userEmp, 
								Principal principal,
								HttpSession session ) {
		
		try {
			
			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);
				
			userEmp.setUser(user);
			userEmp.setRole("ROLE_EMP");
			userEmp.setEnabled(true);
			//userEmp.setPassword(passwordEncoder.encode(userEmp.getPassword()));
			
			user.getUseremp().add(userEmp);
			
			this.userRepository.save(user);
			
			System.out.println("USER EMP: "+userEmp);
			
			System.out.println("Added to database");
			
			// message success............
			session.setAttribute("message", new Message("Employee added successfully !!","success"));
			
			
		}catch(Exception e) {
			System.out.println("ERROR: "+e.getMessage());
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong !! Try again.","danger"));
		}
		
		
		return "normal/add_contact_form";
	}

	// show employee handler
	
	@GetMapping("/show-contacts")
	public String showContacts(Model m, Principal principal) {
		m.addAttribute("title","Show Employees");
		// employee ki list ko bhejni hai
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		
		List<UserEmp> userEmps = this.userEmpRepository.findUserEmpByUser(user.getId());
		
		m.addAttribute("userEmps",userEmps);
		
		return "normal/show_contacts";
	}
	
	// showing particular employee details.
	
	@RequestMapping("/{empid}/contact")
	public String showContactDetail(@PathVariable("empid") Integer empid,
			Model model, Principal principal)
	{
		
		Optional<UserEmp> userEmpOptional = this.userEmpRepository.findById(empid);
		UserEmp userEmp = userEmpOptional.get();
		
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		
		if(user.getId()==userEmp.getUser().getId())
		{
			model.addAttribute("userEmp", userEmp);
			model.addAttribute("title", userEmp.getName());
		}
		
		
		return "normal/contact_detail";
	}
	
	// delete employee handler
	
	@GetMapping("/delete/{empid}")
	public String deleteContact(@PathVariable("empid") Integer empid, Model model,
			HttpSession session, Principal principal)
	{
		Optional<UserEmp> userEmpOptional = this.userEmpRepository.findById(empid);
		UserEmp userEmp = userEmpOptional.get();
		
//		userEmp.setUser(null);
//		
//		this.userEmpRepository.delete(userEmp);
		
		User user=this.userRepository.getUserByUserName(principal.getName());
		
		user.getUseremp().remove(userEmp);
		
		this.userRepository.save(user);
		
		session.setAttribute("message", new Message("Employee deleted successfully !!","success"));
		
		return "redirect:/user/show-contacts";
	}
	
	// open update form handler
	
	@PostMapping("/update-contact/{empid}")
	public String updateForm(@PathVariable("empid") Integer empid, Model m)
	{
		m.addAttribute("title", "Update Contact");
		
		UserEmp userEmp = this.userEmpRepository.findById(empid).get();
		
		m.addAttribute("userEmp", userEmp);
		
		return "normal/update_form";
	}
	
	// update contact handler
	
	@RequestMapping(value = "/process-update/{empid}", method = RequestMethod.POST)
	public String updateHandler(@ModelAttribute UserEmp userEmp, 
			@PathVariable("empid") Integer empid,
			Model m, HttpSession session, Principal principal) 
	{
		
		try {
			
			User user=this.userRepository.getUserByUserName(principal.getName());
			
			userEmp.setEmpId(empid);
			userEmp.setRole("ROLE_EMP");
			userEmp.setUser(user);
			userEmp.setEnabled(true);
			
			this.userEmpRepository.save(userEmp);
			
			session.setAttribute("message", new Message("Employee details updated successfully !!","success"));
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("EMP NAME: "+userEmp.getName());
		System.out.println("EMP ID: "+userEmp.getEmpId());
		System.out.println("EMP ID 2: "+empid);
		
		return "redirect:/user/"+userEmp.getEmpId()+"/contact";
		
	}
	
	// your profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model)
	{
		model.addAttribute("title", "Profile page");
		return "normal/profile";
	}
	
}
