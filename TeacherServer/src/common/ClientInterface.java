package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    public void sendGrade(String opc, int nota) throws RemoteException;
    public void notifyStart(int num_q) throws RemoteException;
    public void examStarted() throws RemoteException;
    public void examFinished() throws RemoteException;
}
