package kr.mamo.web.keygen.controller;

import javax.servlet.http.HttpServletRequest;

import kr.mamo.web.keygen.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserContoller {
	@Autowired
	UserService us;

	@RequestMapping(value = "/info")
	public String info(HttpServletRequest req, Model model, String currentUser) {
		model.addAttribute("info", us.info(currentUser));
        return "user/info";
	}
	
	@RequestMapping(value = "/create")
	public String register(HttpServletRequest req, Model model, String currentUser) {
		us.create(currentUser);
		return "redirect:/user/info";
	}
	
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest req, Model model, String currentUser) {
		us.delete(currentUser);
		return "redirect:/user/info";
	}
	
	@RequestMapping(value = "/ajax")
	@ResponseBody
	public String ajax(HttpServletRequest req, Model model, String currentUser) {
		return "test";
	}
}
