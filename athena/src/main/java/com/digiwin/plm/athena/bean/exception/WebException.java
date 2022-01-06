package com.digiwin.plm.athena.bean.exception;

import dyna.common.exception.ServiceRequestException;

public class WebException extends Exception
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -534500703823245275L;

	private String				msrId				= "";
	private Object[]			args				= null;

	public WebException(String message, Throwable e, String msrId, Object... args)
	{
		super(message, e);
		this.msrId = msrId;
		this.args = args;
	}

	/**
	 * @param message
	 * @param msrId
	 * @param args
	 */
	public WebException(String message, String msrId, Object... args)
	{
		this(message, null, msrId, args);
	}

	public WebException(String message)
	{
		super(message, null);
	}

	/**
	 * @param message
	 * @param msrId
	 * @param args
	 */
	public WebException(ServiceRequestException e)
	{
		this(e.getMessage(), e, e.getMsrId(), e.getArgs());
	}

	public String getMsrId()
	{
		return msrId;
	}

	public Object[] getArgs()
	{
		return args;
	}
}
