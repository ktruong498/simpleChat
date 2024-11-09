package edu.seg2105.edu.server.ui;


import java.util.Scanner;

import edu.seg2105.edu.server.backend.EchoServer;
import edu.seg2105.client.common.*;

public class ServerConsole implements ChatIF {
	
	EchoServer server;
	
	Scanner fromConsole;

	public ServerConsole(int port) {
		server = new EchoServer(port, this);
		fromConsole = new Scanner(System.in);
	}

	public void accept() {
		try {
			String message;
			while (true) {
				message = fromConsole.nextLine();
				server.handleMessageFromServerUI(message);
			}
		}
		catch(Exception e) {
			System.out.println("Unexpected error while reading from console!");
		}
	}

	public void display(String message) {
		if (message.startsWith("SERVERMSG")) {
			System.out.println(message);
		} else {
			System.out.println("> " + message);
		}	
	}

	public void displayFromUser(String message, String login) {
		System.out.println("SERVERMSG> " + message);
	}
	
	

	public static void main(String[] args) {
		int port;
		try {
			port = Integer.parseInt(args[0]);
		}
		catch(ArrayIndexOutOfBoundsException e) {
			port = EchoServer.DEFAULT_PORT;
		}

		ServerConsole serverUI = new ServerConsole(port);		
		try {
			serverUI.server.listen();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.out.println("ERROR - Could not listen for clients!");
		}
		serverUI.accept();
	}
}
