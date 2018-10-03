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
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // Read node id
        System.out.print("Id: ");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        scanner.nextLine();
        
        // Initialize node
        Node node = new Node(id);
        
        // Initialize server first
        node.initializeServer();
        
        // Wait for all servers to be initialized
        System.out.println("Esperando inicialização dos servidores.");
        System.out.print("Digite enter para continuar.");
        scanner.nextLine();
        
        // Initialize clients later
        node.initializeClients();
        
        // Handle message writing
        while (true) {
            // Read user input message
            String messageText = scanner.nextLine();
            
            // Multicast the message
            node.multicastTextMessage(messageText);
        }
    }
}
