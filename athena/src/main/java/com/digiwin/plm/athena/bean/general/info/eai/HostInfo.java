package com.digiwin.plm.athena.bean.general.info.eai;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HostInfo
{
	@ApiModelProperty("产品名")
	private String prod;

	@ApiModelProperty("版本号")
	private String ver;

	@ApiModelProperty("本地主机IP")
	private String ip;

	@ApiModelProperty("产品唯一识别码")
	private String id;

	@ApiModelProperty("产品唯一识别码")
	private String uid;

}
