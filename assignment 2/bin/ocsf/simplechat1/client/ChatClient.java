// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

public class ChatClient extends AbstractClient {
    // Instance variables **********************************************
    ChatIF clientUI;  // Reference to the interface type variable.
    // Constructors ****************************************************
    public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
        super(host, port); // Call the superclass constructor
        this.clientUI = clientUI;
        openConnection();
    }
    // Instance methods ************************************************
    public void handleMessageFromServer(Object msg) {
        clientUI.display(msg.toString());
    }
    public void handleMessageFromClientUI(String message) {
        // Check if the message is a command
        if (message.startsWith("#")) {
            handleCommand(message);
        } else {
            try {
                sendToServer(message);
            } catch (IOException e) {
                clientUI.display("Could not send message to server.  Terminating client.");
                quit();
            }
        }
    }
    /**
     * This method handles command from the client UI
     *
     * @param command The command from the UI.
     */
    private void handleCommand(String command) {
        try {
            switch (command.toLowerCase()) {
                case "#quit":
                    quit();
                    break;
                case "#logoff":
                    closeConnection();
                    break;
                case "#login":
                    if (!isConnected()) {
                        openConnection();
                    } else {
                        clientUI.display("You are already connected.");
                    }
                    break;
                case "#gethost":
                    clientUI.display("Current host: " + getHost());
                    break;
                case "#getport":
                    clientUI.display("Current port: " + getPort());
                    break;
                default:
                    if (command.startsWith("#sethost ")) {
                        if (!isConnected()) {
                            setHost(command.substring(9));
                        } else {
                            clientUI.display("You must be logged off to set the host.");
                        }
                    } else if (command.startsWith("#setport ")) {
                        if (!isConnected()) {
                            setPort(Integer.parseInt(command.substring(9)));
                        } else {
                            clientUI.display("You must be logged off to set the port.");
                        }
                    } else {
                        clientUI.display("Unknown command");
                    }
                    break;
            }
        } catch (IOException e) {
            clientUI.display("An error occurred: " + e.getMessage());
        }
    }
    // Overridden methods from AbstractClient
    protected void connectionClosed() {
        clientUI.display("Connection closed.");
    }
    protected void connectionException(Exception exception) {
        clientUI.display("Server has shut down unexpectedly.");
        quit();
    }
    public void quit() {
        try {
            closeConnection();
        } catch (IOException e) {
            // Not much we can do about this
        }
        System.exit(0);
    }
}