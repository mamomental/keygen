package kr.mamo.web.keygen.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/level")
public class LevelContoller {
	@RequestMapping(value = "/list")
	public String sayHello(HttpServletRequest req, Model model) {
        return "level/list";
	}
}
