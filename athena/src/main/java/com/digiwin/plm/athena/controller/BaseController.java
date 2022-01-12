package com.digiwin.plm.athena.controller;


import com.digiwin.plm.athena.bean.annotation.ApplicationPlatform;
import com.digiwin.plm.athena.bean.enums.PlatformEnum;
import com.digiwin.plm.athena.bean.exception.WebException;
import com.digiwin.plm.athena.bean.general.info.eai.EAIServiceInfo;
import com.digiwin.plm.athena.bean.general.info.eai.HostInfo;
import com.digiwin.plm.athena.bean.general.info.eai.HostProductInfo;
import com.digiwin.plm.athena.bean.general.vo.eai.ProductRegisterVo;
import com.digiwin.plm.athena.bean.general.vo.eai.SrvRegVo;
import com.digiwin.plm.athena.conf.properties.CrossHostProperties;
import com.digiwin.plm.athena.net.PLMServiceProvider;
import dyna.common.bean.data.system.CrossServiceConfig;
import dyna.common.exception.ServiceRequestException;
import dyna.common.util.JsonUtils;
import dyna.common.util.StringUtils;
import dyna.net.service.brs.ERPI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 产品注册与服务
 */
@Api(tags = "默认入口，可在请求头放digi-service参数，表明具体的请求接口")
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class BaseController
{
	private static final Logger log = LoggerFactory.getLogger(BaseController.class);

	@Autowired
	private RequestMappingHandlerMapping	requestMappingHandlerMapping;

	@Autowired
	private PLMServiceProvider psp = null;

	@Autowired
	private CrossHostProperties crossHostProperties;

	private Map<String, String> getHeader(HttpServletRequest request) throws ServiceRequestException
	{
		System.out.println("---------------------------------");
		System.out.println("Method:" + request.getMethod());
		System.out.println("--------");
		Map<String, String> map = new HashMap<>();
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements())
		{
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			System.out.println(key + ":" + value);
			map.put(key, value);
		}
		return map;
	}

	private Map<String, String> getService(HttpServletRequest request) throws ServiceRequestException
	{
		Map<String, String> requestHeader = this.getHeader(request);
		// 获取待转发的service
		Map<String, String> service = new HashMap<>();
		String serviceStr = requestHeader.get("digi-service");
		if (!StringUtils.isNullString(serviceStr))
		{
			service = JsonUtils.getObjectByJsonStr(serviceStr, Map.class);
		}
		System.out.println("--------");
		log.info("service: " + service);
		return service;
	}

	public String forward(String serviceName)
	{
		if (StringUtils.isNullString(serviceName))
		{
			return "forward:/default";
		}

		return "forward:/"+serviceName;
	}

	@ApiIgnore
	@ApiOperation(value = "首页")
	@RequestMapping("/default")
	public String defaultRequest()
	{
		return "index";
	}

	/**
	 * 注册入口
	 *
	 * @param request
	 * @return
	 * @throws WebException
	 * @throws ServiceRequestException
	 */
	@PostMapping ("/syncProd")
	public String syncProd(HttpServletRequest request) throws ServiceRequestException
	{
		Map<String, String> service = this.getService(request);
		String serviceName = service.get("name");
		return forward(serviceName);
	}

	/**
	 * 服务入口
	 *
	 * @param request
	 * @param response
	 * @return
	 */

	@ApiOperation(value = "请求头放digi-service参数，表明具体的请求接口,空参数则进入首页")
	@RequestMapping("/")
	public String invokeSrv(HttpServletRequest request, HttpServletResponse response) throws ServiceRequestException
	{
		response.addHeader("ApplicationType", "Athena");
		response.addHeader("digi-protocol", "raw");
		response.addHeader("digi-srvver", "1.0");
		response.addHeader("digi-srvcode", "000");

		PlatformEnum platformEnum = PlatformEnum.EAI;
		String platform = request.getHeader("Platform");
		if(!StringUtils.isNullString(platform))
		{
			PlatformEnum.valueOf(platform);
		}

		this.listAPIByPlatform(platformEnum);
		Map<String, String> service = this.getService(request);
		String serviceName = service.get("name");
		return forward(serviceName);
	}

	/**
	 * 取得产品注册信息
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 * @throws ServiceRequestException
	 */
	@ResponseBody
	@ApiOperation(value = "获取PLM注册的所有产品")
	@PostMapping("/getProdRegInfo")
	public ProductRegisterVo getProdRegInfo(HttpServletRequest request, HttpServletResponse response) throws WebException, ServiceRequestException
	{
		response.setHeader("digi-action", "reg");
		Map<String, String> service = this.getService(request);
		String ip = service.get("ip");
		ERPI erpi = psp.getServiceInstance(ERPI.class);

		Calendar cal = Calendar.getInstance();
		TimeZone timeZone = cal.getTimeZone();
		int zone = timeZone.getRawOffset() / 3600000;

		HostProductInfo hostProductInfo = new HostProductInfo();
		hostProductInfo.setProd(crossHostProperties.getName());
		hostProductInfo.setVer(crossHostProperties.getVer());
		hostProductInfo.setIp(ip);
		hostProductInfo.setId(crossHostProperties.getUid());
		hostProductInfo.setUid(crossHostProperties.getUid());
		hostProductInfo.setTimezone(String.valueOf(zone));
		hostProductInfo.setResturl(crossHostProperties.getResturl());
		hostProductInfo.setRetrytimes(crossHostProperties.getRetrytimes());
		hostProductInfo.setRetryinterval(crossHostProperties.getRetryinterval());
		hostProductInfo.setConcurrence(crossHostProperties.getConcurrence());

		return new ProductRegisterVo(hostProductInfo);
	}

	/**
	 * 取得服务注册信息
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 * @throws ServiceRequestException
	 */
	@ResponseBody
	@ApiOperation(value = "获取PLM所有注册的接口")
	@PostMapping("/getSrvRegInfo")
	public SrvRegVo getSrvRegInfo(HttpServletRequest request, HttpServletResponse response) throws WebException, ServiceRequestException
	{
		response.setHeader("digi-action", "reg");
		Map<String, String> service = this.getService(request);
		String ip = service.get("ip");
		ERPI erpi = psp.getServiceInstance(ERPI.class);

		List<String> srvnameList = this.listAPIByPlatform(PlatformEnum.EAI);
		EAIServiceInfo eaiServiceInfo = new EAIServiceInfo(srvnameList);

		SrvRegVo srvRegVo = new SrvRegVo();

		HostInfo hostInfo = new HostInfo();
		hostInfo.setProd(crossHostProperties.getName());
		hostInfo.setVer(crossHostProperties.getVer());
		hostInfo.setIp(ip);
		hostInfo.setUid(crossHostProperties.getUid());
		hostInfo.setId(crossHostProperties.getUid());

		srvRegVo.setHost(hostInfo);
		srvRegVo.setService(eaiServiceInfo);

		return srvRegVo;
	}

	/**
	 * cross同步设置到plm
	 *
	 * @param requestData
	 * @param response
	 * @return
	 * @throws WebException
	 * @throws ServiceRequestException
	 */
	@ApiOperation(value = "接收中台信息，整合设定同步PLM资讯")
	@PostMapping("/doSyncProcess")
	public void doSyncProcess(@RequestBody String requestData, HttpServletResponse response) throws WebException, ServiceRequestException
	{
		ERPI erpi = psp.getServiceInstance(ERPI.class);
		String code;
		try
		{
			code = erpi.doSyncProcess(requestData, true);
			response.setHeader("digi-srvcode", code);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ServiceRequestException(null, e.getMessage(), e);
		}
	}

	/**
	 *  根据请求来源平台获取所需接口列表
	 * @param platformEnum
	 * @return
	 */
	public List<String> listAPIByPlatform(PlatformEnum platformEnum)
	{
		List<String> urlList = new ArrayList<>();
		Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
		for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet())
		{
			RequestMappingInfo info = m.getKey();
			HandlerMethod method = m.getValue();

			ApplicationPlatform platform = method.getMethod().getDeclaredAnnotation(ApplicationPlatform.class);
			if(platform==null || platform.platform() != platformEnum)
			{
				continue;
			}

			PatternsRequestCondition p = info.getPatternsCondition();
			for (String url : p.getPatterns())
			{
				if(url.startsWith("/"))
				{
					url = url.substring(1);
				}
				urlList.add(url);
			}

		}
		log.info("返回结果:{}" + String.valueOf(urlList));
		return urlList;
	}

}
