package com.digiwin.plm.athena.net;

import java.util.Date;
import java.util.List;

import com.digiwin.plm.athena.conf.properties.ClientConfig;
import dyna.common.conf.ConfigurableClientImpl;
import dyna.common.conf.ConfigurableKVElementImpl;
import dyna.common.systemenum.ConnectionMode;
import dyna.common.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.digiwin.plm.athena.bean.exception.WebException;

import dyna.common.Version;
import dyna.common.bean.data.system.RIG;
import dyna.common.exception.ServiceNotFoundException;
import dyna.common.exception.ServiceRequestException;
import dyna.common.systemenum.ApplicationTypeEnum;
import dyna.common.systemenum.LanguageEnum;
import dyna.net.impl.ServiceProviderFactory;
import dyna.net.impl.rmipool.StatelessRMIClient;
import dyna.net.service.Service;
import dyna.net.service.brs.AAS;
import dyna.net.spi.ServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class PLMServiceProviderImpl implements PLMServiceProvider
{
	@Autowired
	private ClientConfig                          clientConfig;
	private static final Logger       log		= LoggerFactory.getLogger(PLMServiceProviderImpl.class);
	private StatelessRMIClient	client	= null;
	private ServiceProvider		sp		= null;

	private void initClient()
	{
		try
		{
			ConfigurableClientImpl configurableClient = new ConfigurableClientImpl();
			configurableClient.setClientMode(ConnectionMode.getClientMode(clientConfig.getModename()));
			configurableClient.setDefaultLookupServicePort(Integer.valueOf(clientConfig.getServiceLookup().getPort()));
			configurableClient.setLookupServiceHost(clientConfig.getDefaultHost());
			configurableClient.configured();

			if (client == null)
			{
				client = new StatelessRMIClient(configurableClient);
				client.open();
				sp = ServiceProviderFactory.getServiceProvider(client);
			}
			else
			{
				try
				{
					client.testConnection();
				}
				catch(Exception e)
				{
					client = new StatelessRMIClient(configurableClient);
					client.open();
					sp = ServiceProviderFactory.getServiceProvider(client);
				}
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
		}
	}

	private synchronized <T extends Service> T getServiceInstance(Class<T> serviceClass, String credential) throws WebException
	{
		try
		{
			initClient();
		}
		catch (Exception e)
		{
			throw new WebException("Server can't be connected", "ID_CLIENT_CONNECT_ERROR", e);
		}
		if (client.testConnection())
		{
			try
			{
				return sp.getServiceInstance(serviceClass, credential);
			}
			catch (ServiceNotFoundException e)
			{
				client = null;
				throw new WebException("Server can't be connected", "ID_CLIENT_CONNECT_ERROR", e);
			}
		}
		else
		{
			client = null;
			throw new WebException("Server can't be connected", "ID_CLIENT_CONNECT_ERROR");
		}
	}

	public synchronized <T extends Service> T getServiceInstance(Class<T> serviceClass) throws WebException
	{
		return this.getServiceInstance(serviceClass, client == null ? null : client.getSeesionId());
	}

	public void genPLMSession(String userId, String ip, String hostName, LanguageEnum lan) throws WebException
	{
		AAS aas = this.getServiceInstance(AAS.class, null);
		String portalString = "portal";
		String portalPassword = portalString + userId + DateFormat.format(new Date(), "yyyyMMdd");
		portalPassword = EncryptUtils.encryptMD5(portalPassword);
		hostName = StringUtils.isNullString(hostName) ? ip : hostName;
		try
		{
			aas.checkClientVersion(Version.getVersionInfo(), null, false);
			String sessionid = aas.lookupSession(userId, portalPassword, ip, hostName, ApplicationTypeEnum.WEB);
			if (StringUtils.isNullString(sessionid))
			{
				List<RIG> rigs = aas.listRIGOfUserForLogin(userId);
				if (SetUtils.isNullList(rigs))
				{
					throw new WebException("missing userId or groupId or roleId or application type", "ID_MOBILE_USER_NO_RIG");
				}
				RIG rig = rigs.get(0);
				sessionid = aas.login(userId, rig.getGroupId(), rig.getRoleId(), portalPassword, ip, hostName, ApplicationTypeEnum.WEB, lan);
			}
			if(client!=null)
			{
				client.bindSeessionId(sessionid);
			}
		}
		catch (ServiceRequestException e)
		{
			if ("ID_APP_SESSION_EXPIRED".equals(e.getMsrId()))
			{
				client = null;
			}
			// log.error(e.getMessage(), e);
			throw new WebException(e);
		}
	}

	public void releasePLMSession() throws WebException
	{
		if(client!=null)
		{
			AAS aas = this.getServiceInstance(AAS.class, client.getSeesionId());
			try
			{
				aas.logout();
			}
			catch (ServiceRequestException e)
			{
				throw new WebException(e);
			}
			client = null;
		}
		
	}

	public String lookPLMUserByMobile(String userId) throws WebException
	{
		AAS aas = this.getServiceInstance(AAS.class);

		try
		{
			aas.checkClientVersion(Version.getVersionInfo(), null, true);
			return aas.getPLMUserByMobile(userId);
		}
		catch (ServiceRequestException e)
		{
			throw new WebException(e);
		}
	}

	public String lookPLMUserByPLMKey(String userId) throws WebException
	{
		return userId;
	}

	public String genPLMUserKey(String plmuserId) throws WebException
	{
		return plmuserId;
	}

	public String getPLMUserGuid(String userId) throws WebException
	{
		AAS aas = this.getServiceInstance(AAS.class);

		try
		{
			aas.checkClientVersion(Version.getVersionInfo(), null, true);
			return aas.getUserById(userId).getGuid();
		}
		catch (ServiceRequestException e)
		{
			throw new WebException(e);
		}
	}

}
