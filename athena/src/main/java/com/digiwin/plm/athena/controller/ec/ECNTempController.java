package com.digiwin.plm.athena.controller.ec;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.digiwin.plm.athena.bean.annotation.ApplicationPlatform;
import com.digiwin.plm.athena.bean.enums.PlatformEnum;
import com.digiwin.plm.athena.bean.exception.WebException;
import com.digiwin.plm.athena.bean.general.info.aftersale.AfterSaleInfo;
import com.digiwin.plm.athena.bean.general.info.aftersale.ECNSaleDetail;
import com.digiwin.plm.athena.bean.general.info.wo.ECNWoInfo;
import com.digiwin.plm.athena.bean.general.vo.AfterSaleVo;
import com.digiwin.plm.athena.bean.util.JsonConvertUtil;
import com.digiwin.plm.athena.net.PLMServiceProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digiwin.plm.bean.exception.AthenaException;


import dyna.common.SearchCondition;
import dyna.common.SearchConditionFactory;
import dyna.common.bean.data.FoundationObject;
import dyna.common.bean.data.FoundationObjectImpl;
import dyna.common.bean.data.StructureObject;
import dyna.common.bean.data.StructureObjectImpl;
import dyna.common.exception.ServiceRequestException;
import dyna.common.systemenum.OperateSignEnum;
import dyna.common.systemenum.SearchRevisionTypeEnum;
import dyna.common.systemenum.SystemClassFieldEnum;
import dyna.common.util.DateFormat;
import dyna.common.util.SetUtils;
import dyna.common.util.StringUtils;
import dyna.net.service.brs.BOAS;
import dyna.net.service.brs.ERPI;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "ECN变更单" )
@RestController
public class ECNTempController
{
	@Autowired
	private PLMServiceProvider psp = null;

	@SuppressWarnings("unchecked")
	@ApiOperation("根据ECN单号获取售后订单")
	@ApplicationPlatform(platform = PlatformEnum.EAI)
	@PostMapping(path = "/after.sale.so.data.get")
	public AfterSaleVo getAfterSaleByEcn(HttpServletRequest request, @RequestBody Map<String, Object> body) throws AthenaException
	{

		String token = request.getHeader("token");

		if (SetUtils.isNullMap(body))
		{
			throw new AthenaException("invalid request", "", null, token);
		}

		Map<String, Object> std_data = (Map<String, Object>) body.get("std_data");
		Map<String, Object> parameter = (Map<String, Object>) std_data.get("parameter");


		List<String> ecnidList = new ArrayList<>();
		List<Map<String,Object>> ecn_dataList = (List<Map<String,Object>>)parameter.get("ecn_data");
		if(!SetUtils.isNullList(ecn_dataList))
		{
			for(Map<String,Object> ecn:ecn_dataList)
			{
				String ecnId = (String)ecn.get("ecn_no");
				if(!StringUtils.isNullString(ecnId))
				{
					ecnidList.add(ecnId);
				}
			}
		}

		try
		{
			ERPI erpi = (ERPI) psp.getServiceInstance(ERPI.class);
			BOAS boas = (BOAS) psp.getServiceInstance(BOAS.class);
			Map<String, List<StructureObject>> structureMap = erpi.getAfterSaleInfo(ecnidList);

			AfterSaleVo vo = new AfterSaleVo();
			List<AfterSaleInfo> saleList = new ArrayList<>();

			if (!SetUtils.isNullMap(structureMap))
			{
				structureMap.forEach((ec, list) -> {

					SearchCondition condition = SearchConditionFactory.createSearchCondition4Class("ECN2", null, false);
					condition.addFilter(SystemClassFieldEnum.ID, ec, OperateSignEnum.EQUALS);
					condition.setSearchRevisionTypeEnum(SearchRevisionTypeEnum.ISLATESTRLSONLY);
					try
					{
						FoundationObject ecn = boas.listObject(condition).get(0);
						AfterSaleInfo saleInfo = JsonConvertUtil.convertMapToObject((FoundationObjectImpl) ecn, AfterSaleInfo.class);

						List<ECNSaleDetail> detailList = new ArrayList<>();

						if (!SetUtils.isNullList(list))
						{
							for (StructureObject str : list)
							{
								Date date = (Date)str.get("YDJHR");
								if(date!=null)
								{
									str.put("YDJHR", DateFormat.formatYMD(date));
								}
								ECNSaleDetail detail = JsonConvertUtil.convertMapToObject((StructureObjectImpl) str, ECNSaleDetail.class);
								detailList.add(detail);
							}
						}

						saleInfo.setEcn_so_detail(detailList);

						saleList.add(saleInfo);
					}
					catch (ServiceRequestException e)
					{
						e.printStackTrace();
					}

				});
			}

			vo.setEcn_so_data(saleList);



			return vo;

		}
		catch (WebException e)
		{
			throw new AthenaException(e.getMessage(), null, null, token);
		}
		catch (ServiceRequestException e)
		{
			throw new AthenaException(e.getMessage(), null, null, token);
		}

	}

