package com.ibm.cicsdev.osgi.ds.storage;

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
