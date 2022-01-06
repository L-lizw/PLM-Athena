package com.digiwin.plm.athena.bean.general.info;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Execution
{
	@ApiModelProperty(value = "执行返回代码")
	String code;

	@ApiModelProperty(value = "执行sql返回错误码")
	String sql_code;

	@ApiModelProperty(value = "返回描述信息")
	String description;

	@ApiModelProperty(value = "请求token原路返回")
	String token_id;
}
