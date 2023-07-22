package task2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * This program implements a socket client using Socket class
 *
 * @author ByteBuggers
 * @for SCS3203 - Middleware Architecture | Assignment 1 (Task 2)
 * @version 1.1
 * @since 2023-07-16
 */

public class MySocketClient {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: java task2.MySocketClient <SERVER_IP> <SERVER_PORT>");
            return;
        }

        // get ip address, port number and client type from command line arguments
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        String type = args[2];

        Scanner scanner = new Scanner(System.in);
        Socket socket = new Socket(ip, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.println(type);

        switch (type) {
            case "PUBLISHER":
                while (true) {
                    // get message from user and send to server
                    System.out.print("Enter message: ");
                    String message = scanner.nextLine();
                    out.println(message);

                    // terminate the client if user sends terminate message
                    if (message.equalsIgnoreCase("terminate")) {
                        out.close();
                        socket.close();
                        break;
                    }
                }
                break;

            case "SUBSCRIBER":
                System.out.println("Waiting for messages...");
                String publisherMessage = null;

                // listen for messages from server and print
                while ((publisherMessage = in.readLine()) != null) {
                    System.out.println(publisherMessage);
                }
                break;

            default:
                System.out.println("Invalid client type");
                break;
        }

        // close resources
        socket.close();
        scanner.close();
        System.out.println("Shutting down client...");
    }
}