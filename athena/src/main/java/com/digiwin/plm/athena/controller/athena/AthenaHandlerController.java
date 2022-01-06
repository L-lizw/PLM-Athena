package com.digiwin.plm.athena.controller.athena;

import com.digiwin.plm.athena.bean.factory.ResponseFactory;
import dyna.common.systemenum.ApplicationTypeEnum;
import dyna.common.util.SetUtils;
import dyna.common.util.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

@ControllerAdvice
public class AthenaHandlerController implements ResponseBodyAdvice
{

	@Override public boolean supports(MethodParameter returnType, Class converterType)
	{
		return true;
	}

	@Override public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response)
	{
		HttpHeaders headers = request.getHeaders();



		List<String> requestApplicationType = headers.get("ApplicationType");
		//默认athena平台发送的请求
		if(!SetUtils.isNullList(requestApplicationType))
		{

		}

		HttpHeaders responseHeaders = response.getHeaders();
		List<String> responseApplicationType = responseHeaders.get("ApplicationType");
		if(!SetUtils.isNullList(responseApplicationType))
		{
			String applicationType = responseApplicationType.get(0);
			if(applicationType.equalsIgnoreCase("Athena"))
			{
				String token = SetUtils.isNullList(headers.get("token"))?"":headers.get("token").get(0);
				return ResponseFactory.getSuccessStandardVo(body, token);
			}
		}

		return body;
	}
}
