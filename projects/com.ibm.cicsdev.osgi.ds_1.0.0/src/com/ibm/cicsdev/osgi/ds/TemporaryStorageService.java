package com.ibm.cicsdev.osgi.ds;

public interface TemporaryStorageService {
	public String get(int id) throws TemporaryStorageException;
	public int put(String record) throws TemporaryStorageException;
}
