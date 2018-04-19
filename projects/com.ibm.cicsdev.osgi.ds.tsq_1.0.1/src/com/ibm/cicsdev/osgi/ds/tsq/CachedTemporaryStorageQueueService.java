package com.ibm.cicsdev.osgi.ds.tsq;

import java.util.HashMap;
import java.util.Map;

import com.ibm.cics.server.CICSExecutorService;
import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.ItemHolder;
import com.ibm.cics.server.TSQ;
import com.ibm.cicsdev.osgi.ds.TemporaryStorageException;
import com.ibm.cicsdev.osgi.ds.TemporaryStorageService;

public class CachedTemporaryStorageQueueService implements TemporaryStorageService {
	private static final String TSQ_NAME = "TSQS";

	private Map<Integer, String> localCache = new HashMap<>();

	public void activate() {
		System.out.println("Starting background cache");
		CICSExecutorService.runAsCICS(new BackgroundCache(localCache));
	}

	public void deactivate() {
		localCache.clear();
	}

	private static TSQ getTSQ() {
		TSQ tsq = new TSQ();
		tsq.setName(TSQ_NAME);

		return tsq;
	}

	@Override
	public String get(int id) throws TemporaryStorageException {
		if (!localCache.containsKey(id)) {
			final ItemHolder holder = new ItemHolder();

			try {
				getTSQ().readItem(id, holder);
			} catch (CicsConditionException e) {
				throw new TemporaryStorageException(e, e.getRESP(), e.getRESP2());
			}

			final String record = holder.getStringValue();
			localCache.put(id, record);

			return record;
		}

		return localCache.get(id);
	}

	@Override
	public int put(String record) throws TemporaryStorageException {
		try {
			final int id = getTSQ().writeString(record);
			localCache.put(id, record);
			return id;
		} catch (CicsConditionException e) {
			throw new TemporaryStorageException(e, e.getRESP(), e.getRESP2());
		}
	}
	
	public static class BackgroundCache implements Runnable {
		private final Map<Integer, String> cache;
		public BackgroundCache(Map<Integer, String> cache) {
			this.cache = cache;
		}
		
		@Override
		public void run() {
			TSQ tsq = getTSQ();
			
			final ItemHolder holder = new ItemHolder();
			int id = 1;
			while(true) {
				try {
					System.out.println("Caching item: " + id);
					
					tsq.readNextItem(holder);
					cache.put(id, holder.getStringValue());
					
					id++;
					
					Thread.yield();
				} catch(CicsConditionException e) {
					break;
				}
			}
		}
	}
}
