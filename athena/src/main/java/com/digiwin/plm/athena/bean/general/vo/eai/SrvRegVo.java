package com.digiwin.plm.athena.bean.general.vo.eai;

import com.digiwin.plm.athena.bean.general.info.eai.EAIServiceInfo;
import com.digiwin.plm.athena.bean.general.info.eai.HostInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SrvRegVo
{
	@ApiModelProperty("主机信息")
	private HostInfo host;

	@ApiModelProperty("接口信息")
	private EAIServiceInfo service;
}
