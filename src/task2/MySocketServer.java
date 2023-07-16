package task2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This program implements a socket server that can handle multiple clients concurrently
 *
 * @author Sandul Renuja
 * @version 1.0
 * @since 2023-07-16
 */

public class MySocketServer {
    private static ServerSocket server;
    private static int port;

    private static final ArrayList<Socket> subscribers = new ArrayList<>();

    public static void main(String[] args) {
        // get port number from command line argument
        port = Integer.parseInt(args[0]);

        // create a server socket
        try {
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for clients...");
        } catch (IOException e) {
            System.err.println("Error creating socket server...");
            e.printStackTrace();
            return;
        }

        // keep listening for new connections
        while (true) {

            // create socket and wait for client connection
            try {
                Socket clientSocket = server.accept();
                System.out.println("New client connected...");

                // get client type - publisher or subscriber
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String clientType = in.readLine();

                switch (clientType) {
                    case "PUBLISHER" -> {
                        System.out.println("Publisher connected");
                        // create a new thread for the publisher
                        PublisherClientThread publisher = new PublisherClientThread(clientSocket);
                        publisher.start();
                    }
                    case "SUBSCRIBER" -> {
                        System.out.println("Subscriber connected");
                        // add subscriber to the list
                        subscribers.add(clientSocket);
                    }
                    default -> System.out.println("Unknown client type");
                }


            } catch (IOException e) {
                System.err.println("Error accepting client connection...");
                e.printStackTrace();
            }
        }
    }

    private static class PublisherClientThread extends Thread {
        private final Socket socket;

        public PublisherClientThread(Socket clientSocket) {
            this.socket = clientSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String message;

                while ((message = in.readLine()) != null) {
                    System.out.println(message);


                    // send message to all subscribers
                    for (Socket subscriber : subscribers) {
                        PrintWriter out = new PrintWriter(subscriber.getOutputStream(), true);
                        out.println(message);
                    }

                    if (message.equalsIgnoreCase("terminate")) {
                        // Close the client socket
                        socket.close();

                        // close all subscriber sockets
//                        for (Socket subscriber : subscribers) {
//                            subscriber.close();
//                        }
                        break;
                    }
                }

            } catch (IOException e) {
                System.err.println("Error reading message from publisher...");
                e.printStackTrace();
            }
        }
    }
}