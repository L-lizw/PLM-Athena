package com.digiwin.plm.athena.bean.general.info.eai;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HostProductInfo extends HostInfo
{
	@ApiModelProperty("时间")
	private String timezone;

	@ApiModelProperty("地址")
	private String resturl;

	@ApiModelProperty("失败重试次数")
	private String retrytimes;

	@ApiModelProperty("重试超时时间")
	private String retryinterval;

	@ApiModelProperty("并发最大数")
	private String concurrence;

}
