package com.digiwin.plm.athena.web.intercept;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.digiwin.plm.athena.net.PLMServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.digiwin.plm.bean.exception.AthenaException;

import dyna.common.exception.ServiceRequestException;
import dyna.common.systemenum.LanguageEnum;
import dyna.common.util.StringUtils;

public class SessionInterceptor implements HandlerInterceptor 
{
	@Autowired
	private PLMServiceProvider psp;

	private static final Logger log = LoggerFactory.getLogger(SessionInterceptor.class);

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception
	{
		String token = request.getHeader("token");
		try {

			String url = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI()
					+ (request.getQueryString() == null ? "" : "?" + request.getQueryString());
			request.setCharacterEncoding("UTF-8");
			String paramUser = request.getParameter("user");
			//默認使用admin进行服务的获取
			if(StringUtils.isNullString(paramUser))
			{
				paramUser = "admin";
			}

			log.info("发起人:" + paramUser + "  " + "url:" + url);

			String plmUser = psp.lookPLMUserByMobile(paramUser);
			if (StringUtils.isNullString(plmUser)) {
				throw new ServiceRequestException("no user");
			}
			psp.genPLMSession(plmUser, request.getRemoteAddr(), request.getRemoteHost(), LanguageEnum.ZH_CN);
			return true;

		} catch (IOException e) {
			log.error(e.getMessage());
			new AthenaException(e.getMessage(), "", "", token);
		}
		return false;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception 
	{
		// TODO Auto-generated method stub

	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception
	{
		psp.releasePLMSession();
	}
}
