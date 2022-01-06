package com.digiwin.plm.athena.bean.general.info.eai;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HostInfo
{
	@ApiModelProperty("产品名")
	String prod;

	@ApiModelProperty("版本号")
	String ver;

	@ApiModelProperty("本地主机IP")
	String ip;

	@ApiModelProperty("产品唯一识别码")
	String id;

	@ApiModelProperty("产品唯一识别码")
	String uid;

}
