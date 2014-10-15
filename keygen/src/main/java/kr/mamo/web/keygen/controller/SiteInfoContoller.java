package kr.mamo.web.keygen.controller;

import javax.servlet.http.HttpServletRequest;

import kr.mamo.web.keygen.db.model.User;
import kr.mamo.web.keygen.service.SiteInfoService;
import kr.mamo.web.keygen.service.UserInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/site")
public class SiteInfoContoller {
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	SiteInfoService siteInfoService;

	@RequestMapping(value = "/list")
	public String list(HttpServletRequest req, Model model, String currentUser) {
		model.addAttribute("list", siteInfoService.list(null, null, currentUser));
        return "site/list";
	}
	
	@RequestMapping(value = "/decList")
	@ResponseBody
	public Object decList(HttpServletRequest req, Model model, String currentUser, @RequestParam(required=false, value="encKey", defaultValue="") String encKey) {
		User user = userInfoService.info(currentUser);
		
		return siteInfoService.list(null != user ? user.getPrivateKey() : null, encKey, currentUser);
	}

	@RequestMapping(value = "/create")
	@ResponseBody
	public String create(HttpServletRequest req, Model model, String currentUser, @RequestParam(required=true, value="siteName") String siteName, @RequestParam(required=true, value="siteId") String siteId, @RequestParam(required=true, value="sitePw") String sitePw) {
		
		User user = userInfoService.info(currentUser);
		if (null != user) {
			if (!siteInfoService.create(user.getPublicKey(), currentUser, siteName, siteId, sitePw)) {
				siteInfoService.update(user.getPublicKey(), currentUser, siteName, siteId, sitePw);
			}
		}
		return "ok";
	}
}
