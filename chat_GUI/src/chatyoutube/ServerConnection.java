/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatyoutube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Me
 */

//fix formatting, did end of tutorial, think it works

public class ServerConnection implements Runnable{

    private Socket socket;
    private BufferedReader fromServer;

    public ServerConnection(Socket inServer) throws IOException {

        socket = inServer;
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void run() {

        try {
            while (true) {

                String message = fromServer.readLine();
                System.out.print("\n" + message);

            }
        } catch (IOException ex) {

        } finally {
            try {
                fromServer.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
