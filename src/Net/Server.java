package Net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import Client.Enemy;
import Client.PlayerMP;

/******************************
 * Server für den Multiplayer *
 ******************************/
public class Server implements Runnable {

	public ServerSocket serverSocket;
	private final List<Client> clients = new LinkedList<>();
	private ExecutorService executors = Executors.newCachedThreadPool();
	public static AtomicInteger player = new AtomicInteger(0);
	public static List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();
	public static Vector<Enemy> spawnedEnemys;
	ServerLogic sl;
	boolean running;
	
	public Server() throws IOException {
		serverSocket = new ServerSocket(1333);
		running = true;
		executors.execute(this);
		spawnedEnemys = new Vector<Enemy>();
		System.out.println("Server startet");
		sl = new ServerLogic();
		Client.read();
		Client.SpawnEnemy();
	}

	@Override
	public void run() {
		Client.tick = false;
		while (running) {
			try {
				if(player.get() != 2){
					System.out.println("Warten auf Clienten...");
					Socket newClient = serverSocket.accept();
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
			Client.tick = true;
		}
	}
}


/******************************************
 * Klasse zur bearbeitung der ServerLogik *
 ******************************************/
class ServerLogic implements Runnable{
	static int lvl = 1;
	int dis;
	double deltaX, deltaY;
	boolean resting, moving;
	private int mDirection = -1;
	private int mSteps = 0;
	
	public ServerLogic(){
		startThread();
	}
	
	private synchronized void startThread(){
		new Thread(this).start();
	}

	@Override
	public void run() {
		while(true){
			try {
				if(Server.spawnedEnemys.size() != 0){
					EnemyMove();					
				}			
				doLogic();					
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void EnemyMove(){
		double[] x = {0,0} ;
		double[] y = {0,0};
		int index = 0;
		int hx = 0, hy = 0;
		for(PlayerMP p : Server.connectedPlayers){
			if(p != null){
				x[index] = p.getX();
				y[index] = p.getY();
			}
			index++;
		}
		index = 0;
		for(Enemy e : Server.spawnedEnemys){
			if(Math.abs(e.getX()-x[0]) <Math.abs(e.getX()-x[1])){
				hx = (int)x[0];
			}else if(Math.abs(e.getX()-x[0]) > Math.abs(e.getX()-x[1])) {
				hx = (int)x[1];		
			}
			if(Math.abs(e.getY()-y[0]) < Math.abs(e.getY()-y[1])){
				hy = (int)y[0];
			}else if(Math.abs(e.getY()-y[0]) > Math.abs(e.getY()-y[1])){
					hy = (int)y[1];
			}
			
			deltaX =Math.abs(e.getX()/16-hx/16);
			deltaY =Math.abs(e.getY()/16-hy/16);
			dis =(int) Math.sqrt((deltaX*deltaX)+(deltaY*deltaY));
			
			if(dis <= 4){
				chasePlayer(e.getX(), e.getY(), hx , hy, e);			
			}else{
				if(!resting){
					moving = true;
					moveRandom(e.getX(), e.getY(),e);
					resting = true;					
				}
			}
				resting = false;
		}
	}
	
	private void moveRandom(double x, double y, Enemy e) {
			if (mDirection == -1) {
				mDirection = (int) (Math.random() * 4);
			}
			
			if (mSteps < 10) {
				moveene(mDirection, x, y, e);
				mSteps++;
			} else {
				mDirection = -1;
				mSteps = 0;
			}
	}
	
	private void moveene(int i, double x, double y, Enemy e){
			switch(i){
			case 0:
					if (Client.leveldata[((int) (y / 16)) - 1][((int) (x / 16))] == 1) {
						mDirection = -1;
					} else if (Client.leveldata[((int) (y / 16)) - 1][((int) (x / 16))] == 4) {
						mDirection = -1;
					} else
					y--;
					e.setY(y);
				break;
			case 1:		
					if (Client.leveldata[((int) (y / 16)) + 1][((int) (x / 16))] == 1) {
						mDirection = -1;
					} else if (Client.leveldata[((int) (y / 16)) + 1][((int) (x / 16))] == 4) {
						mDirection = -1;
					} else
					y++;
					e.setY(y);
				break;
			case 2:		
					if (Client.leveldata[((int) (y / 16))][((int) (x / 16) + 1)] == 1) {
						mDirection = -1;
					} else if (Client.leveldata[((int) (y / 16))][((int) (x / 16) + 1)] == 4) {
						mDirection = -1;
					} else
					x++;
					e.setX(x);
				break;
			case 3:		
					if (Client.leveldata[((int) (y / 16))][((int) (x / 16) - 1)] == 1) {
						mDirection = -1;
					} else if (Client.leveldata[((int) (y / 16))][((int) (x / 16) - 1)] == 4) {
						mDirection = -1;
					} else
					x--;
					e.setX(x);
				break;
			}
	}
	
	private void chasePlayer(double x, double y, double px, double py, Enemy e) {
		
				if (y > py) {
				MoveYEh(x,y,e);

				}
				if (y < py) {
				MoveYEd(x,y,e);
				}
				if (x > px) {
				MoveXEl(x,y,e);
				}
				if (x < px) {
				MoveXEr(x,y,e);
				}	
				
	}
	
	private void MoveYEh(double x, double y, Enemy e) {

		int oldY = ((int) (y / 16));

		if (Client.leveldata[((int) (y / 16))- 1][((int) (x / 16)) ] == 1) {
			y = oldY * 16;
			e.setY(y);
		} else if (Client.leveldata[((int)(y/16))-1][((int)(x/16))]== 2 || Client.leveldata[((int) (y / 16))-1][((int) (x / 16))] == 3 || Client.leveldata[((int) (y / 16) - 1)][((int) (x / 16))] == 4) {
			y = oldY * 16;
			e.setY(y);
		} else
			y--;
			e.setY(y);
		oldY = (int) y;

	}
	private void MoveYEd(double x, double y, Enemy e) {

		int oldY = ((int) (y / 16));

		if (Client.leveldata[((int) (y / 16)) + 1][((int) (x / 16))] == 1) {
			y = oldY * 16;
			e.setY(y);
		} else if (Client.leveldata[((int)(y/16))+1][((int)(x/16))]== 2 || Client.leveldata[((int) (y / 16))+1][((int) (x / 16))] == 3 || Client.leveldata[((int) (y / 16)) + 1][((int) (x / 16))] == 4) {
			y = oldY * 16;
			e.setY(y);
		} else
			y++;
			e.setY(y);
		oldY = (int) y;

	}
	private void MoveXEr(double x, double y, Enemy e) {

		int oldX = ((int) (x / 16));

		if (Client.leveldata[((int) (y / 16))][((int)(x / 16)) + 1] == 1) {
			x = oldX * 16;
			e.setX(x);
		} else if (Client.leveldata[((int)(y/16))][((int)(x/16))+1]== 2 || Client.leveldata[((int) (y / 16))][((int) (x / 16)) + 1] == 3 || Client.leveldata[((int) (y / 16))][((int) (x / 16)) + 1] == 4) {
			x = oldX * 16;
			e.setX(x);
		} else
			x++;
			e.setX(x);
		oldX = (int)x;

	}
	private void MoveXEl(double x, double y, Enemy e) {

		int oldX = ((int) (x / 16));

		if (Client.leveldata[((int) (y / 16))][((int) (x / 16)) - 1] == 1) {
			x = oldX * 16;
			e.setX(x);
		} else if (Client.leveldata[((int)(y/16))][((int)(x/16))-1]== 2 || Client.leveldata[((int) (y / 16))][((int) (x / 16)) - 1] == 3 || Client.leveldata[((int) (y / 16))][((int) (x / 16)) - 1] == 4) {
			x = oldX * 16;
			e.setX(x);
		} else
			x--;
			e.setX(x);
		oldX = (int)x;

	}
	
	
	private synchronized void doLogic() {
		if(Server.connectedPlayers.size() != 0){
			int index = 0;
			double[] x = {0,0};
			double[] y = {0,0};
			for(PlayerMP p : Server.connectedPlayers){
				if(p != null){
					x[index] = p.getX();
					y[index] = p.getY();
				}
				index++;
			}
			
			if (Client.leveldata[(int)x[0]/16][(int)y[0]/16] == 9 && Client.leveldata[(int)x[1]/16][(int)y[1]/16] == 92) {
				lvl++;
				Client.read();
				Client.SpawnEnemy();
			}
			if (Client.leveldata[(int)x[0]/16][(int)y[0]/16] == 8 && Client.leveldata[(int)x[1]/16][(int)y[1]/16] == 82) {
				lvl--;
				Client.read();
			}
			if (Client.leveldata[(int)x[0]/16][(int)y[0]/16] == 42 && Client.leveldata[(int)x[1]/16][(int)y[1]/16] == 42) {
				System.out.println("Finished");

			}
		}
	}
	
}