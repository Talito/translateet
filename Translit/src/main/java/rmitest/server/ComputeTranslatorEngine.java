package rmitest.server;

import java.rmi.RemoteException;
import rmitest.base.Compute;
import rmitest.base.Task;

public class ComputeTranslatorEngine implements Compute {

	@Override
	public <T> T executeTask(Task<T> t) throws RemoteException {
		System.out.println("got compute task: " + t);
		return t.execute();
	}
}