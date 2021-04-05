/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatyoutube;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Me
 */
public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 9000);       
        
        Scanner input = new Scanner(System.in);
        PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);

        ServerConnection serverConn = new ServerConnection(socket);
        
        new Thread(serverConn).start();
        
        while (true) {

            System.out.print("Enter A Message: ");
            String message = input.nextLine();

            if (message.contains("quit")) {
                break;
            }

            toServer.println(message);

        }
        socket.close();
        //fromServer.close();
    }

}
