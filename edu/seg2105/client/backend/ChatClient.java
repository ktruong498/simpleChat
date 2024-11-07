// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
		if (message.charAt(0) == '#') {
			String[] args = message.split(" ");	
			switch (args[0]) {
				case "#quit":
					quit();
					clientUI.display("Quitting now");
					System.exit(0);
					break;
				case "#logoff":
					clientUI.display("Logging off");
					quit();
					break;
				case "#sethost":
					if (isConnected()) {
						clientUI.display("Please log off to change the host name");
					} else {
						try {
							setHost(args[1]);
							clientUI.display("Setting host to " + args[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							clientUI.display("Please enter a host name");
						}
					}
					break;
				case "#setport":
					if (isConnected()) {
						clientUI.display("Please log off to change the port name");
					} else {
						try {
							setPort(Integer.parseInt(args[1]));
							clientUI.display("Setting port to " + args[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							clientUI.display("Please enter a host port");
						}
					}
					break;					
				case "#login":
					if (isConnected()){
						clientUI.display("Already logged in");
					} else {
						try {
							openConnection();
						} catch ( IOException e) {
							clientUI.display("Error when trying to open connection");
						}
					}
					break;
				case "#gethost":
					clientUI.display(getHost());
					break;
				case "#getport":
					clientUI.display(String.valueOf(getPort()));
				default:
					break;
			}
		} else {
			sendToServer(message);
		}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  /**
   * Method that is called when the connection has been closed.
   * Displays a message to the user and quits.
   */
  protected void connectionClosed() {
	  clientUI.display("The connection to the server has been closed");
	  //System.exit(0);
  }
  
  protected void connectionException(Exception exception) {
	  clientUI.display("The server has unexpectedly shut down:" + exception.getMessage());
	  System.exit(0);
  }
  
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
