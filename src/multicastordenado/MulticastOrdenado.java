/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multicastordenado;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Renato Correa
 */
public class MulticastOrdenado {
    private static Scanner scanner;
    private static Node node;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // Read node id
        System.out.print("Id: ");
        scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        
        // Initialize node
        node = new Node(id);
        
        // Initialize server first
        node.initializeServer();
        
        // Wait for all servers to be initialized
        System.out.println("Esperando inicialização dos servidores.");
        System.out.print("Digite qualquer número para continuar: ");
        scanner.nextInt();
        
        // Initialize clients
        node.initializeClients();
        
        // Handle message writing
        handleMessageWriting();
    }
    
    private static void handleMessageWriting() throws IOException {
        while (true) {
            // Read user input message
            synchronized (System.out) {
                System.out.print("Mensagem a enviar: ");
            }
            String text = scanner.next();
            
            // Multicast the message
            node.multicastTextMessage(text);
        }
    }
}
