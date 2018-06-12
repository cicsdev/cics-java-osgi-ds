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

public class StorageException extends Exception {
	private final int resp;
	private final int resp2;
	
	public StorageException(Throwable t, int resp, int resp2) {
		super(t);
		this.resp = resp;
		this.resp2 = resp2;
	}
	
	public int getResp() {
		return resp;
	}
	
	public int getResp2() {
		return resp2;
	}
}
