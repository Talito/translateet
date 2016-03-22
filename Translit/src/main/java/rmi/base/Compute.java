package rmi.base;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Compute extends Remote {
    
    public static final String SERVICE_NAME = "ComputeTranslatorEngine";
    
    <T> T executeTask(Task<T> t) throws RemoteException;
}