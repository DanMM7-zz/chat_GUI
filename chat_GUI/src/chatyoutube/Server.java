/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatyoutube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Me
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    static ArrayList<ClientHandler> clients = new ArrayList<>();
    static ExecutorService executorPool = Executors.newCachedThreadPool();
    static BufferedReader fromClient;

    public static void main(String[] args) throws IOException {
        // TODO code application logic here

        ServerSocket server = new ServerSocket(9000);

        System.out.println("Server Started At " + new Date());

        try {
            while (true) {

                Socket socket = server.accept();
                fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String name = fromClient.readLine();
                ClientHandler clientThread = new ClientHandler(socket, clients, name);
                clients.add(clientThread);

                executorPool.execute(clientThread);

            }
        } catch (IOException ex) {

        } finally {

        }
    }

}
