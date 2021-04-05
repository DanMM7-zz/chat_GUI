/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatyoutube;

import static chatyoutube.chatInterface.incomingArea;
import static chatyoutube.chatInterface.outgoingArea;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Me
 */
public class chatInterface extends Application {

    private Button connectButton = new Button("Connect");
    private Button disconnectButton = new Button("Disconnect");
    private Button sendButton = new Button("Send");

    static TextArea incomingArea = new TextArea();
    static TextArea outgoingArea = new TextArea();
    static Label userLbl = new Label("Username");

    boolean isConnected = false;
    Socket socket;
    PrintWriter toServer;
    ServerConnection serverConn;

    @Override
    public void start(Stage primaryStage) {

        outgoingArea.setPrefHeight(100);
        outgoingArea.setPrefWidth(300);

        incomingArea.setPrefHeight(250);

        GridPane topPane = new GridPane();
        GridPane bottomPane = new GridPane();

        topPane.add(connectButton, 1, 0);
        topPane.add(disconnectButton, 2, 0);
        topPane.add(new Label(""), 0, 1);

        bottomPane.add(new Label(""), 0, 0);
        bottomPane.add(outgoingArea, 0, 1);
        bottomPane.add(sendButton, 3, 1);
        bottomPane.add(userLbl, 0, 3);

        BorderPane mainPane = new BorderPane();

        mainPane.setTop(topPane);
        mainPane.setCenter(new ScrollPane(incomingArea));
        mainPane.setBottom(bottomPane);

        //ConnListener listenerObj = new ConnListener();
        connectButton.setOnAction(new ConnectListener());
        disconnectButton.setOnAction(new disConnectListener());
        sendButton.setOnAction(new SendListener());
        Scene scene = new Scene(mainPane);
        primaryStage.setTitle("Chat");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);

    }

    class ConnectListener implements EventHandler<ActionEvent> {

        public void handle(ActionEvent e) {

            if (isConnected != true) {
                try {
                    socket = new Socket("localhost", 9000);
                    toServer = new PrintWriter(socket.getOutputStream(), true);
                    incomingArea.appendText("Connected To Server");
                    isConnected = true;
                    String name = JOptionPane.showInputDialog("Enter Your Username: ");
                    userLbl.setText(name);
                    toServer.println(name);
                    toServer.flush();
                    serverConn = new ServerConnection(socket);
                    new Thread(serverConn).start();

                } catch (Exception ex) {
                    incomingArea.appendText("Cannot Connect To Server \n");
                }
            }
            else{
                incomingArea.appendText("Already Connected To Server");
            }
        }

    }

    class disConnectListener implements EventHandler<ActionEvent>{
        
        public void handle(ActionEvent e){
            
            try {
                
                incomingArea.appendText("\nDisconnected From Server");
                socket.close();
                isConnected = false;                
            } catch (IOException ex) {
                Logger.getLogger(chatInterface.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }
    
    class SendListener implements EventHandler<ActionEvent> {

        public void handle(ActionEvent e) {

            if ((outgoingArea.getText()).equals("")) {
                outgoingArea.setText("");
                outgoingArea.requestFocus();
            } else {
                try {
                    toServer.println(outgoingArea.getText());
                    toServer.flush();
                } catch (Exception ex) {
                    incomingArea.appendText("Message not sent. \n");
                }
                outgoingArea.setText("");
                outgoingArea.requestFocus();
            }

            outgoingArea.setText("");
            outgoingArea.requestFocus();

        }
    }

    class ServerConnection implements Runnable {

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
                    incomingArea.appendText("\n" + message);

                }
            } catch (IOException ex) {

            } finally {
                try {
                    fromServer.close();
                } catch (IOException ex) {
                    Logger.getLogger(chatyoutube.ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }

}
