package client;

import common.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ClientImplementation client = new ClientImplementation();
            ServerInterface stub = (ServerInterface) registry.lookup("Exam");
            client.getidentifier();
            stub.registerStudent(client, client.idenitifier);
            if (!client.exam_closed) {
                System.out.println("Client registrat, esperant notificació.");
                synchronized (client) {
                    client.wait();
                    int num_q = 0;
                    while(num_q < client.num_questions && !client.exam_finshed){
                        String quest = stub.getQuestion(client, num_q);
                        client.printQuestion(num_q+1, quest);
                        client.getAnswer();
                        stub.sendAnswer(client, client.idenitifier, num_q, client.answer);
                        num_q++;

                    }
                    stub.finishExam(client, client.idenitifier);
                }
            } else {
                System.err.println("No s'ha pogut entrar a l'examen.");
            }


        } catch (RemoteException e) {
            System.err.println("Excepció remota: " + e.toString()); e.printStackTrace();
        } catch (Exception e){
            System.err.println("Exepció del client: " + e.toString()); e.printStackTrace();
        }
    }
}
