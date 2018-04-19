package com.ibm.cicsdev.osgi.ds.tsq;

import java.util.ArrayList;
import java.util.List;

import com.ibm.cicsdev.osgi.ds.TemporaryStorageException;
import com.ibm.cicsdev.osgi.ds.TemporaryStorageService;

public class InMemoryStorageService implements TemporaryStorageService {
	private List<String> store = new ArrayList<>();
	
	public void activate() {
		System.out.println("Starting in-memory temporary storage.");
	}
	
	public void deactivate() {
		System.out.println("Stopping in-memory temporary storage");
		store.clear();
	}
	
	@Override
	public String get(int id) throws TemporaryStorageException {
		// We're off by one, so the actual index is one less.
		return store.get(id - 1);
	}
	
	@Override
	public int put(String record) throws TemporaryStorageException {
		store.add(record);
		return store.size();
	}
}
