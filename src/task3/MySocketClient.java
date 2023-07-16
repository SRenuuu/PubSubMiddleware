package task3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * This program implements a socket client using Socket class
 *
 * @author Sandul Renuja
 * @version 1.0
 * @since 2023-07-16
 */

public class MySocketClient {
    public static void main(String[] args) throws IOException {
        // get ip address, port number, client type and topic from command line arguments
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        String type = args[2];
        String topic = args[3];

        Scanner scanner = new Scanner(System.in);
        Socket socket = new Socket(ip, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // send client type and topic to server
        out.println(type);
        out.println(topic);

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
                System.out.println("Waiting for messages from topic: " + topic);
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