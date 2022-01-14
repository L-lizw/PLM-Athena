package com.digiwin.plm.athena.bean.general.info.aftersale;

import java.util.List;

import com.digiwin.plm.athena.bean.annotation.Alias;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AfterSaleInfo
{
	@ApiModelProperty("ECN单号")
	@Alias("ID$")
	private String ecn_no;

	@ApiModelProperty("ECN项次")
	@Alias("")
	private String ecn_seq;

	@ApiModelProperty("ECN主件项次")
	@Alias("")
	private String ecn_master_seq;

	@ApiModelProperty("售后订单列表")
	private List<ECNSaleDetail> ecn_so_detail;
}
