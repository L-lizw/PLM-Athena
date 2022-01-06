package com.digiwin.plm.athena.conf.properties;

import dyna.common.conf.ConnToASConfig;
import dyna.common.util.SetUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Lizw
 * @date 2022/1/5
 **/
@Getter
@Setter
@Component
@RefreshScope
@ConfigurationProperties(prefix = "client")
public class ClientConfig
{
	private String modename;

	private List<ClientConfig.ServerConf> serverlist;

	private ClientConfig.ServiceLookup serviceLookup;

	public ConnToASConfig getDefaultHost()
	{
		if(!SetUtils.isNullList(serverlist))
		{
			for(ServerConf serverConf:serverlist)
			{
				if(serverConf.isIsdefault())
				{
					ConnToASConfig connToASConfig = new ConnToASConfig();
					connToASConfig.setDefault(true);
					connToASConfig.setIpAddress(serverConf.getIp());
					connToASConfig.setName(serverConf.getName());
					connToASConfig.setPort(serviceLookup.getPort());
					if(serverConf.getPort() != null)
					{
						connToASConfig.setPort(serverConf.getPort().intValue());
					}
					return connToASConfig;
				}
			}
		}

		return null;
	}

	@Getter
	@Setter
	public static class ServerConf
	{

		private boolean isdefault;

		private String name;

		private String ip;

		private Integer port;

	}

	@Setter
	public static class ServiceLookup
	{
		private Integer port;

		public int getPort()
		{
			return port == null? 1299 :port.intValue();
		}
	}

}



