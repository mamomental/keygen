package kr.mamo.web.keygen.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.mamo.web.keygen.KeygenConstant;
import kr.mamo.web.keygen.db.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AccountInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	kr.mamo.web.keygen.service.UserInfoService us;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String thisURL = request.getRequestURI();
		UserService userService = UserServiceFactory.getUserService();
		
        if (request.getUserPrincipal() == null && !"/".equals(thisURL)) {
    		response.sendRedirect("/");
    		return false;       	
        }

        if (request.getUserPrincipal() == null) {
    		request.setAttribute(KeygenConstant.keygenRequest.URL, userService.createLoginURL(thisURL));
    		request.setAttribute(KeygenConstant.keygenRequest.CURRENT_USER, "");

        } else {
			request.setAttribute(KeygenConstant.keygenRequest.URL, userService.createLogoutURL(thisURL));
			request.setAttribute(KeygenConstant.keygenRequest.CURRENT_USER, request.getUserPrincipal().getName());
        }
        
        
		return super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		String url = (String)request.getAttribute(KeygenConstant.keygenRequest.URL);
		modelAndView.addObject(KeygenConstant.keygenRequest.URL, url);
		String currentUser = (String)request.getAttribute(KeygenConstant.keygenRequest.CURRENT_USER);
		
		if (StringUtils.isEmpty(currentUser)) {
			modelAndView.addObject("loginUser", false);
			modelAndView.addObject(KeygenConstant.keygenRequest.CURRENT_USER_LEVEL, User.LEVEL.ANONYMOUS);
		} else {
			modelAndView.addObject("loginUser", true);
			User user = us.info(currentUser);
			if (null != user) {
				modelAndView.addObject(KeygenConstant.keygenRequest.CURRENT_USER_LEVEL, User.LEVEL.fromLevel(user.getLevel()));
			} else {
				modelAndView.addObject(KeygenConstant.keygenRequest.CURRENT_USER_LEVEL, User.LEVEL.ANONYMOUS);
			}
		}
		modelAndView.addObject(KeygenConstant.keygenRequest.CURRENT_USER, currentUser);

		
		super.postHandle(request, response, handler, modelAndView);
	}

}
