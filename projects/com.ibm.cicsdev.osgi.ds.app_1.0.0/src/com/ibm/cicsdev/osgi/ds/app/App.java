package com.ibm.cicsdev.osgi.ds.app;

import org.osgi.service.component.ComponentContext;

import com.ibm.cics.server.CicsException;
import com.ibm.cics.server.DataHolder;
import com.ibm.cics.server.EndOfChainIndicatorException;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.TerminalPrincipalFacility;
import com.ibm.cicsdev.osgi.ds.TemporaryStorageException;
import com.ibm.cicsdev.osgi.ds.TemporaryStorageService;

public class App {
	private static TemporaryStorageService ts;
	private static boolean bound = false;
	
	public void activate(ComponentContext cc) {
		System.out.println("Starting temporary storage application.");
	}
	
	public void modified(ComponentContext cc) {
		System.out.println("Temporary storage application was modified. Current temporary storage service: " + ts);
	}
	
	public void deactivate(ComponentContext cc) {
		System.out.println("Stopping temporary storage application.");
	}
	
	public void bindTemporaryStorageService(TemporaryStorageService ts) {
		System.out.println("Binding " + ts);
		App.ts = ts;
		App.bound = true;
	}
	
	public void unbindTemporaryStorageService(TemporaryStorageService ts) {
		System.out.println("Unbinding " + ts);
		
		if(ts.equals(App.ts)) {
			App.bound = false;
		} else {
			System.out.println(ts + " is already unbound");
		}
	}
	
	public static void main(String[] args) throws TemporaryStorageException {
		Task task = Task.getTask();
		
		if(!bound) {
			task.err.println("No service is bound.");
		}
		
		String[] params = getAction(task).split(" ");
		
		switch(params[1]) {
		case "PUT":
			System.out.println("Putting using bound service: " + ts);
			task.out.println(" - Created: " + ts.put(params[2]));
			break;
		case "GET":
			int id = Integer.parseInt(params[2]);
			System.out.println("Getting " + id + " using bound service: " + ts);
			task.out.println(" - " + ts.get(id));
			break;
		default:
			task.out.println(" - Invalid argument: " + params[1]);
		}
	}
	
	private static String getAction(Task task)  {
		TerminalPrincipalFacility tpf = getTPF(task);
		
		if(tpf == null) {
			return "PUT UNKNOWN";
		}

		final DataHolder data = new DataHolder();
		
		try {
			tpf.receive(data);
		} catch(EndOfChainIndicatorException e) {
			// Expected
		} catch(CicsException e) {
			return "PUT UNKNOWN";
		}
		
		return data.getStringValue();
		 
	}
	
	private static TerminalPrincipalFacility getTPF(Task task) {
		Object pf = task.getPrincipalFacility();
		
		if(pf instanceof TerminalPrincipalFacility) {
			return (TerminalPrincipalFacility) pf;
		}
		
		return null;
	}
}
