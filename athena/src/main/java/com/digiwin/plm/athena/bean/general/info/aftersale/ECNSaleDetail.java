package com.digiwin.plm.athena.bean.general.info.aftersale;

import com.digiwin.plm.athena.bean.annotation.Alias;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ECNSaleDetail
{
	@ApiModelProperty("客户编号")
	@Alias("KHBH")
	String customer_no;
	
	@ApiModelProperty("料件编号")
	@Alias("LJBH$ALTERID$")
	String item_no;
	
	@ApiModelProperty("销售数量")
	@Alias("XSSL")
	String so_qty;
	
	@ApiModelProperty("约定交货日")
	@Alias("YDJHR")
	String delivery_date;

	@ApiModelProperty("价格")
	String price = "0";
	
	@ApiModelProperty("据点")
	@Alias("FACTORY")
	String om_company_id;

	@ApiModelProperty("营运据点编号")
	String om_site_id;

	@ApiModelProperty("营运域编号")
	String om_region_id;
	
}
