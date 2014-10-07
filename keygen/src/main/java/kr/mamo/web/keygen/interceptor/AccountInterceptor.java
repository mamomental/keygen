package kr.mamo.web.keygen.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AccountInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String thisURL = request.getRequestURI();
		UserService userService = UserServiceFactory.getUserService();

        if (request.getUserPrincipal() == null) {
    		request.setAttribute("url", userService.createLoginURL(thisURL));
    		request.setAttribute("userName", "");
    		response.sendRedirect("/");
    		return false;
        }
		request.setAttribute("url", userService.createLogoutURL(thisURL));
		request.setAttribute("userName", request.getUserPrincipal().getName());
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		String url = (String)request.getAttribute("url");
		modelAndView.addObject("url", url);
		String userName = (String)request.getAttribute("userName");
		modelAndView.addObject("userName", userName);

		
		super.postHandle(request, response, handler, modelAndView);
	}

}
