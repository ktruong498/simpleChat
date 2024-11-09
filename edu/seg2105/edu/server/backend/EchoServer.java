package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import ocsf.server.*;

import java.io.IOException;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

	ChatIF serverUI;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) 
  {
    super(port);
	this.serverUI = serverUI;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	serverUI.display("Message received: " + msg + " from " + client.getInfo("loginID"));
	if (msg.toString().startsWith("#login")) {
		if (client.getInfo("loginID") == null) {
			String loginID = msg.toString().split(" ")[1];
			client.setInfo("loginID", loginID);
			String logonMsg = loginID + " has logged on.";
			serverUI.display(logonMsg);
			this.sendToAllClients(logonMsg);
		} else {
			try {
				client.sendToClient("Eroor: Client tried to login twice. Closing connection.");
				client.close();
			} catch (IOException e) {
				serverUI.display("Error when trying to close client");
			}
		}
	} else {
	    String sendMsg = client.getInfo("loginID") + "> " + msg;
	    this.sendToAllClients(sendMsg);
	}
  }

  public void handleMessageFromServerUI (String message) {
		if (message.charAt(0) == '#') {
			String[] args = message.split(" ");
			switch (args[0]) {
				case "#quit":
					serverUI.display("Quitting server");
					try {
						close();
					} catch (IOException e) {
						serverUI.display("Error while closing server");
						System.exit(1);
					}
					System.exit(0);
					break;
				case "#stop":
					serverUI.display("Stopping server");
					stopListening();
					break;
				case "#close":
					try {
						serverUI.display("Closing server");
						close();
					} catch (IOException e) {
						serverUI.display("Error while closing server");
						System.exit(1);
					}
					break;
				case "#setport":
					if (isListening() || getNumberOfClients() > 0) {
						serverUI.display("Please log off to change the host name");
					} else {
						try {
							setPort(Integer.parseInt(args[1]));
							serverUI.display("Setting port to " + args[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							serverUI.display("Please enter a port number");
						}
					}
					break;					
				case "#start":
					if (isListening() || getNumberOfClients() > 0){
						serverUI.display("Server already started");
					} else {
						try {
							listen();
						} catch ( IOException e) {
							serverUI.display("Error when trying to open connection");
						}
					}
					break;
				case "#getport":
					serverUI.display(String.valueOf(getPort()));
				default:
					break;
			}
		} else {
			String serverMsg = "SERVERMSG> " + message;
			sendToAllClients(serverMsg);
			serverUI.display(serverMsg);
		}
		
  }
  protected void clientConnected(ConnectionToClient client) {
	  serverUI.display("A new client has connected.");
  }

  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  serverUI.display(client.getInfo("loginID") + " has disconnected.");
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    serverUI.display
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    serverUI.display
      ("Server has stopped listening for connections.");
  }
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */

  /*
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      serverUI.display("ERROR - Could not listen for clients!");
    }
  }
  */
}
//End of EchoServer class
