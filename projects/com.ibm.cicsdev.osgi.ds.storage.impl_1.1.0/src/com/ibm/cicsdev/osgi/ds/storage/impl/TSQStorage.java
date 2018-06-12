package com.ibm.cicsdev.osgi.ds.storage.impl;

/* Licensed Materials - Property of IBM                               */
/*                                                                    */
/* SAMPLE                                                             */
/*                                                                    */
/* (c) Copyright IBM Corp. 2018 All Rights Reserved                   */
/*                                                                    */
/* US Government Users Restricted Rights - Use, duplication or        */
/* disclosure restricted by GSA ADP Schedule Contract with IBM Corp   */
/*                                                                    */

import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.ItemHolder;
import com.ibm.cics.server.TSQ;
import com.ibm.cicsdev.osgi.ds.storage.StorageException;
import com.ibm.cicsdev.osgi.ds.storage.StorageService;

/**
 * Implementation of a storage service which uses a TSQ as the underlying
 * storage system.
 * 
 * @author Alexander D Brown
 * @author Ivan Hargreaves
 * @version 1.1.0
 */
public class TSQStorage implements StorageService
{
	/** The name of the TSQ */
	private static final String TSQ_NAME = "TSQS";

	@Override
	public String get(int id) throws StorageException
	{
		final ItemHolder holder = new ItemHolder();

		try
		{
			// Read the item from the TSQ.
			getTSQ().readItem(id, holder);
		} catch (CicsConditionException e)
		{
			throw new StorageException(e, e.getRESP(), e.getRESP2());
		}

		// Return the string value.
		return holder.getStringValue();
	}

	@Override
	public int put(String record) throws StorageException
	{
		try
		{
			// Put the item into the TSQ, getting the item number of the inserted item.
			final int id = getTSQ().writeString(record);
			return id;
		} catch (CicsConditionException e)
		{
			throw new StorageException(e, e.getRESP(), e.getRESP2());
		}
	}

	/**
	 * Called by DS when this service is started.
	 */
	public void activate()
	{
		System.out.println("Starting TSQ service");
	}

	/**
	 * Called by DS when this service is started.
	 */
	public void deactivate()
	{
		System.out.println("Stopping TSQ service");
	}

	/**
	 * @return The TSQ object, as JCICS objects cannot be shared across threads.
	 */
	private static TSQ getTSQ()
	{
		TSQ tsq = new TSQ();
		tsq.setName(TSQ_NAME);

		return tsq;
	}
}
