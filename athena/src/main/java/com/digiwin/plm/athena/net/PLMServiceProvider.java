package com.digiwin.plm.athena.net;

import com.digiwin.plm.athena.bean.exception.WebException;

import dyna.common.systemenum.LanguageEnum;
import dyna.net.service.Service;

public interface PLMServiceProvider 
{
	public <T extends Service> T getServiceInstance(Class<T> serviceClass) throws WebException;

	public void genPLMSession(String userId, String ip, String hostName, LanguageEnum lan) throws WebException;
	
	public void releasePLMSession() throws WebException;;

	public String lookPLMUserByMobile(String userId) throws WebException;
	
	public String lookPLMUserByPLMKey(String userId) throws WebException;

	public String genPLMUserKey(String plmuserId) throws WebException;

	public String getPLMUserGuid(String plmuserId) throws WebException;

}