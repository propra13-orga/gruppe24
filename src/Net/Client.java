package Net;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import Client.GamePanel;
import Client.PlayerMP;
import Net.packets.Packet00Login;
import Net.packets.Packet01Disconnect;
import Net.packets.Packet02Move;

public class Client implements Runnable {
	List<Client> clients;
	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private boolean isServer;
	static GamePanel game;
	private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();
	static int[][] leveldata;
	public String userName;

	public List<Client> copyClients() {
		synchronized (clients) {
			return new ArrayList<>(clients);
		}
	}

	public void send(Object msg) {
		try {
			getOutput().writeObject(msg);
			getOutput().flush();
		} catch (Exception e) {
			try {
				getSocket().close();
			} catch (Exception e2) {
			}
		}
	}

	public void broadcast(Object msg, boolean self) {
		for (Client client : copyClients()) {
			if (client != this || self)
				try {
					client.getOutput().writeObject(msg);
					client.getOutput().flush();
				} catch (Exception e) {
					try {
						client.getSocket().close();
					} catch (IOException e1) {
					}
				}
		}
	}

	public void onRecv(Object message) throws Exception {
		if (isServer) {
			onServerRecv(copyClients(), message);
		} else {
			onClientRecv(message);
		}
	}

	protected void onClientClosed() {

	}

	protected void onServerClientClosed() {
		Packet01Disconnect o = new Packet01Disconnect(userName);

		System.out.println("SERVER < [" + socket.getInetAddress() + ":"
				+ socket.getPort() + "] "
				+ ((Packet01Disconnect) o).getUsername() + " has left...");

		Server.player.getAndDecrement();

		connectedPlayers.remove(getPlayerMPIndex(o.getUsername()));
		broadcast(o, false);
	}

	protected void onServerRecv(List<Client> clients, Object o)
			throws Exception {

		if (o instanceof Packet00Login) {
			System.out.println("[" + socket.getInetAddress() + ":"
					+ socket.getPort() + "] "
					+ ((Packet00Login) o).getUsername() + " has connected...");
			getCoor(o);
			System.out.println(((Packet00Login) o).getX() + ","
					+ ((Packet00Login) o).getY());
			PlayerMP player2 = new PlayerMP(game.pl2,
					((Packet00Login) o).getX(), ((Packet00Login) o).getY(),
					((Packet00Login) o).getUsername(), socket.getInetAddress(),
					socket.getPort(), 100, game);

			addConnection(player2, o);

		} else if (o instanceof Packet02Move) {
			this.handleMove(((Packet02Move) o), true);
		}

	}

	protected void onClientRecv(Object o) throws Exception {

		if (o instanceof Packet00Login) {
			handleinit((Packet00Login) o);
			if (!((Packet00Login) o).getUsername().equals(userName)) {
				handleLogin((Packet00Login) o, socket.getInetAddress(),
						socket.getPort());
			}
			System.out.println("LOGIN erhalten");
		} else if (o instanceof Packet01Disconnect) {
			System.out.println("CLIENT < [" + socket.getInetAddress() + ":"
					+ socket.getLocalPort() + "] "
					+ ((Packet01Disconnect) o).getUsername()
					+ " has left the world...");
			game.removePlayerMP(((Packet01Disconnect) o).getUsername());
		} else if (o instanceof Packet02Move) {
			handleMove((Packet02Move) o, false);
		}

	}

	@Override
	public void run() {
		try {
			while (true) {

				Object object = input.readObject();
				onRecv(object);

			}
		} catch (SocketException | EOFException e) {
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (clients != null) {
				synchronized (clients) {
					clients.remove(this);
				}
			}
			if (isServer)
				onServerClientClosed();
			else
				onClientClosed();
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

	@SuppressWarnings("static-access")
	public Client(InetAddress addr, int port, GamePanel p)
			throws UnknownHostException, IOException {
		this(new Socket(addr, port), false);
		this.game = p;
		new Thread(this).start();
	}

	Client(Socket socket, boolean isServer) throws IOException {
		this.socket = socket;
		this.isServer = isServer;
		socket.setTcpNoDelay(true);
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());
	}

	// SERVER TEIL

	public void getCoor(Object o) {
		for (int row = 0; row < leveldata.length; row++) {
			for (int col = 0; col < leveldata[row].length; col++) {
				if (Server.player.get() == 1) {
					if (leveldata[row][col] == 2) {
						((Packet00Login) o).setX(col * 16);
						((Packet00Login) o).setY(row * 16);
					}
				} else if (Server.player.get() == 2) {
					if (leveldata[row][col] == 22) {
						((Packet00Login) o).setX(col * 16);
						((Packet00Login) o).setY(row * 16);
					}
				}
			}
		}
	}

	public void addConnection(PlayerMP player, Object o) {
		boolean alreadyConnected = false;
		for (PlayerMP p : connectedPlayers) {
			if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
				if (p.ipAddress == null) {
					p.ipAddress = player.ipAddress;
				}
				if (p.port == -1) {
					p.port = player.port;
				}
				alreadyConnected = true;
				
			}else{
				userName = player.getUsername();

				// relay to the current connected player that there is a new
				// player
				System.out.println("Server sendet neuen Player");
				broadcast(o, true);

				// relay to the new player that the currently connect player
				// exists
				o = new Packet00Login(p.getUsername(), p.x, p.y);
				System.out.println("Server sendet existenten Player");
				send(o);
			}
		}
		if (!alreadyConnected) {
			this.connectedPlayers.add(player);
		}
	}

