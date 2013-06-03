package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import Client.GamePanel;


public class GameServer extends Thread {
	
	private DatagramSocket socket;
	private GamePanel parent;
	
	public GameServer(GamePanel parent) {
		this.parent = parent;
		try {
			this.socket = new DatagramSocket(1337);
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}
	
	public void run(){
		while(!Thread.interrupted()){
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String message = new String(packet.getData());
			System.out.println("CLIENT ["+packet.getAddress().getHostAddress()+":"+packet.getPort()+"] > " + message.trim()); 
			if(message.trim().equalsIgnoreCase("ping")){
				System.out.println(packet.getAddress() + ":" + packet.getPort());
				sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
			}
		}
	}
	
	public void sendData(byte[] data, InetAddress ipAddress, int port){
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
