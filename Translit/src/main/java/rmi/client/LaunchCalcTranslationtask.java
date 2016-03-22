package rmi.client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.netcomputing.webservices.datamodel.Text;
import org.netcomputing.webservices.queues.q4user.DBQueue;
import org.netcomputing.webservices.queues.q4user.PacketFormatException;
import org.netcomputing.webservices.queues.q4user.TranslatedPacket;
import org.netcomputing.webservices.queues.q4user.TranslationPacket;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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
	
	//volatile Logger logger = Logger.getLogger(this.getClass().getName());
	
	private Text msg;
	private String langFrom, langTo;
	
	public LaunchCalcTranslationtask() {
		this.msg = new Text(); msg.setMessage("whatever");
		this.langFrom = "Spanish";
		this.langTo = "Polish";
	}
	
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
//			//logger.log(Level.INFO, "Got computed translation: " + translation.getTranslations() + "."
//					+ " Languages: " + langFrom + " > " + langTo);
			/**
			 * Send solution to DBQueue
			 */
		
			ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost("localhost"); //TODO: configure?
		    try {
				Connection connection = factory.newConnection();
				Channel channel = connection.createChannel();
				
				TranslatedPacket trans = new TranslatedPacket(langFrom, langTo,
						translation.getMessage(), msg.getMessage());
				
				channel.queueDeclare(DBQueue.DB, true, false, false, null);
				channel.basicPublish("", DBQueue.DB, null, trans.getPacket().getBytes("UTF-8"));
				
				channel.close();
				connection.close();
		    } catch (IOException e) {
		    	//logger.log(Level.SEVERE, "Reception failed", e);
			} catch (TimeoutException e) {
				//logger.log(Level.SEVERE, "Reception timed out", e);
			}
			
			//logger.log(Level.INFO, "DBQueue update() called by the AI...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new LaunchCalcTranslationtask().start();
	}

}
