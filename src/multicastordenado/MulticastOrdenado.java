/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicastordenado;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Renato Correa
 */
public class MulticastOrdenado {
    
    private static final int NUM_PROCESSES = 1;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // Server
        
        int clock = 0;
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Id: ");
        int id = scanner.nextInt();
        
        ServerThread serverThread = new ServerThread(id);
        serverThread.start();
        
        System.out.println("Esperando inicialização dos servidores.");
        System.out.print("Digite algum número para continuar: ");
        scanner.nextInt();
        
        // Clients
        
        Socket[] sockets = new Socket[NUM_PROCESSES];
        for (int i = 0; i < NUM_PROCESSES; ++i)
            sockets[i] = new Socket("localhost", 3031 + i);
        
        OutputStreamWriter[] streams = new OutputStreamWriter[NUM_PROCESSES];
        for (int i = 0; i < NUM_PROCESSES; ++i)
            streams[i] = new OutputStreamWriter(sockets[i].getOutputStream());
        
        BufferedWriter[] writers;
        writers = new BufferedWriter[NUM_PROCESSES];
        for (int i = 0; i < NUM_PROCESSES; ++i)
            writers[i] = new BufferedWriter(streams[i]);
        serverThread.writers = writers;
        
        while (true) {
            synchronized (System.out) {
                System.out.print("Mensagem: ");
            }
            String message = scanner.next();
            
            for (int i = 0; i < NUM_PROCESSES; ++i){
                writers[i].write(message + "\n");
                writers[i].write(Integer.toString(clock) + "\n"); // Enviar o tempo
                writers[i].write(Integer.toString(id) + "\n"); // Enviar o id do remetente
                writers[i].write(Integer.toString(id) + "\n"); // Enviar o ack
                
                writers[i].flush();
            }   
        }
    }
    
}
