package server;

import common.ClientInterface;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Server {

    private static final String SEPARATOR=";";
    private static String[] exam;
    private static int num_questions = 0;
    private static HashMap<String, Integer> notes_finals = new HashMap<>();


    private static Registry startRegistry(Integer port)
            throws RemoteException {
        if(port == null) {
            port = 1099;
        }
        try {
            Registry registry = LocateRegistry.getRegistry(port);
            registry.list( );
            return registry;
        }
        catch (RemoteException ex) {
            // No valid registry at that port.
            System.out.println("RMI registry cannot be located ");
            Registry registry= LocateRegistry.createRegistry(port);
            System.out.println("RMI registry created at port ");
            return registry;
        }
    }

    private static void reader() {
        String[] questions = new String[50];
        try (BufferedReader br = new BufferedReader(new FileReader("Exam.csv"))) {
            String line = br.readLine();
            while (line != null) {
                String[] question = line.split(SEPARATOR);
                questions[num_questions] = Arrays.toString(question);

                line = br.readLine();
                num_questions++;
            }

        } catch (Exception e) {
            System.err.println("No es pot accedir a l'arxiu");
            e.printStackTrace();
        }
        exam = new String[num_questions];
        System.arraycopy(questions, 0, exam, 0, num_questions);
        for(int k=0; k < exam.length; k++){
            exam[k] = exam[k].replaceAll("\\[", "").replaceAll("]","");
        }
    }
    private static void writer() {
        File file = new File("Notes_Examen.txt");
        BufferedWriter bf = null;
        try{
            //create new BufferedWriter for the output file
            bf = new BufferedWriter( new FileWriter(file) );
            bf.write("Alumne | Nota");
            bf.newLine();
            //iterate map entries
            for (Map.Entry<String, Integer> entry : notes_finals.entrySet()) {
                //put key and value separated by a colon

                bf.write(entry.getKey() + " | " + entry.getValue());
                //new line
                bf.newLine();
            }
            bf.flush();
        } catch (Exception e) {
            System.err.println("No s'ha pogut crear a l'arxiu");
            e.printStackTrace();
        }finally{
            try{
                //always close the writer
                assert bf != null;
                bf.close();
            }catch(Exception e){
                System.err.println("No es pot tancar a l'arxiu");
            }
        }
        System.out.println("Ja se t'ha creat el arxiu amb les notes.");
    }

    public static void main(String[] args) {
        try {
            String start_word = "start";
            String finish_word = "finish";
            Registry registry = startRegistry(null);
            ServerImplementation obj = new ServerImplementation();
            registry.bind("Exam",obj);

            Interrupted.Interrupt interrupt = new Interrupted.Interrupt(obj, start_word);
            //The tread starts reading for the key
            interrupt.start();//define the semaphore object
            reader();
            synchronized (obj){
                while(!interrupt.interrupted){
                    System.out.println("Escriu \""+ start_word+"\" per començar l'examen:");
                    obj.wait();
                }
            }
            obj.examStarted();
            obj.notifyStart(num_questions, exam);

            Interrupted.Interrupt interrupt1 = new Interrupted.Interrupt(obj, finish_word);
            interrupt1.start();
            synchronized (obj){
                while(!interrupt1.interrupted){
                    System.out.println("Escriu \""+ finish_word+"\" per acabar l'examen:");
                    obj.wait();
                }
            }

            for(ClientInterface client : obj.students){
                int nota = obj.notes.get(obj.id_university.get(client));
                client.sendGrade("Temps", nota);
            }
            obj.notifyFinish();
            notes_finals = obj.notes;
            writer();
        } catch (Exception e) {
            System.err.println("Excepció del Servidor: " + e.toString()); e.printStackTrace();
        }
    }
}
class Interrupted {

    static class Interrupt extends Thread {
        String interrupt_key = null;
        Object semaphore = null;
        boolean interrupted = false;

        Interrupt(Object semaphore, String interrupt_key){
            this.semaphore = semaphore;
            this.interrupt_key = interrupt_key;
        }

        public void run(){
            while (true) {
                //read the key
                Scanner scanner = new Scanner(System.in);
                String x = scanner.nextLine();
                if (x.equals(this.interrupt_key)) {
                    //if is the key we expect, change the variable, notify and return(finish thread)
                    synchronized (this.semaphore) {
                        interrupted = true;
                        this.semaphore.notify();
                        return;
                    }
                }
            }
        }
    }
}
