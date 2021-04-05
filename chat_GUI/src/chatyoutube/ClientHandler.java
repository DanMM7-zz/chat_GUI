/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatyoutube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Me
 */
public class ClientHandler implements Runnable{
    
    private Socket socket;
    private PrintStream toClient;
    private BufferedReader fromClient;
    private ArrayList<ClientHandler> clients;
    private String message, name;

    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients, String name) throws IOException{

        this.socket = socket;        
        this.clients = clients;
        this.name = name;
        toClient = new PrintStream(socket.getOutputStream());
        fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void run() {

        try {

//            address = fromClient.readLine();
//            System.out.println(address);

            while (true) {                
                
                message = fromClient.readLine();
                System.out.print("\n" + message);

                if(!message.isEmpty()){
                    
                        broadcastMessage(message);
                    
                }
                
                
//                toClient.print("\nReceived");
//                toClient.flush();

//                confirm = fromClient.readLine();
//                System.out.println(confirm);

            }

        } catch (IOException ex) {

        }

    }
  
    public void broadcastMessage(String message){
        
        for(ClientHandler client : clients){
            
            client.toClient.println(name + ": " + message);
        }
        
    }
    
    
    
    
}
