package kr.mamo.web.keygen.controller;

import javax.servlet.http.HttpServletRequest;

import kr.mamo.web.keygen.service.MasterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.appengine.api.datastore.Entity;

@Controller
@RequestMapping("/master")
public class MasterContoller {
	@Autowired
	MasterService master;
	
	@RequestMapping(value = "/info")
	public String sayHello(HttpServletRequest req, Model model) {
		model.addAttribute("info", master.info());
		Entity entity = master.info();
		model.addAttribute("name", entity.getProperty("name"));
		model.addAttribute("pbk", entity.getProperty("publicKey"));
		model.addAttribute("prk", entity.getProperty("privateKey"));
        return "master/info";

	}
}
