package com.digiwin.plm.athena.bean.general.vo.athena;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseObject
{
	String rule_id;
	
	List<Map<String,Object>> change_objects;
	
}
