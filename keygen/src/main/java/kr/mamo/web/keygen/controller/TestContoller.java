package kr.mamo.web.keygen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestContoller {
	@RequestMapping("/hello")
	public String sayHello() {
        return "hello";
    }
}
