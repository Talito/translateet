package rmi.server;

import java.rmi.RemoteException;

import rmi.base.Compute;
import rmi.base.Task;

public class ComputeTranslatorEngine implements Compute {

	@Override
	public <T> T executeTask(Task<T> t) throws RemoteException {
		System.out.println("got compute task: " + t);
		return t.execute();
	}
}