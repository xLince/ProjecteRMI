package client;


import common.ClientInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientImplementation extends UnicastRemoteObject implements ClientInterface {
    String idenitifier;
    boolean exam_closed = false;
    boolean exam_finshed = false;
    int num_questions;
    String answer;

    public ClientImplementation() throws RemoteException {
        super();
    }

    @Override
    public void sendGrade(String opc, int nota) throws RemoteException {
        if(!exam_finshed) {
            if (opc.equals("Acabat")) {
                System.out.println("Ja has acabat el examen.");
            } else {
                System.out.println("Se t'ha acabat el temps.");
            }
            System.out.println("Nota: " + nota);
        }
    }

    @Override
    public void notifyStart(int num_q) {
        System.out.println("El examen començarà ara.");
        num_questions = num_q;
        synchronized (this) {
            this.notify();
        }

    }
    @Override
    public void examStarted() {
        exam_closed = true;
    }

    @Override
    public void examFinished() {
        exam_finshed = true;
    }


    public synchronized void getidentifier(){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Entra el identificador:");
        idenitifier = keyboard.next();
    }

    public synchronized void getAnswer(){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Entra el numero de l'opció correcta:");
        answer = keyboard.next();

    }

    public void printQuestion(int num_q, String question){
        if(!exam_finshed) {
            String[] quest = question.split(", ");
            System.out.println("Pregunta " + num_q + ":");
            for (String s : quest) {
                System.out.println(s);
            }
        }
    }
}
