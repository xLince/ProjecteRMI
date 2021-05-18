package server;

import common.ClientInterface;
import common.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {
    List<ClientInterface> students = new ArrayList<>();
    HashMap<ClientInterface, String> id_university = new HashMap<>();
    HashMap<String, Integer> notes = new HashMap<>();
    private boolean started = false;
    private String[] exam;

    public ServerImplementation() throws RemoteException {
        super();
    }

    public void examStarted (){
        this.started= true;
    }

    @Override
    public synchronized void registerStudent(ClientInterface client, String identifier) throws RemoteException {
        if (!this.started) {
            students.add(client);
            id_university.put(client, identifier);
            System.out.println("Estudiants en la sala: "+students.size());
            notes.put(identifier, 0);
            this.notify();
        }else{
            client.examStarted();
        }
    }

    @Override
    public synchronized void sendAnswer(ClientInterface client, String id, int question, String selected) throws RemoteException {
        String answer = exam[question].substring(exam[question].length() - 1);
        if (answer.equals(selected)) {
            notes.replace(id, (notes.get(id) + 1));
        }

    }

    @Override
    public synchronized String getQuestion(ClientInterface client, int question) throws RemoteException{
        String quest = exam[question];
        quest = quest.substring(0, quest.length() - 1);
        return quest;
    }

    @Override
    public synchronized void finishExam(ClientInterface client, String id) throws RemoteException{
        int nota = notes.get(id);
        students.remove(client);
        client.sendGrade("Acabat", nota);
    }

    public void notifyStart(int num_q, String[] ex){
        exam = ex;
        for(ClientInterface c : students){
            try{
                c.notifyStart(num_q);
            }catch(RemoteException e){
                System.out.println("No s'ha pogut contactar amb l'estudiant");
            }
        }
    }

    public void notifyFinish(){

        for(ClientInterface c : students){
            try{
                c.examFinished();
            }catch(RemoteException e){
                System.out.println("No s'ha pogut contactar amb l'estudiant");
            }
        }
    }
}
