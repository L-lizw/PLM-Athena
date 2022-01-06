package com.digiwin.plm.athena.bean.general.vo;


import com.digiwin.plm.athena.bean.general.info.StandardInfo;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Athena平台标准返回", description= "Athena平台接收对象")
public class StandardVo
{
	StandardInfo std_data;
}
