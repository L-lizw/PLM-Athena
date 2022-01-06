package com.digiwin.plm.athena.bean.general.vo;

import java.util.List;

import com.digiwin.plm.athena.bean.general.info.aftersale.AfterSaleInfo;
import io.swagger.annotations.Api;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Api(tags = "售后订单返回对象")
public class AfterSaleVo
{	
	public List<AfterSaleInfo> ecn_so_data;

}
