package com.digiwin.plm.athena.bean.general.info.eai;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EAIServiceInfo
{
	@ApiModelProperty("注册接口集合")
	private List<String> srvname;

}
