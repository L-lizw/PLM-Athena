package com.digiwin.plm.athena.bean.general.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Athena平台标准返回内嵌信息")
public class StandardInfo
{
	Execution execution;

	@ApiModelProperty(value = "接口返回对象统一放parameter中")
	Object	parameter;
}
