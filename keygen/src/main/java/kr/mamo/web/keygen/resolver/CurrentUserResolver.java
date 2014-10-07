package kr.mamo.web.keygen.resolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CurrentUserResolver implements HandlerMethodArgumentResolver {

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer container, NativeWebRequest request,
			WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest httpServletRequest = request.getNativeRequest(HttpServletRequest.class);
		
		String userName = (String)httpServletRequest.getAttribute("userName");
		if (null != userName) {
			return userName;
		}
		return null;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(String.class);
	}
}
