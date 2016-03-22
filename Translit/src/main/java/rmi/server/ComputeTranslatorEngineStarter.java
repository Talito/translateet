package rmi.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import rmi.base.Compute;
import rmi.base.RmiStarter;

/**
 * start the server component. this exposes the an implementation of the Compute
 * interface as a service over RMI An RMI server program needs to create the
 * initial remote objects and export them to the RMI runtime, which makes them
 * available to receive incoming remote invocations.
 * 
 * @author srasul; adapted by Jose and Stijn
 * 
 */
public class ComputeTranslatorEngineStarter extends RmiStarter {
	
	private static Logger logger = Logger.getLogger(ComputeTranslatorEngineStarter.class.getName());
	
	@Override
	public void start() {
		try {
			logger.log(Level.INFO, "Starting ComputeTranslatorEngineStarter");
			Compute engine = new ComputeTranslatorEngine();
			//System.setProperty("java.rmi.server.hostname","192.168.1.2");
			logger.log(Level.INFO, "ComputeTranslatorEngine() created.");
			Compute engineStub = (Compute) UnicastRemoteObject.exportObject(engine, 0);
			logger.log(Level.INFO, "UnicastRemoteObject.export...() called.");
			Registry registry = LocateRegistry.getRegistry(); // default port 1099 for RMIRegistry
			logger.log(Level.INFO, "LocateRegistry.getRegistry() called.");
			registry.rebind(Compute.SERVICE_NAME, engineStub);
			logger.log(Level.INFO, "Service binded...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ComputeTranslatorEngineStarter();
	}
}