package rmi.client;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.netcomputing.webservices.datamodel.Text;
import org.netcomputing.webservices.queues.q4user.DBQueue;

import rmi.base.Compute;
import rmi.base.RmiStarter;
/**
 * RMI Client : get the RMI Compute service and send a task to compute PI to N
 * digits
 * START THE RMIREGISTRY.EXE from your JAVA Runtime/bin directory first
 * and make sure the remote object classes can be found in the classpath of the registry.
 * @author srasul; adapted by Jose
 */
public class LaunchCalcTranslationtask extends RmiStarter {
	
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	private Text msg;
	private String langFrom, langTo;
	
	public LaunchCalcTranslationtask(Text msg, String langFrom, String langTo) {
		this.msg = msg;
		this.langFrom = langFrom;
		this.langTo = langTo;
	}
	
	@Override
	public void start() {
		try {
			Registry registry = LocateRegistry.getRegistry();
			Compute compute = (Compute) registry.lookup(Compute.SERVICE_NAME);
			CalcTranslation task = new CalcTranslation(msg, langFrom,  langTo);
			Text translation = compute.executeTask(task);
			logger.log(Level.INFO, "Got computed translation: " + translation.getTranslations() + "."
					+ " Languages: " + langFrom + " > " + langTo);
			/**
			 * Send solution to DBQueue
			 */
			DBQueue us = new DBQueue();
			us.updateDatabase();
			logger.log(Level.INFO, "DBQueue update() called by the AI...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
