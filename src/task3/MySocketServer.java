package task3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This program implements a socket server that can handle multiple clients concurrently and filter messages
 * based on topic / subject
 *
 * @author ByteBuggers
 * @for SCS3203 - Middleware Architecture | Assignment 1 (Task 3)
 * @version 1.1
 * @since 2023-07-16
 */

public class MySocketServer {
    private static ServerSocket server;
    private static int port;

    private static final ArrayList<Client> subscribers = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java task3.MySocketServer <SERVER_PORT>");
            return;
        }

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
                String clientTopic = in.readLine();

                Client client = new Client(clientSocket, clientType, clientTopic);

                switch (clientType) {
                    case "PUBLISHER" -> {
                        System.out.println("Publisher connected for topic: " + clientTopic);

                        // create a new thread for the publisher
                        PublisherClientThread publisher = new PublisherClientThread(client);
                        publisher.start();
                    }
                    case "SUBSCRIBER" -> {
                        System.out.println("Subscriber connected");

                        // add client to subscribers list
                        subscribers.add(client);
                    }
                    default -> System.out.println("Unknown client type");
                }


            } catch (IOException e) {
                System.err.println("Error accepting client connection...");
                e.printStackTrace();
            }
        }
    }

    private static class Client {
        private final Socket socket;
        private final String clientType;
        private final String topic;

        public Client(Socket socket, String clientType, String topic) {
            this.socket = socket;
            this.clientType = clientType;
            this.topic = topic;
        }
    }

    private static class PublisherClientThread extends Thread {
        private final Client publisher;

        public PublisherClientThread(Client publisher) {
            this.publisher = publisher;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(publisher.socket.getInputStream()));

                String message = null;

                while ((message = in.readLine()) != null) {
                    System.out.println(message);

                    // send message to subscribers of topic
                    for (Client subscriber : subscribers) {
                        if (subscriber.topic.equals(publisher.topic)) {
                            PrintWriter out = new PrintWriter(subscriber.socket.getOutputStream(), true);
                            out.println(message);
                        }
                    }

                    if (message.equalsIgnoreCase("terminate")) {
                        // Close the client socket
                        publisher.socket.close();

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