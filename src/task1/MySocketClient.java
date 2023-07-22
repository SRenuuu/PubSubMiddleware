package task1;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * This program implements a socket client using Socket class
 *
 * @author ByteBuggers
 * @for SCS3203 - Middleware Architecture | Assignment 1 (Task 1)
 * @version 1.1
 * @since 2023-07-16
 */

public class MySocketClient {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java task1.MySocketClient <SERVER_IP> <SERVER_PORT>");
            return;
        }

        // get ip address and port number from command line arguments
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Client started...");

        try {
            // create a socket connection to server
            Socket socket = new Socket(ip, port);
            System.out.println("Connected to server: " + ip + ":" + port);

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true);

            String message;

            // get inputs from user and send to server until user enters "terminate"
            while (true) {
                message = inputReader.readLine();
                outputWriter.println(message);

                if (message.equalsIgnoreCase("terminate")) {
                    break;
                }
            }

            System.out.println("Connection terminated.");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        scanner.close();
        System.out.println("Shutting down client...");
    }
}