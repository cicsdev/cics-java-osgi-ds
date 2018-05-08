package com.ibm.cicsdev.osgi.ds.cicsapp;

import org.osgi.service.component.ComponentContext;

import com.ibm.cics.server.CicsException;
import com.ibm.cics.server.DataHolder;
import com.ibm.cics.server.EndOfChainIndicatorException;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.TerminalPrincipalFacility;
import com.ibm.cicsdev.osgi.ds.storage.StorageException;
import com.ibm.cicsdev.osgi.ds.storage.StorageService;

/**
 * Class containing the entry point for the OSGi declarative services (DS)
 * sample.
 * 
 * @author Alexander D Brown
 * @author Ivan Hargreaves
 */
public class CICSApp
{
	/** The storage service which should be bound by DS */
	private static StorageService storage;

	/** True if the storage service is currently bound */
	private static boolean bound = false;

	public static void main(String[] args) throws StorageException
	{
		// Get the CICS Task as an object
		Task task = Task.getTask();

		// Show a warning message if no service is currently bound.
		if (!bound)
		{
			task.err.println("No service is bound.");
		}

		// Get the parameters
		String action = getAction(task);

		if (action == null)
		{
			task.err.println("CICSApp was not called from a terminal.");
			return;
		}

		String[] params = action.split(" ", 2);

		// Perform the action based on the first word of the input
		switch (params[1])
		{
		case "PUT":
			// Put the data into the storage service.
			System.out.println("Putting using bound service: " + storage);
			task.out.println(" - Created: " + storage.put(params[2]));
			break;
		case "GET":
			// Retrieve the data from the storage service
			int id = Integer.parseInt(params[2]);
			System.out.println("Getting " + id + " using bound service: " + storage);
			task.out.println(" - " + storage.get(id));
			break;
		default:
			task.out.println(" - Invalid argument: " + params[1]);
		}
	}

	/**
	 * Called by DS when this is first activated.
	 */
	public void activate(ComponentContext cc)
	{
		System.out.println("Starting temporary storage application.");
	}

	/**
	 * Called by DS when this is modified.
	 */
	public void modified(ComponentContext cc)
	{
		System.out.println("Temporary storage application was modified. Current temporary storage service: " + storage);
	}

	/**
	 * Called by DS when this is deactivated.
	 */
	public void deactivate(ComponentContext cc)
	{
		System.out.println("Stopping temporary storage application.");
	}

	/**
	 * Binds a storage service to this class. Called by DS.
	 * 
	 * @param storage
	 *            The new storage service.
	 */
	public void bindStorageService(StorageService storage)
	{
		System.out.println("Binding " + storage);
		CICSApp.storage = storage;
		CICSApp.bound = true;
	}

	/**
	 * Unbinds a storage service from this class. Called by DS.
	 * 
	 * @param service
	 *            The storage service to unbind. May not be the currently bound
	 *            service.
	 */
	public void unbindStorageService(StorageService service)
	{
		System.out.println("Unbinding " + service);

		// If the service is the currently bound service
		if (service.equals(CICSApp.storage))
		{
			// Don't actually unbound the service as we may want to continue using it.
			// Instead set a flag to suggest that there's no service bound/
			CICSApp.bound = false;
		} else
		{
			System.out.println(service + " is already unbound");
		}
	}

	/**
	 * Retrieves the action from the terminal input.
	 * 
	 * @param task
	 *            The current CICS task.
	 * @return The action string from the terminal input. <code>null</code> if there
	 *         is no terminal to retrieve input from.
	 */
	private static String getAction(Task task)
	{
		// Get the terminal.
		TerminalPrincipalFacility tpf = getTPF(task);

		// Early return if not called from a terminal.
		if (tpf == null)
		{
			return null;
		}

		DataHolder data = new DataHolder();

		// Receive data from the terminal.
		try
		{
			tpf.receive(data);
		} catch (EndOfChainIndicatorException e)
		{
			// Expected exception - we've read everything possible
		} catch (CicsException e)
		{
			// All other exceptions just mean we should return early.
			return null;
		}

		// Return the data from the terminal.
		return data.getStringValue();

	}

	/**
	 * Get the Terminal Principal Facility.
	 * 
	 * @param task
	 *            The CICS task.
	 * @return The Terminal Principal Facility. <code>null</code> if there is no TPF.
	 */
	private static TerminalPrincipalFacility getTPF(Task task)
	{
		// Get the principal facility
		Object pf = task.getPrincipalFacility();

		// If it's a terminal principal facility, cast and return
		if (pf instanceof TerminalPrincipalFacility)
		{
			return (TerminalPrincipalFacility) pf;
		}

		// Otherwise, it's not a terminal pricipal facility and we should return null.
		return null;
	}
}
