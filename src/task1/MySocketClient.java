package task1;

import java.io.IOException;
import java.io.ObjectOutputStream;
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
    public static void main(String[] args) {
        // get ip address and port number from command line arguments
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Client started...");

        // get input from user and send to server
        while (true) {
            System.out.print("Enter message: ");
            String message = scanner.nextLine();

            try {
                // create a socket and object output stream
                Socket socket = new Socket(ip, port);

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                // write to socket using ObjectOutputStream
                oos.writeObject(message);

                // close resources
                oos.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // terminate the client if user sends terminate request
            if (message.equalsIgnoreCase("terminate")) {
                break;
            }
        }

        scanner.close();
        System.out.println("Shutting down client...");
    }
}