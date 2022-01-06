package com.digiwin.plm.athena.controller.athena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.digiwin.plm.athena.bean.annotation.ApplicationPlatform;
import com.digiwin.plm.athena.bean.enums.PlatformEnum;
import com.digiwin.plm.athena.bean.exception.WebException;
import com.digiwin.plm.athena.bean.general.vo.athena.AthenaDataChangeGetVo;
import com.digiwin.plm.athena.bean.general.vo.athena.ResponseObject;
import com.digiwin.plm.athena.net.PLMServiceProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.digiwin.plm.bean.exception.AthenaException;


import dyna.common.SearchCondition;
import dyna.common.SearchConditionFactory;
import dyna.common.bean.data.FoundationObject;
import dyna.common.bean.data.StructureObject;
import dyna.common.exception.ServiceRequestException;
import dyna.common.systemenum.OperateSignEnum;
import dyna.common.systemenum.SearchRevisionTypeEnum;
import dyna.common.systemenum.SystemClassFieldEnum;
import dyna.common.systemenum.ppms.OperationStateEnum;
import dyna.common.util.DateFormat;
import dyna.common.util.SetUtils;
import dyna.common.util.StringUtils;
import dyna.net.service.brs.BOAS;

@Api(tags = "对接Athena平台")
@RestController
public class AthenaController
{
	@Autowired
	private PLMServiceProvider psp = null;

	@SuppressWarnings("unchecked")
	@ApplicationPlatform( platform = PlatformEnum.EAI)
	@ApiOperation("数据侦测")
	@RequestMapping(path = "/data.change.get", method = RequestMethod.POST)
	public AthenaDataChangeGetVo listObject(HttpServletRequest request, @RequestBody Map<String, Object> body) throws AthenaException
	{
		String token = request.getHeader("token");
		try
		{

			if (SetUtils.isNullMap(body))
			{
				throw new AthenaException("invalid request body", "", "", token);
			}

			Map<String, Object> std_data = (Map<String, Object>) body.get("std_data");
			Map<String, Object> parameter = (Map<String, Object>) std_data.get("parameter");

			Map<String, Map<String, Object>> tem = new HashMap<String, Map<String, Object>>();

			boolean typeAsResultField = false;
			SearchCondition condition = SearchConditionFactory.createSearchCondition4Class("ECN2", null, false);

			List<Map<String, Object>> rules = (List<Map<String, Object>>) parameter.get("rules");
			if (!SetUtils.isNullList(rules))
			{
				for (Map<String, Object> rule : rules)
				{
					String rule_id = (String) rule.get("rule_id");

					if (tem.get(rule_id) == null)
					{
						tem.put(rule_id, new HashMap<String, Object>());
					}

					// 列整合处理
//					List<Map<String, Object>> action_params = (List<Map<String, Object>>) rule.get("action_params");
//					if (!SetUtils.isNullList(action_params))
//					{
//						for (Map<String, Object> action_param : action_params)
//						{
//							String name = (String) action_param.get("name");
//							List<Map<String, Object>> function_params = (List<Map<String, Object>>) action_param.get("function_params");
//							if (!SetUtils.isNullList(function_params))
//							{
//								for (Map<String, Object> function : function_params)
//								{
//									if ("column".equalsIgnoreCase((String) function.get("type")))
//									{
//										String columnName = (String) function.get("value");
//										if ("type".equalsIgnoreCase(columnName))
//										{
//											tem.get(rule_id).put(name, (String) function.get("value"));
//										}
//									}
//								}
//							}
//						}
//					}

					// 返回列
					List<Map<String, Object>> resultColumns = (List<Map<String, Object>>) rule.get("return_columns");
					if (!SetUtils.isNullList(resultColumns))
					{
						for (Map<String, Object> entry : resultColumns)
						{
							String columnName = (String) entry.get("name");
							String alias	= (String)entry.get("alias");
							if (columnName.contains("."))
							{
								columnName = columnName.split("\\.")[1];
								if (columnName.equalsIgnoreCase("ecn_no"))
								{
									condition.addResultField(SystemClassFieldEnum.ID.getName());
								}
								else if (columnName.equalsIgnoreCase("type"))
								{
									typeAsResultField = true;
								}

								tem.get(rule_id).put(columnName, alias);
							}
						}
					}

					// 查询条件
					Map<String, Object> dynamic_condition = (Map<String, Object>) rule.get("dynamic_condition");
					if (!SetUtils.isNullMap(dynamic_condition))
					{
						String type = (String) dynamic_condition.get("type");
						List<Map<String, Object>> items = (List<Map<String, Object>>) dynamic_condition.get("items");
						if ("AND_GROUP".equalsIgnoreCase(type))
						{
							condition.startGroup();

							if (!SetUtils.isNullList(items))
							{
								for (Map<String, Object> item : items)
								{
									String operation = (String) item.get("op");
									OperateSignEnum operateSignEnum = null;
									if ("EQUAL".equalsIgnoreCase(operation))
									{
										operateSignEnum = OperateSignEnum.EQUALS;
									}
									else if ("LESS_THAN".equalsIgnoreCase(operation))
									{
										operateSignEnum = OperateSignEnum.NOTLATER;
									}
									else if ("GREATER_EQUAL".equalsIgnoreCase(operation))
									{
										operateSignEnum = OperateSignEnum.NOTEARLIER;
									}

									String fieldType = (String) item.get("right_value_type");
									Object value = item.get("right");

									if ("DATE_TIME".equalsIgnoreCase(fieldType))
									{
										value = DateFormat.parse(String.valueOf(value), DateFormat.PTN_YMDHMS);
									}

									String fieldName = (String) item.get("left");

									if (fieldName.contains("."))
									{
										fieldName = fieldName.split("\\.")[1];
										if (fieldName.equalsIgnoreCase(SystemClassFieldEnum.RELEASETIME.name()))
										{
											fieldName = SystemClassFieldEnum.RELEASETIME.getName();
										}
									}

									if (fieldName.equalsIgnoreCase("status") && String.valueOf(value).equalsIgnoreCase("RLS"))
									{
										condition.setSearchRevisionTypeEnum(SearchRevisionTypeEnum.ISLATESTRLSONLY);
										continue;
									}

									condition.addFilter(fieldName, value, operateSignEnum);
								}
							}

							condition.endGroup();
						}
					}

				}
			}

			BOAS boas = (BOAS) psp.getServiceInstance(BOAS.class);
			List<FoundationObject> ecnList = boas.listObject(condition);

			if (typeAsResultField && !SetUtils.isNullList(ecnList))
			{
				for (FoundationObject ecn : ecnList)
				{
					List<StructureObject> afterSaleList = boas.listObjectOfRelation(ecn.getObjectGuid(), "SHDD", null, null, null);
					List<StructureObject> woList = boas.listObjectOfRelation(ecn.getObjectGuid(), "GDBG", null, null, null);

					boolean afterBooleanResult = SetUtils.isNullList(afterSaleList);
					boolean woBooleanResult = SetUtils.isNullList(woList);

					String type = "2";

					if (afterBooleanResult && woBooleanResult)
					{
						throw new AthenaException("no data found in ecn : id = " + ecn.getId(), "", "", token);
					}

					if ((!afterBooleanResult) && woBooleanResult)
					{
						type = "0";
					}
					else if (afterBooleanResult && (!woBooleanResult))
					{
						type = "1";
					}

					ecn.put("type", type);
				}
			}

			return this.convert(ecnList, tem);

		}
		catch (WebException e)
		{
			throw new AthenaException(e.getMessage(), "", "", token);
		}
		catch (ServiceRequestException e)
		{
			throw new AthenaException(e.getMessage(), "", "", token);
		}

	}

