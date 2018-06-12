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

import java.util.ArrayList;
import java.util.List;

import com.ibm.cicsdev.osgi.ds.storage.StorageException;
import com.ibm.cicsdev.osgi.ds.storage.StorageService;

/**
 * In-memory implementation of the storage service.
 * <p>
 * <b>Note:</b> Indexes start at 1, not 0.
 * 
 * @author Alexander D Brown
 * @author Ivan Hargreaves
 * @version 1.0.0
 */
public class InMemoryStorage implements StorageService
{
	/** The underlying storage */
	private List<String> store = new ArrayList<>();

	@Override
	public String get(int id) throws StorageException
	{
		// We're off by one, so the actual index is one less than the ID as we index
		// from 1, not 0.
		return store.get(id - 1);
	}

	@Override
	public int put(String record) throws StorageException
	{
		// Add the item to storage.
		store.add(record);

		// Return the index of the item plus one, as our storage service indexes from 1.
		return store.size();
	}

	/**
	 * Called when this service is started.
	 */
	public void activate()
	{
		System.out.println("Starting in-memory temporary storage.");
	}

	/**
	 * Called when this service is stopped.
	 */
	public void deactivate()
	{
		System.out.println("Stopping in-memory temporary storage");

		// Clear the storage, in case this service is re-used later without being
		// re-initialised.
		store.clear();
	}
}
