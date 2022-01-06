package com.digiwin.plm.athena.bean.enums;

public enum PlatformEnum
{
	Client("客户端"),

	EAI("互联中台");

	String description;

	 PlatformEnum(String description)
	{
		this.description = description;
	}
}
