package com.digiwin.plm.athena.bean.general.info.wo;

import java.io.Serializable;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ECNWoInfo implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2107801391874452129L;

	List<com.digiwin.plm.bean.general.info.wo.ECNWoDetail> ecn_wo_data;
}
