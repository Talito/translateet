package rmitest.client;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.netcomputing.webservices.datamodel.Text;
import org.netcomputing.webservices.datamodel.Translation;

import rmitest.base.Task;

/**
 * The CalcTranslation class implements the Task interface.
 *  For this example, the actual algorithm is not important. 
 *  What is important is that the algorithm is computationally
 * expensive, meaning that you would want to have it executed on a capable
 * server.
 */
public class CalcTranslation implements Task<Text>, Serializable {
	
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	private static final long serialVersionUID = 3942967283733335029L;

	private Text msg;
	private String langFrom, langTo;
	
	/**
	 * Construct a task to calculate translate between the specified languages.
	 */
	public CalcTranslation(Text msg, String langFrom, String langTo) {
		this.msg = msg;
		this.langFrom = langFrom;
		this.langTo = langTo;
	}
	/**
	 * Do the translation itself.
	 */
	public Text execute() {
		return computeComplexTranslation(msg, langFrom, langTo);
	}

	public Text computeComplexTranslation(Text msg, String langFrom, String langTo) {
		Translation t = new Translation();
		t.setText("Translation done by the AI");
		msg.getTranslations().add(t);
		logger.log(Level.INFO, "Got computed translation: " + t.getText());
		return msg;
	}

}
