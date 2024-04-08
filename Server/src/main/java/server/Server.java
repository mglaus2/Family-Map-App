package server;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;
import handler.*;

public class Server {
	private static final int MAX_WAITING_CONNECTIONS = 12;
	private HttpServer server;
	private void run(String portNumber) {
		try {
			server = HttpServer.create(new InetSocketAddress(Integer.parseInt(portNumber)), MAX_WAITING_CONNECTIONS);
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}

		server.setExecutor(null);

		server.createContext("/", new FileHandler());
		server.createContext("/user/login", new LoginHandler());
		server.createContext("/user/register", new RegisterHandler());
		server.createContext("/clear", new ClearHandler());
		server.createContext("/fill/", new FillHandler());
		server.createContext("/load", new LoadHandler());
		server.createContext("/person/", new PersonWithPersonIDHandler());
		server.createContext("/person", new PersonHandler());
		server.createContext("/event/", new EventWithEventIDHandler());
		server.createContext("/event", new EventHandler());

		server.start();
	}

	public static void main(String[] args) {		
		String portNumber = args[0];
		new Server().run(portNumber);
	}
}