	@SuppressWarnings("unchecked")
	@ApiOperation("根据ECN单号获取工单信息")
	@ApplicationPlatform(platform = PlatformEnum.EAI)
	@PostMapping(path = "/wo.change.data.get")
	public ECNWoInfo getOrderInfoByEcn(HttpServletRequest request, @RequestBody Map<String, Object> body) throws AthenaException
	{
		String token = request.getHeader("token");
		if (SetUtils.isNullMap(body))
		{
			throw new AthenaException("invalid request body!", "", null, token);
		}
		try
		{
			Map<String, Object> std_data = (Map<String, Object>) body.get("std_data");
			Map<String, Object> parameter = (Map<String, Object>) std_data.get("parameter");

			List<String> ecnidList = new ArrayList<>();
			List<Map<String,Object>> ecn_dataList = (List<Map<String,Object>>)parameter.get("ecn_data");
			if(!SetUtils.isNullList(ecn_dataList))
			{
				for(Map<String,Object> ecn:ecn_dataList)
				{
					String ecnId = (String)ecn.get("ecn_no");
					if(!StringUtils.isNullString(ecnId))
					{
						ecnidList.add(ecnId);
					}
				}
			}

			ERPI erpi = (ERPI) psp.getServiceInstance(ERPI.class);
			BOAS boas = (BOAS) psp.getServiceInstance(BOAS.class);
			Map<String, List<StructureObject>> structureMap = erpi.getOrderInfo(ecnidList);

			List<com.digiwin.plm.bean.general.info.wo.ECNWoDetail> detailList = new ArrayList<>();

			if (!SetUtils.isNullMap(structureMap))
			{
				structureMap.forEach((ec, list) -> {

					SearchCondition condition = SearchConditionFactory.createSearchCondition4Class("ECN2", null, false);
					condition.addFilter(SystemClassFieldEnum.ID, ec, OperateSignEnum.EQUALS);
					condition.setSearchRevisionTypeEnum(SearchRevisionTypeEnum.ISLATESTRLSONLY);
					try
					{
						FoundationObject ecn = boas.listObject(condition).get(0);

						if (!SetUtils.isNullList(list))
						{
							for (StructureObject str : list)
							{
								str.put("ECN$ID$", ecn.getId());
								Date date = (Date)str.get("GCSYQJQ");
								if(date!=null)
								{
									str.put("GCSYQJQ", DateFormat.formatYMD(date));
								}

								String bgfs = (String)str.get("");

								com.digiwin.plm.bean.general.info.wo.ECNWoDetail detail = JsonConvertUtil.convertMapToObject((StructureObjectImpl) str, com.digiwin.plm.bean.general.info.wo.ECNWoDetail.class);
								if(detail.getChange_mode() != null && detail.getChange_mode().equalsIgnoreCase("1"))
								{
									detail.setWo_seq(" ");
									detail.setWo_term_seq(" ");
								}
								detailList.add(detail);
							}
						}
					}
					catch (ServiceRequestException e)
					{
						e.printStackTrace();
					}

				});
			}

			return new ECNWoInfo(detailList);

		}
		catch (WebException e)
		{
			throw new AthenaException(e.getMessage(), null, null, token);
		}
		catch (ServiceRequestException e)
		{
			throw new AthenaException(e.getMessage(), null, null, token);
		}

	}

}
