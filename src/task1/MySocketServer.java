package task1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This program implements a socket server using SocketServer class
 *
 * @author ByteBuggers
 * @version 1.1
 * @for SCS3203 - Middleware Architecture | Assignment 1 (Task 1)
 * @since 2023-07-16
 */

public class MySocketServer {
    private static ServerSocket server;
    private static int port;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java task1.MySocketServer <SERVER_PORT>");
            return;
        }

        // get port number from command line argument
        port = Integer.parseInt(args[0]);

        // create a server socket
        try {
            // create socket and wait for client connection
            server = new ServerSocket(port);
            System.out.println("Server started and listening on port: " + port);
            System.out.println("Waiting for client connection...");
        } catch (IOException e) {
            System.err.println("Error starting socket server...");
            e.printStackTrace();
            return;
        }

        try {
            // accept connection from one client
            Socket socket = server.accept();
            System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;

            // read all incoming messages from client
            while ((message = inputReader.readLine()) != null) {
                System.out.println("Client: " + message);
                if (message.equalsIgnoreCase("terminate")) {
                    break;
                }
            }

            System.out.println("Client disconnected: " + socket.getInetAddress().getHostAddress());
            socket.close();

        } catch (IOException e) {
            System.err.println("Error accepting client connection...");
            e.printStackTrace();
        }

        // close the ServerSocket object
        try {
            System.out.println("Shutting down socket server...");
            server.close();
        } catch (IOException e) {
            System.err.println("Error closing socket server...");
            e.printStackTrace();
        }
    }
}