	public PlayerMP getPlayerMP(String username) {
		for (PlayerMP player : this.connectedPlayers) {
			if (player.getUsername().equals(username)) {
				return player;
			}
		}
		return null;
	}

	public int getPlayerMPIndex(String username) {
		int index = 0;
		for (PlayerMP player : this.connectedPlayers) {
			if (player.getUsername().equals(username)) {
				break;
			}
			index++;
		}
		return index;
	}

	private void handleMove(Packet02Move packet, boolean server) {
		if (server) {
			if (getPlayerMP(packet.getUsername()) != null) {
				int index = getPlayerMPIndex(packet.getUsername());
				PlayerMP player = this.connectedPlayers.get(index);
				player.setMovingDir(packet.getMovingDir());
				int movingDir = packet.getMovingDir();
				int moveY = (int) packet.y / 16;
				int moveX = (int) packet.x / 16;

				if (movingDir == 1) {

					int oldY = moveY;

					if (leveldata[moveY - 1][moveX] == 1
							|| leveldata[moveY - 1][moveX] == 11
							|| leveldata[moveY - 1][moveX] == 16) {
						moveY = oldY;
					} else if (leveldata[moveY - 1][moveX] == 4
							|| leveldata[moveY - 1][moveX] == 30
							|| leveldata[moveY - 1][moveX] == 31
							|| leveldata[moveY - 1][moveX] == 99) {
						moveY = oldY;
					} else if (leveldata[moveY - 1][moveX] == 9 /*
																 * &&
																 * EnemyCounter
																 * != 0
																 */) {
						moveY = oldY;
					} else
						moveY--;
				} else if (movingDir == 3) {

					int oldY = moveY;

					if (leveldata[moveY + 1][moveX] == 1
							|| leveldata[moveY + 1][moveX] == 11
							|| leveldata[moveY + 1][moveX] == 16) {
						moveY = oldY;
					} else if (leveldata[moveY + 1][moveX] == 4
							|| leveldata[moveY + 1][moveX] == 30
							|| leveldata[moveY + 1][moveX] == 31
							|| leveldata[moveY + 1][moveX] == 99) {
						moveY = oldY;
					} else if (leveldata[moveY + 1][moveX] == 9 /*
																 * &&
																 * EnemyCounter
																 * != 0
																 */) {
						moveY = oldY;
					} else
						moveY++;
				} else if (movingDir == 2) {

					int oldX = moveX;

					if (leveldata[moveY][moveX - 1] == 1
							|| leveldata[moveY][moveX - 1] == 11
							|| leveldata[moveY][moveX - 1] == 16) {
						moveX = oldX;
					} else if (leveldata[moveY][moveX - 1] == 4
							|| leveldata[moveY][moveX - 1] == 30
							|| leveldata[moveY][moveX - 1] == 31
							|| leveldata[moveY][moveX - 1] == 99) {
						moveX = oldX;
					} else if (leveldata[moveY][moveX - 1] == 9 /*
																 * &&
																 * EnemyCounter
																 * != 0
																 */) {
						moveX = oldX;
					} else
						moveX--;
				} else if (movingDir == 4) {

					int oldX = moveX;

					if (leveldata[moveY][moveX + 1] == 1
							|| leveldata[moveY][moveX + 1] == 11
							|| leveldata[moveY][moveX + 1] == 16) {
						moveX = oldX;
					} else if (leveldata[moveY][moveX + 1] == 4
							|| leveldata[moveY][moveX + 1] == 30
							|| leveldata[moveY][moveX + 1] == 31
							|| leveldata[moveY][moveX + 1] == 99) {
						moveX = oldX;
					} else if (leveldata[moveY][moveX + 1] == 9 /*
																 * &&
																 * EnemyCounter
																 * != 0
																 */) {
						moveX = oldX;
					} else
						moveX++;
				}

				player.x = packet.x = moveX * 16;
				player.y = packet.y = moveY * 16;

				broadcast(packet, true);
			}
		} else {
			game.movePlayer(packet.getUsername(), packet.getX(), packet.getY(),
					packet.getMovingDir());
		}
	}

	@SuppressWarnings("resource")
	public static void read() {
		try {
			String sTemp;
			int i, j;
			leveldata = new int[15][15];
			BufferedReader oReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File("res/lvl/test.level")))); // Zeile
																			// für
			// Zeile
			// einlesen

			i = 0;

			while ((sTemp = oReader.readLine()) != null) {

				// Zeile in Einzelteile zerlegen (wir trennen durch ;

				java.util.StringTokenizer stWerte = new StringTokenizer(sTemp,
						";");

				j = 0;

				// Nun eintragen in den Array. Es wird nicht überprüft, ob
				// die Grenzen überschritten werden!

				while (stWerte.hasMoreTokens()) {

					leveldata[i][j] = Integer.parseInt(stWerte.nextToken());
					j++;
				}
				i++;
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace(); // Fehler ausdrucken

		} catch (IOException e) {

			e.printStackTrace(); // Fehler ausdrucken

		}
	}

	// CLIENTEN TEIL
	private void handleLogin(Packet00Login packet, InetAddress address, int port) {
		System.out.println("[" + address.getHostAddress() + ":" + port + "] "
				+ packet.getUsername() + " has joined the game...");
		PlayerMP player2 = new PlayerMP(game.pl2,
				((Packet00Login) packet).getX(),
				((Packet00Login) packet).getY(), packet.getUsername(), address,
				port, 100, game);
		game.addPlayerMP(player2);
	}

	private void handleinit(Object o) {
		game.setStart(((Packet00Login) o).getUsername(),
				((Packet00Login) o).getX(), ((Packet00Login) o).getY());
	}

}