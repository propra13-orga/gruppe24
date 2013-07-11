package Net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Network {

	/*public static void main(String[] args) throws IOException {

		// SERVER SEITE
		Server s = new Server();

		// CLIENT SEITE
		Client c = new Client(InetAddress.getByName("localhost"), 1337);

		c.getOutput().writeObject("keyup");
	}*/

}

/*class Client implements Runnable {
	List<Client> clients;
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private boolean isServer;

	public List<Client> copyClients() {
		synchronized (clients) {
			return new ArrayList<>(clients);
		}
	}

	public void onRecv(Object message) throws Exception {
		if (isServer) {
			onServerRecv(copyClients(), message);
		} else {
			onClientRecv(message);
		}
	}

	public void onServerRecv(List<Client> clients, Object message)
			throws Exception {

		
	}

	public void onClientRecv(Object message) throws Exception {

		
		
	}

	@Override
	public void run() {
		try {
			while (true) {

				Object object = input.readObject();
				onRecv(object);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (clients != null) {
				synchronized (clients) {
					clients.remove(this);
				}
			}
		}
	}

	public ObjectOutputStream getOutput() {
		return output;
	}

	public boolean isServer() {
		return isServer;
	}

	public Socket getSocket() {
		return socket;
	}

	public Client(InetAddress addr, int port) throws UnknownHostException,
			IOException {
		this(new Socket(addr, port), false);
		new Thread(this).start();
	}

	Client(Socket socket, boolean isServer) throws IOException {
		this.socket = socket;
		this.isServer = isServer;
		socket.setTcpNoDelay(true);
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());
	}
}*/

/*class Server implements Runnable {

	private ServerSocket serverSocker;
	private final List<Client> clients = new LinkedList<>();
	private ExecutorService executors = Executors.newCachedThreadPool();

	public Server() throws IOException {
		serverSocker = new ServerSocket(1337);
		executors.execute(this);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket newClient = serverSocker.accept();
				System.out.println("Neuer Client");
				synchronized (clients) {
					Client client = new Client(newClient, true);
					client.clients = clients;
					clients.add(client);
					executors.execute(client);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}*/
