package task1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This program implements a socket server using ServerSocket class
 *
 * @author Sandul Renuja
 * @version 1.0
 * @since 2023-07-16
 */

public class MySocketServer {
    private static ServerSocket server;
    private static int port;

    public static void main(String[] args) {
        // get port number from command line argument
        port = Integer.parseInt(args[0]);

        // create a server socket
        try {
            server = new ServerSocket(port);
            System.out.println("Waiting for client connection...");
        } catch (IOException e) {
            System.err.println("Error creating socket server...");
            e.printStackTrace();
            return;
        }

        // keep listening for new connections
        while (true) {

            // create socket and wait for client connection
            try {
                Socket socket = server.accept();

                // read from socket to ObjectInputStream object
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                // convert ObjectInputStream object to String
                String message = (String) ois.readObject();
                System.out.println(message);

                // close resources
                ois.close();
                socket.close();

                // terminate the server if client sends terminate message
                if (message.equalsIgnoreCase("terminate")) {
                    break;
                }

            } catch (IOException e) {
                System.err.println("Error accepting client connection...");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.err.println("Error reading message from client...");
                e.printStackTrace();
            }
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