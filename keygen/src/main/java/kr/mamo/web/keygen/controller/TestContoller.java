package kr.mamo.web.keygen.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@Controller
public class TestContoller {
	@RequestMapping(value = "/hello")
	public String sayHello(HttpServletRequest req, Model model) {
		UserService userService = UserServiceFactory.getUserService();

        String thisURL = req.getRequestURI();

        if (req.getUserPrincipal() != null) {
			model.addAttribute("test01", req.getUserPrincipal().getName());
			model.addAttribute("test02", userService.createLogoutURL(thisURL));
        } else {
			model.addAttribute("test01", userService.createLoginURL(thisURL));
        }
        return "hello";
	}
}
