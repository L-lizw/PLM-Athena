package com.digiwin.plm.athena.bean.general.vo.eai;

import com.digiwin.plm.athena.bean.general.info.eai.HostProductInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductRegisterVo
{
	@ApiModelProperty("产品注册详细信息")
	private HostProductInfo host;

}
