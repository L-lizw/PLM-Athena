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
	private String ecn_no;
	
	@ApiModelProperty("ECN主件项次")
	private String ecn_master_seq;
	
	@ApiModelProperty("ECN项次")
	@Alias("ECNXC")
	private String ecn_seq;
	
	@ApiModelProperty("子件料号")
	@Alias("ZJLH$ALTERID$")
	private String new_component_no;
	
	@ApiModelProperty("旧元件料号")
	@Alias("JYJLH$ALTERID$")
	private String old_component_no;
	
	@ApiModelProperty("变更方式")
	@Alias("BGFS$NAME")
	private String change_mode;
	
	@ApiModelProperty("组成用量")
	@Alias("YL")
	private Integer qty;
	
	@ApiModelProperty("底数")
	@Alias("DS")
	private Integer master_base_number;
	
	@ApiModelProperty("客供料")
	private String customer_supplied_material;
	
	@ApiModelProperty("代买料")
	private String purchasing_material;
	
	@ApiModelProperty("作业编号")
	private String op_no;
	
	@ApiModelProperty("返修料号")
	private String repair_item_no;
	
	@ApiModelProperty("变更原因")
	private String change_reason;
	
	@ApiModelProperty("在制品处理")
	private String wip_handle;
	
	@ApiModelProperty("在途品处理")
	private String in_transit_handle;
	
	@ApiModelProperty("在库品处理")
	private String stock_item_handle;
	
	@ApiModelProperty("客户现场品处理")
	private String scene_item_handle;
	
	@ApiModelProperty("退库品是否可消耗")
	private String return_stock_item_run_out;
	
	@ApiModelProperty("是否连带变更")
	private String is_change;
	
	@ApiModelProperty("工程师")
	private String engineer_no;
	
	@ApiModelProperty("交期")
	@Alias("GCSYQJQ")
	private String delivery_date;
	
	@ApiModelProperty("工程电话号码")
	private String engineer_tel_no;
	
	@ApiModelProperty("备注")
	private String remarks;
	
	@ApiModelProperty("变更工单号")
	@Alias("BGGDH")
	private String wo_no;
	
	@ApiModelProperty("SN号")
	private String sn;
	
	@ApiModelProperty("工单项次")
	@Alias("GDXC")
	private String wo_seq;
	
	@ApiModelProperty("需求数量")
	@Alias("CNBCXQSL")
	private Integer demand_qty;
	
	@ApiModelProperty("工单项序")
	@Alias("GDXX")
	private String wo_term_seq;
	
	@ApiModelProperty("营运公司编号")
	@Alias("FACTORY")
	private String om_company_id;
	
	@ApiModelProperty("营运据点编号")
	private String om_site_id;
	
	@ApiModelProperty("营运域编号")
	private String om_region_id;
	
}
