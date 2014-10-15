package kr.mamo.web.keygen.controller;

import javax.servlet.http.HttpServletRequest;

import kr.mamo.web.keygen.service.SiteInfoService;
import kr.mamo.web.keygen.service.UserInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserInfoContoller {
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	SiteInfoService siteInfoService;

	@RequestMapping(value = "/info")
	public String info(HttpServletRequest req, Model model, String currentUser) {
		model.addAttribute("info", userInfoService.info(currentUser));
        return "user/info";
	}
	
	@RequestMapping(value = "/create")
	public String register(HttpServletRequest req, Model model, String currentUser) {
		userInfoService.create(currentUser);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest req, Model model, String currentUser) {
		siteInfoService.delete(currentUser, null);
		userInfoService.delete(currentUser);
		return "redirect:/";
	}
}
