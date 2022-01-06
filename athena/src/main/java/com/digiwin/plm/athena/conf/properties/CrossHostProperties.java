package com.digiwin.plm.athena.conf.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "host")
@RefreshScope
@ToString
public class CrossHostProperties
{
	private String ip;

	private String id;

	private String ver;

	private String port;

	private String acct;

	private String uid;

	private String resturl;

}
