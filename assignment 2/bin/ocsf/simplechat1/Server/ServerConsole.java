
import java.io.*;
import ocsf.server.*;
import java.util.Scanner;

import common.ChatIF;
// New class for E50 KA
public class ServerConsole implements ChatIF {
    // The default port to listen on.
    final public static int DEFAULT_PORT = 354;

    // The instance of the server, used to perform different actions based on user input.
    EchoServer server;

    // Constructor that receives the server instance to allow direct communication.
    public ServerConsole(EchoServer server) {
        this.server = server;
    }

    // This method waits for input from the console and processes it.
    public void accept() {
        try {
            BufferedReader fromConsole =
                    new BufferedReader(new InputStreamReader(System.in));
            String message;

            while (true) {
                message = fromConsole.readLine();
                handleMessageFromServerConsole(message);
            }
        }
        catch (Exception ex) {
            System.out.println
                    ("Unexpected error while reading from console!");
        }
    }

    // This method handles the server-side input, allowing actions without modifying EchoServer.
    private void handleMessageFromServerConsole(String message) {
        // You can implement controls to check the nature of the message, whether it's a command or not.
        if (message.charAt(0) == '#') {
            // It's a command, parse it and execute the corresponding action.
            executeCommand(message);
        } else {
            // It's a regular message, just send it to all clients.
            server.sendToAllClients("SERVER MSG> " + message);
        }
    }

    // Method to parse and execute commands.
    private void executeCommand(String message) {
        // Here, parse the command and execute the necessary actions on the 'server' object.
        // For example, if you have a command to stop the server, you would call:
        // server.stopListening();
    }

    // This method is called when the server sends a message to the console.
    @Override
    public void display(String message) {
        System.out.println("> " + message);
    }

    // Server starter method.
    public static void main(String[] args) {
        int port = 0; // Port to listen on

        try {
            port = Integer.parseInt(args[0]); // Get port from command line
        } catch(Throwable t) {
            port = DEFAULT_PORT; // Set port to default
        }

        EchoServer sv = new EchoServer(port);

        try {
            sv.listen(); // Start listening for connections
        } catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
        }

        // Create the server console and start it.
        ServerConsole sc = new ServerConsole(sv);
        sc.accept(); // Wait for console data
    }
    //End of Server console class KA
}