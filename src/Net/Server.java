package Net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {

	private ServerSocket serverSocker;
	private final List<Client> clients = new LinkedList<>();
	private ExecutorService executors = Executors.newCachedThreadPool();
	public static AtomicInteger player = new AtomicInteger(0);
	public Server() throws IOException {
		serverSocker = new ServerSocket(1333);
		executors.execute(this);
		System.out.println("Server startet");
		Client.read();
	}

	@Override
	public void run() {

		while (true) {
			try {
				if(player.get() != 2){
					System.out.println("Warten auf Clienten...");
					Socket newClient = serverSocker.accept();
					System.out.println("Neuer Client");
					synchronized (clients) {
						Client client = new Client(newClient, true);
						client.clients = clients;
						clients.add(client);
						executors.execute(client);
						player.getAndIncrement();
					}
				} else {
					Thread.sleep(500);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}