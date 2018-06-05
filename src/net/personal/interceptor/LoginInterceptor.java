package net.personal.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import net.personal.pojo.SampleUser;
import net.personal.staticData.WebDictionary;

public class LoginInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("requested");
		System.out.println(request.getRequestURI());
		SampleUser user = (SampleUser) request.getSession().getAttribute(WebDictionary.USER_FIELD);
		if (user == null) {
			response.sendRedirect(WebDictionary.LOGIN_URI);
			return false;
		}
		return super.preHandle(request, response, handler);
	}

}
