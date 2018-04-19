package com.ibm.cicsdev.osgi.ds.tsq;

import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.ItemHolder;
import com.ibm.cics.server.TSQ;
import com.ibm.cicsdev.osgi.ds.TemporaryStorageException;
import com.ibm.cicsdev.osgi.ds.TemporaryStorageService;

public class TemporaryStorageQueueService implements TemporaryStorageService {
	private static final String TSQ_NAME = "TSQS";

	public void activate() {
		System.out.println("Starting TSQ service");
	}

	public void deactivate() {
		System.out.println("Stopping TSQ service");
	}

	private static TSQ getTSQ() {
		TSQ tsq = new TSQ();
		tsq.setName(TSQ_NAME);

		return tsq;
	}

	@Override
	public String get(int id) throws TemporaryStorageException {
		final ItemHolder holder = new ItemHolder();

		try {
			getTSQ().readItem(id, holder);
		} catch (CicsConditionException e) {
			throw new TemporaryStorageException(e, e.getRESP(), e.getRESP2());
		}

		return holder.getStringValue();
	}

	@Override
	public int put(String record) throws TemporaryStorageException {
		try {
			final int id = getTSQ().writeString(record);
			return id;
		} catch (CicsConditionException e) {
			throw new TemporaryStorageException(e, e.getRESP(), e.getRESP2());
		}
	}
}
