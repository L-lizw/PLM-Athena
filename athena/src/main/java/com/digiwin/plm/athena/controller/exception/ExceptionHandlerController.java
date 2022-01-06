package com.digiwin.plm.athena.controller.exception;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.digiwin.plm.athena.bean.exception.WebException;
import com.digiwin.plm.athena.bean.factory.ResponseFactory;
import com.digiwin.plm.athena.bean.general.vo.StandardVo;
import com.digiwin.plm.athena.bean.util.I18nResourceUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digiwin.plm.bean.exception.AthenaException;


import dyna.common.exception.ServiceRequestException;
import dyna.common.systemenum.LanguageEnum;

@ControllerAdvice
public class ExceptionHandlerController
{
	private static final Logger	log	= LoggerFactory.getLogger(ExceptionHandlerController.class);

	@ResponseBody
	@ExceptionHandler(WebException.class)
	public Map<String, Object> handlerWXException(WebException ex, HttpServletRequest request)
	{
		return this.getErrorPage(ex, request);
	}

	@ResponseBody
	@ExceptionHandler(ServiceRequestException.class)
	public Map<String, Object> handleServiceRequestException(ServiceRequestException ex, HttpServletRequest request)
	{
		WebException wxex = new WebException(ex);
		return this.getErrorPage(wxex, request);
	}

	private Map<String, Object> getErrorPage(WebException ex, HttpServletRequest request)
	{
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("code", "1");
		errorMap.put("message", I18nResourceUtil.getResource(LanguageEnum.ZH_CN.getLocal().toString(), ex.getMsrId(), ex.getMessage(), ex.getArgs()));
		log.error(ex.getMsrId() + ":" + ex.getMessage());
		return errorMap;
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@ExceptionHandler(AthenaException.class)
	public StandardVo handlerAthenaException(AthenaException exception)
	{
		try
		{
			 Map<String,String> exceptionInfoMap = BeanUtils.describe(exception);
			 return ResponseFactory.getFailStandardVo(exceptionInfoMap, exception.getToken());
		}
		catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
