package com.digiwin.plm.bean.general.info.wo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.digiwin.plm.athena.bean.annotation.Alias;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ECNWoDetail implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3825880692646659923L;

	@ApiModelProperty("ECN编号")
	@Alias("ECN$ID$")
	String ecn_no;
	
	@ApiModelProperty("ECN主件项次")
	String ecn_master_seq;
	
	@ApiModelProperty("ECN项次")
	@Alias("ECNXC")
	String ecn_seq;
	
	@ApiModelProperty("子件料号")
	@Alias("ZJLH$ALTERID$")
	String new_component_no;
	
	@ApiModelProperty("旧元件料号")
	@Alias("JYJLH$ALTERID$")
	String old_component_no;
	
	@ApiModelProperty("变更方式")
	@Alias("BGFS$NAME")
	String change_mode;
	
	@ApiModelProperty("组成用量")
	@Alias("YL")
	Integer qty;
	
	@ApiModelProperty("底数")
	@Alias("DS")
	Integer master_base_number;
	
	@ApiModelProperty("客供料")
	String customer_supplied_material;
	
	@ApiModelProperty("代买料")
	String purchasing_material;
	
	@ApiModelProperty("作业编号")
	String op_no;
	
	@ApiModelProperty("返修料号")
	String repair_item_no;
	
	@ApiModelProperty("变更原因")
	String change_reason;
	
	@ApiModelProperty("在制品处理")
	String wip_handle;
	
	@ApiModelProperty("在途品处理")
	String in_transit_handle;
	
	@ApiModelProperty("在库品处理")
	String stock_item_handle;
	
	@ApiModelProperty("客户现场品处理")
	String scene_item_handle;
	
	@ApiModelProperty("退库品是否可消耗")
	String return_stock_item_run_out;
	
	@ApiModelProperty("是否连带变更")
	String is_change;
	
	@ApiModelProperty("工程师")
	String engineer_no;
	
	@ApiModelProperty("交期")
	@Alias("GCSYQJQ")
	String delivery_date;
	
	@ApiModelProperty("工程电话号码")
	String engineer_tel_no;
	
	@ApiModelProperty("备注")
	String remarks;
	
	@ApiModelProperty
	@Alias("BGGDH")
	String wo_no;
	
	@ApiModelProperty("SN号")
	String sn;
	
	@ApiModelProperty("工单项次")
	@Alias("GDXC")
	String wo_seq;
	
	@ApiModelProperty("需求数量")
	@Alias("CNBCXQSL")
	Integer demand_qty;
	
	@ApiModelProperty("工单项序")
	@Alias("GDXX")
	String wo_term_seq;
	
	@ApiModelProperty("营运公司编号")
	@Alias("FACTORY")
	String om_company_id;
	
	@ApiModelProperty("营运据点编号")
	String om_site_id;
	
	@ApiModelProperty("营运域编号")
	String om_region_id;
	
}
