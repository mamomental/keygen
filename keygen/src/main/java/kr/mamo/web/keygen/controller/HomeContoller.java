package kr.mamo.web.keygen.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@Controller
public class HomeContoller {
	@RequestMapping(value = "/")
	public String sayHello(HttpServletRequest req, Model model) {
		UserService userService = UserServiceFactory.getUserService();
        String thisURL = req.getRequestURI();

        if (req.getUserPrincipal() != null) {
			model.addAttribute("loginUser", true);
			model.addAttribute("userName", req.getUserPrincipal().getName());
			model.addAttribute("url", userService.createLogoutURL(thisURL));
        } else {
			model.addAttribute("loginUser", false);
			model.addAttribute("url", userService.createLoginURL(thisURL));
        }
        return "index";
	}
}
