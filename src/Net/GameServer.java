package Net;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import Client.*;
import Net.packets.Packet;
import Net.packets.Packet.PacketTypes;
import Net.packets.Packet00Login;
import Net.packets.Packet01Disconnect;
import Net.packets.Packet02Move;
import Net.packets.Packet03Map;

public class GameServer extends Thread {

    private DatagramSocket socket;
    private GamePanel game;
    private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();
    int [][] leveldata;
    
    public GameServer(GamePanel game) {
        this.game = game;
        try {
            this.socket = new DatagramSocket(1337);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        read();
    }

    public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                    + ((Packet00Login) packet).getUsername() + " has connected...");
            PlayerMP player2 = new PlayerMP(game.pl2, ((Packet00Login)packet).getX(), ((Packet00Login)packet).getY()-16,((Packet00Login) packet).getUsername(), address, port, 100, game);
            this.addConnection(player2, (Packet00Login) packet);
            break;
        case DISCONNECT:
            packet = new Packet01Disconnect(data);
            System.out.println("SERVER < [" + address.getHostAddress() + ":" + port + "] "
                    + ((Packet01Disconnect) packet).getUsername() + " has left...");
            this.removeConnection((Packet01Disconnect) packet);
            break;
        case MOVE:
            packet = new Packet02Move(data);
            this.handleMove(((Packet02Move) packet));
            break;
        case MAP:
        	packet = new Packet03Map(data);
        	this.sendMapData(((Packet03Map) packet));
        }
    }

    public void addConnection(PlayerMP player, Packet00Login packet) {
        boolean alreadyConnected = false;
        for (PlayerMP p : this.connectedPlayers) {
            if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
                if (p.ipAddress == null) {
                    p.ipAddress = player.ipAddress;
                }
                if (p.port == -1) {
                    p.port = player.port;
                }
                alreadyConnected = true;
            } else {
                // relay to the current connected player that there is a new
                // player
                sendData(packet.getData(), p.ipAddress, p.port);

                // relay to the new player that the currently connect player
                // exists
                packet = new Packet00Login(p.getUsername(), p.x, p.y);
                sendData(packet.getData(), player.ipAddress, player.port);
            }
        }
        if (!alreadyConnected) {
            this.connectedPlayers.add(player);
        }
    }

    public void removeConnection(Packet01Disconnect packet) {
        this.connectedPlayers.remove(getPlayerMPIndex(packet.getUsername()));
        packet.writeData(this);
    }
    
    public void sendMapData(Packet03Map packet){
    	
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

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
            DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
            try {
                this.socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void sendDataToAllClients(byte[] data) {
        for (PlayerMP p : connectedPlayers) {
            sendData(data, p.ipAddress, p.port);
        }
    }

    private void handleMove(Packet02Move packet) {
        if (getPlayerMP(packet.getUsername()) != null) {
            int index = getPlayerMPIndex(packet.getUsername());
            PlayerMP player = this.connectedPlayers.get(index);
            player.setMovingDir(packet.getMovingDir());
            int movingDir = packet.getMovingDir();
            int moveY = (int) packet.y/16;
            int moveX = (int) packet.x/16;
            
            if (movingDir == 1) {

    			int oldY = moveY;
    			int oldX = moveX;

    			if (leveldata[moveY - 1][moveX] == 1|| leveldata[moveY - 1][moveX] == 11|| leveldata[moveY - 1][moveX] == 16) {
    				moveY = oldY;
    			} else if (leveldata[moveY - 1][moveX] == 4 || leveldata[moveY - 1][moveX] == 30|| leveldata[moveY-1][moveX] == 31|| leveldata[moveY-1][moveX] == 99) {
    				moveY = oldY;
    			} else if(leveldata[moveY -1][moveX]==9 /*&& EnemyCounter != 0*/){
    				moveY = oldY;
    			} else
    				moveY--;
    		}

    		if (movingDir == 3) {

    			int oldY = moveY;
    			int oldX = moveX;

    			if (leveldata[moveY + 1][moveX] == 1|| leveldata[moveY + 1][moveX] == 11|| leveldata[moveY + 1][moveX] == 16) {
    				moveY = oldY;
    			} else if (leveldata[moveY + 1][moveX] == 4|| leveldata[moveY + 1][moveX] == 30|| leveldata[moveY+1][moveX] == 31|| leveldata[moveY+1][moveX] == 99) {
    				moveY = oldY;
    			} else if(leveldata[moveY +1][moveX]==9 /*&& EnemyCounter != 0*/){
    				moveY = oldY;
    			}else
    				moveY++;
    		}

    		if (movingDir == 2) {

    			int oldY = moveY;
    			int oldX = moveX;

    			if (leveldata[moveY][moveX - 1] == 1|| leveldata[moveY][moveX-1] == 11|| leveldata[moveY][moveX-1] == 16) {
    				moveX = oldX;
    			} else if (leveldata[moveY][moveX - 1] == 4|| leveldata[moveY][moveX-1] == 30|| leveldata[moveY][moveX-1] == 31|| leveldata[moveY][moveX-1] == 99) {
    				moveX = oldX;
    			} else if(leveldata[moveY][moveX-1]==9 /*&& EnemyCounter != 0*/){
    				moveX = oldX;
    			}else
    				moveX--;
    		}

    		if (movingDir == 4) {

    			int oldY = moveY;
    			int oldX = moveX;

    			if (leveldata[moveY][moveX + 1] == 1|| leveldata[moveY][moveX+1] == 11|| leveldata[moveY][moveX+1] == 16) {
    				moveX = oldX;
    			} else if (leveldata[moveY][moveX + 1] == 4|| leveldata[moveY][moveX+1] == 30|| leveldata[moveY][moveX+1] == 31|| leveldata[moveY][moveX+1] == 99) {
    				moveX = oldX;
    			} else if(leveldata[moveY][moveX+1]==9 /*&& EnemyCounter != 0*/){
    				moveX = oldX;
    			}else
    				moveX++;
    		}     
            player.x = packet.x = moveX*16;
            player.y = packet.y = moveY*16;
            packet.writeData(this);
        }
    }
        
        public void read() {
    		try {
    			String sTemp;
    			String rest = null;
    			int i, j;
    			leveldata = new int[15][15];
    			BufferedReader oReader = new BufferedReader(new InputStreamReader(
    					new FileInputStream(new File("res/lvl/lvl1.level")))); // Zeile
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
        
        

}