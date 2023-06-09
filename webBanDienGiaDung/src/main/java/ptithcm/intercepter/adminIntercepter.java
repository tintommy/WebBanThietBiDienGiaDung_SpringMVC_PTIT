package ptithcm.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import ptithcm.Entity.NguoiDungEntity;
import ptithcm.controller.userController;

public class adminIntercepter extends HandlerInterceptorAdapter{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
	NguoiDungEntity user=(NguoiDungEntity) request.getSession().getAttribute("USER");
		
	if(user==null|| user.getQuyen()==0)
	{
		response.sendRedirect(request.getContextPath() + "/khongCoQuyen.htm");
	}
	return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}
}