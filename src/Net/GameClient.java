package Net;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import Client.*;
import Net.packets.Packet;
import Net.packets.Packet.PacketTypes;
import Net.packets.Packet00Login;
import Net.packets.Packet01Disconnect;
import Net.packets.Packet02Move;

public class GameClient extends Client {

    GameClient(Socket socket, boolean isServer) throws IOException {
		super(socket, isServer);
		// TODO Auto-generated constructor stub
	}

	private InetAddress ipAddress;
    private GamePanel gp;
    Client c;
    
    /*public GameClient(GamePanel gp, String ipAddress) {
    	//super.clients;
        this.gp = gp;
        c = new Client(InetAddress.getByName(), 1337);
    }*/
    
    public void run() {
        while (true) {
            DatagramPacket packet = new DatagramPacket(data, data.length);
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;
        switch (type) {
        default:
        case INVALID:
            break;
        case LOGIN:
            packet = new Packet00Login(data);
            handleLogin((Packet00Login) packet, address, port);
            break;
        case DISCONNECT:
            packet = new Packet01Disconnect(data);
            System.out.println("CLIENT < [" + address.getHostAddress() + ":" + port + "] " + ((Packet01Disconnect) packet).getUsername() + " has left the world...");
            gp.removePlayerMP(((Packet01Disconnect) packet).getUsername());
            break;
        case MOVE:
            packet = new Packet02Move(data);
            handleMove((Packet02Move) packet);
        }
    }

    public void sendData(byte[] data) {
            DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1337);
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void handleLogin(Packet00Login packet, InetAddress address, int port) {
        System.out.println("[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername() + " has joined the game...");
        PlayerMP player2 = new PlayerMP(gp.pl2, ((Packet00Login)packet).getX(), ((Packet00Login)packet).getY(), packet.getUsername(), address, port, 100, gp);
        gp.addPlayerMP(player2);
    }

    private void handleMove(Packet02Move packet) {
        gp.movePlayer(packet.getUsername(), packet.getX(), packet.getY(), packet.getMovingDir());
    }
    
    
    
    
    
    
    
    
}