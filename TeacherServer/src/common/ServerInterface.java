package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    public void registerStudent(ClientInterface client, String identifier) throws RemoteException;
    public void sendAnswer(ClientInterface client, String id, int question, String selected) throws RemoteException;
    public String getQuestion(ClientInterface client, int question) throws RemoteException;
    public void finishExam(ClientInterface client, String id) throws RemoteException;
}
