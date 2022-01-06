package com.digiwin.plm.bean.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AthenaException extends Exception
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -534500703823245275L;

	String						message;
	String						errorCode;
	String						sqlCode;
	String						token;

}
