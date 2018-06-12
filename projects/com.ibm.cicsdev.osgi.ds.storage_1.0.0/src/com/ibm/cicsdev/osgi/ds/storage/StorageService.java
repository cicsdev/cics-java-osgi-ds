package com.ibm.cicsdev.osgi.ds.storage;

/* Licensed Materials - Property of IBM                               */
/*                                                                    */
/* SAMPLE                                                             */
/*                                                                    */
/* (c) Copyright IBM Corp. 2018 All Rights Reserved                   */
/*                                                                    */
/* US Government Users Restricted Rights - Use, duplication or        */
/* disclosure restricted by GSA ADP Schedule Contract with IBM Corp   */
/*                                                                    */

/**
 * Interface for storing and retrieving string data.
 * 
 * @author Alexander D Brown
 * @author Ivan Hargreaves
 * @version 1.0.0
 */
public interface StorageService {
	/**
	 * Gets string data from storage.
	 * 
	 * @param id
	 *            The ID of the item to get.
	 * @return The data returned.
	 * @throws StorageException
	 */
	public String get(int id) throws StorageException;
	
	/**
	 * Puts string data into storage.
	 * 
	 * @param record
	 *            The string data to put into storage.
	 * @return The ID of the item that was put into storage.
	 * @throws StorageException
	 */
	public int put(String record) throws StorageException;
}
