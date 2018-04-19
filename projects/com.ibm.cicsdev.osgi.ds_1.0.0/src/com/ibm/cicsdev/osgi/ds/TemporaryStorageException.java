package com.ibm.cicsdev.osgi.ds;

public class TemporaryStorageException extends Exception {
	private final int resp;
	private final int resp2;
	
	public TemporaryStorageException(Throwable t, int resp, int resp2) {
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
