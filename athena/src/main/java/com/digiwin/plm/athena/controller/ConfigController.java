package com.digiwin.plm.athena.controller;

import com.digiwin.plm.athena.conf.properties.ClientConfig;
import com.digiwin.plm.athena.conf.properties.CrossHostProperties;
import dyna.common.util.SetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Lizw
 * @date 2022/1/4
 **/
@RestController
@RefreshScope
public class ConfigController
{
	@Value("${host.ip}")
	private String ip;

	@Autowired
	private ClientConfig cf;

	@Autowired
	private CrossHostProperties cro;

	@GetMapping("getIP")
	public void get()
	{
		List<ClientConfig.ServerConf> list = cf.getServerlist();
		if(!SetUtils.isNullList(list))
		{
			for(ClientConfig.ServerConf conf: list)
			{
				System.out.println(conf.toString());
			}
		}

		ClientConfig.ServiceLookup look = cf.getServiceLookup();
		System.out.println(look.toString());

	}

	@GetMapping("getC")
	public String getc()
	{
		return cro.toString();
	}
}
