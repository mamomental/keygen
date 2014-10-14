package kr.mamo.web.keygen.controller;

import javax.servlet.http.HttpServletRequest;

import kr.mamo.web.keygen.service.MasterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/master")
public class MasterContoller {
	@Autowired
	MasterService master;

	@RequestMapping(value = "/info")
	public String info(HttpServletRequest req, Model model) {
		model.addAttribute("info", master.info());
        return "master/info";
	}
	
	@RequestMapping(value = "/register")
	public String register(HttpServletRequest req, Model model, String currentUser) {
		model.addAttribute("test", currentUser);
		model.addAttribute("info", master.info());
        return "master/info";
	}
}