	private AthenaDataChangeGetVo convert(List<FoundationObject> foundationObjectList, Map<String, Map<String, Object>> tem)
	{
		AthenaDataChangeGetVo vo = new AthenaDataChangeGetVo();
		List<ResponseObject> responseObjectList = new ArrayList<>();
		if (!SetUtils.isNullMap(tem))
		{
			for (Map.Entry<String, Map<String, Object>> entry : tem.entrySet())
			{
				ResponseObject responseObject = new ResponseObject();

				String ruleId = entry.getKey();
				responseObject.setRule_id(ruleId);
				List<Map<String, Object>> change_objects = new ArrayList<>();

				if (!SetUtils.isNullList(foundationObjectList))
				{
					for (FoundationObject fo : foundationObjectList)
					{
						Map<String, Object> change_object = new HashMap<>();
						Map<String, Object> ruleDetail = entry.getValue();
						if (!SetUtils.isNullMap(ruleDetail))
						{
							for (Map.Entry<String, Object> columnEntry : ruleDetail.entrySet())
							{
								String old_columnName = columnEntry.getKey();
								String new_columnName = (String) columnEntry.getValue();
								
								if(StringUtils.isNullString(new_columnName))
								{
									new_columnName = old_columnName;
								}

								if ("type".equalsIgnoreCase(old_columnName))
								{
									change_object.put(new_columnName, fo.get("type"));
								}
								else if ("ecn_no".equalsIgnoreCase(old_columnName))
								{
									change_object.put(new_columnName, fo.getId());
								}
							}
						}

						change_objects.add(change_object);
					}
				}

				responseObject.setChange_objects(change_objects);
				responseObjectList.add(responseObject);
			}
		}
		vo.setResponse_objects(responseObjectList);

		return vo;
	}

}
