package com.digiwin.plm.athena.bean.factory;

import java.util.Map;

import com.digiwin.plm.athena.bean.general.info.Execution;
import com.digiwin.plm.athena.bean.general.info.StandardInfo;
import com.digiwin.plm.athena.bean.general.vo.StandardVo;

import dyna.common.util.SetUtils;
import dyna.common.util.StringUtils;

public class ResponseFactory
{
	public static StandardVo getSuccessStandardVo(Object resultVo, String token)
	{
		Execution execution = new Execution("0", "", "执行成功", token);
		
		StandardInfo standardInfo = new StandardInfo();
		standardInfo.setExecution(execution);
		standardInfo.setParameter(resultVo);
		
		return  new StandardVo(standardInfo);
	}
	
	public static StandardVo getFailStandardVo(Map<String,String> resultVo, String token)
	{
		String message = "";
		if(!SetUtils.isNullMap(resultVo))
		{
			message = resultVo.get("message");
		}
		
		if(StringUtils.isNullString(message))
		{
			message = "执行失败";
		}
		
		Execution execution = new Execution("1", "", message, token);
		
		StandardInfo standardInfo = new StandardInfo();
		standardInfo.setExecution(execution);
		standardInfo.setParameter(resultVo);
		
		return  new StandardVo(standardInfo);
	}
}
