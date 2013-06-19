package Client;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.ListIterator;
import javax.imageio.ImageIO;
import javax.swing.*;

import Server.*;

public class GamePanel extends JPanel implements Runnable, KeyListener, ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton b0;
	private JButton b1;
	private JButton b2;
	private JButton b3;

	JFrame frame;
	KeyEvent e;

	long delta = 0;
	long last = 0;
	long gameover = 0;
	long mreg;
	long l1 = System.currentTimeMillis() ;
	long l2 = System.currentTimeMillis();

	Player hero;
	Enemy ene, sli;
	NPC npc, shopowner;
	EnemyBoss bs;
	Tile ground;
	Tile ps;
	Tile ex;
	Tile wlb;
	Tile check;
	Tile plate1;
	Tile plate2;
	Tile plate3;
	Tile plate4;
	Item sword;
	Item item;
	TileBlock krist;
	TileBlock thun;
	TileBlock ball;
	TileBlock wl;
	TileBlock wt;
	MagicBolt mbb;
	MagicBolt mb;
	Vector<Sprite> actors;
	Vector<Sprite> painter;
	Vector<Sprite> collision;
	Vector<Sprite> painter3;
	Vector<Enviroment> enviroment;
	Vector<Enviroment> painter2;
	String gameov = "Game Over!";
	String finish = "Goal!";

	int[][] leveldata;
	int lvl = 1;
	int EnemyCounter = 0;
	int Coins = 0;
	int xx = 0, yy = 0;

	SoundLib soundlib;

	double x;
	double y;
	double phealth = 130;
	double armor = 100;
	double deltaX, deltaY;
	double deltaXs, deltaYs;

	boolean up;
	boolean down;
	boolean left;
	boolean right;
	boolean started;
	boolean status = false;
	boolean dead = false;
	boolean finished = false;
	boolean OoM = false;
	boolean ServerRunning = false;
	boolean join = false;
	boolean boss = false;
	boolean showtext = false;
	boolean checkp = false;
	boolean sup, sdown, sleft, sright;
	boolean init = true;
	boolean p1, p2, p3, p4;
	boolean bRoom;
	boolean reset = false;
	boolean walkthr = true;

	int dis, diss;
	int moveX, moveY;
	int moveEX, moveEY;
	int dir = 0;
	int oldX, oldY;
	int posx, posy;
	int savex, savey;
	int mana = 100;
	int page = 0;
	int life = 3;
	static int Tilesize = 16;
	static int Width = 15 * Tilesize;
	static int Height = 15 * Tilesize;

	public String Username;

	BufferedImage[] floor = loadPics("pics/floor.gif", 1);
	BufferedImage[] wall = loadPics("pics/wall.gif", 1);
	BufferedImage[] pstart = loadPics("pics/pstart.png", 1);
	BufferedImage[] water = loadPics("pics/water.gif", 2);
	BufferedImage[] exit = loadPics("pics/door.png", 4);
	BufferedImage[] player = loadPics("pics/player.png", 12);
	BufferedImage[] enemy = loadPics("pics/Enemy.png", 1);
	BufferedImage[] np = loadPics("pics/npc.png", 1);
	BufferedImage[] BS = loadPics("pics/boss2.png", 1);
	BufferedImage[] Bolt = loadPics("pics/Bolt.png", 3);
	BufferedImage[] IT = loadPics("pics/item.png", 1);
	BufferedImage[] cp = loadPics("pics/checkpoint.png", 2);
	BufferedImage[] sl = loadPics("pics/slime.png", 4);
	BufferedImage[] sw = loadPics("pics/sword.png", 13);
	BufferedImage[] kr = loadPics("pics/kristall.png", 6);
	BufferedImage[] thu = loadPics("pics/blitze.png", 5);
	BufferedImage[] bl = loadPics("pics/ball.png", 1);


	Thread th;
	JTextArea npc1 = new JTextArea(5,20);
	JTextArea shop = new JTextArea(5,20);
	
	

	private GameClient socketClient;

	public static void main(String[] args) {
		new GamePanel(Width, Height + 20);
	}

	public GamePanel(int w, int h) {
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.BLACK);

		frame = new JFrame("Hero Quest");
		frame.setLocation(960 - (Width / 2), 600 - (Height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		b0 = new JButton("Start");
		b1 = new JButton("Close");
		b2 = new JButton("Server");
		b3 = new JButton("Join");
		b0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				b0.setVisible(false);
				b1.setVisible(false);
				b2.setVisible(false);
				b3.setVisible(false);
				frame.pack();
				frame.requestFocus();
				startGame();

			}
		});
		b1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		b2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new GUI(50, 50);
			}
		});
		b3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				join = true;
				b0.setVisible(false);
				b1.setVisible(false);
				b2.setVisible(false);
				b3.setVisible(false);
				frame.pack();
				frame.requestFocus();
				startGame();
			}
		});

		frame.add(b0, BorderLayout.EAST);
		frame.add(b1, BorderLayout.WEST);
		frame.add(b2, BorderLayout.NORTH);
		frame.add(b3, BorderLayout.SOUTH);
		frame.add(this);
		frame.addKeyListener(this);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		TextBox();
		add(npc1);
		add(shop);
		TRun();

	}

	public void TextBox(){
		npc1.setLineWrap(true);
		npc1.setWrapStyleWord(true);
		npc1.setBackground(Color.black);
		npc1.setForeground(Color.white);
		npc1.setLocation(Width/2-40, Height/2);
		npc1.setVisible(false);
		npc1.setEnabled(false);
		shop.setLineWrap(true);
		shop.setWrapStyleWord(true);
		shop.setBackground(Color.black);
		shop.setForeground(Color.white);
		shop.setLocation(Width/2, Height/2);
		shop.setVisible(false);
		shop.setEnabled(false);
		
	}
	
	private synchronized void TRun() {
		th = new Thread(this);
		th.start();
	}

	private void doInitializations() {

		last = System.nanoTime();
		gameover = 0;

		actors = new Vector<Sprite>();
		collision = new Vector<Sprite>();
		enviroment = new Vector<Enviroment>();
		painter = new Vector<Sprite>();
		painter2 = new Vector<Enviroment>();
		painter3 = new Vector<Sprite>();

		soundlib = new SoundLib();
		soundlib.loadSound("test", "sounds/Test.wav");

		ground();
		SpawnPlayer();
		if (GUI.running || join) {
			socketClient.sendData("ping".getBytes());
			hero.Username = JOptionPane.showInputDialog("Please enter your Playername");
		}
		SpawnEnemy();
		SpawnNPC();
		started = true;

	}

	@Override
	public void run() {

		while (frame.isVisible()) {

			computeDelta();

			if (isStarted()) {
				checkKeys();
				doLogic();
				moveObjects();
				cloneVectors();
			}
			repaint();

			try {
				Thread.sleep(75);
			} catch (InterruptedException e) {
			}

		}
	}

	@SuppressWarnings("unchecked")
	private void cloneVectors() {
		painter = (Vector<Sprite>) actors.clone();
		painter2 = (Vector<Enviroment>) enviroment.clone();
		painter3 = (Vector<Sprite>) collision.clone();
	}

	private void moveObjects() {

		for (ListIterator<Sprite> it = actors.listIterator(); it.hasNext();) {
			Sprite r = it.next();
			r.move(delta);
		}

	}

	private void doLogic() {
	if(npc != null){	
		deltaX =Math.abs((npc.getX()/16)-(hero.getX()/16));
		deltaY =Math.abs(npc.getY()/16-hero.getY()/16);
	}if(shopowner != null){
		deltaXs =Math.abs((shopowner.getX()/16)-(hero.getX()/16));
		deltaYs =Math.abs(shopowner.getY()/16-hero.getY()/16);
	}
		
		dis =(int) Math.sqrt((deltaX*deltaX)+(deltaY*deltaY));
		diss =(int) Math.sqrt((deltaXs*deltaXs)+(deltaYs*deltaYs));
		
		for (ListIterator<Sprite> it = actors.listIterator(); it.hasNext();) {
			Sprite r = it.next();
			r.doLogic(delta);
			if (r.remove) {
				it.remove();
			}
		}
		for (ListIterator<Enviroment> ev = enviroment.listIterator(); ev
				.hasNext();) {
			Enviroment r = ev.next();
			r.doLogic(delta);
		}
		for (ListIterator<Sprite> co = collision.listIterator(); co.hasNext();) {
			Sprite r = co.next();
			r.doLogic(delta);
		}

		hero.setFrame(moveX * Tilesize, moveY * Tilesize - Tilesize, Tilesize,
				Tilesize);
		sword.setFrame(hero.getX()+xx, hero.getY()+yy, Tilesize, Tilesize);
		
		if(boss && bs.locked){
			if(bs.getX()+bs.getWidth()/2<hero.getX()&& bs.getX()+bs.getWidth()!= getWidth()-16){
					bs.MoveXEr();
			}
			if(bs.getX()+bs.getWidth()/2> hero.getX()+hero.getWidth()){
					bs.MoveXEl();
			}
			if(hero.intersects(bs.t)){	
				if(System.currentTimeMillis()-l1 > 600){
					createBoltBoss(bs.getX()+bs.getWidth()/2-5, bs.getY());
					l1 = System.currentTimeMillis();
				}		
			}				
		}	
		
		if (leveldata[moveY][moveX] == 9) {
			lvl++;
			read();
			Clear();
			ground();
			SpawnPlayer();
			SpawnEnemy();
			SpawnNPC();
			SpawnBoss();
			SpawnKristallRoom();
			p1=false;p2=false;p3=false;p4=false;
		}
		if (leveldata[moveY][moveX] == 8) {
			lvl--;
			read();
			actors.clear();
			enviroment.clear();
			collision.clear();
			ground();
			SpawnPlayer();
			moveX = savex;
			moveY = savey;
			SpawnNPC();
			SpawnKristallRoom();
			EnemyCounter = 0;
			p1=true;p2=true;p3=true;p4=true;
		}
		if (leveldata[moveY][moveX] == 42) {
			finished = true;
			stopGame();

		}
		if(check != null){
			if(check.getX()== hero.getX()&& check.getY()== hero.getY()+16){
				check.setLoop(1,1);
				checkp = true;
			}
		}

		for (int i = 0; i < actors.size(); i++) {
			for (int n = i + 1; n < actors.size(); n++) {
				Sprite s1 = actors.elementAt(i);
				Sprite s2 = actors.elementAt(n);

				s1.collidedWith(s2);
			}
		}
		for (int i = 0; i < actors.size(); i++) {
			for (int n = i + 1; n < collision.size(); n++) {
				Sprite s1 = actors.elementAt(i);
				Sprite s2 = collision.elementAt(n);

				s1.collidedWith(s2);
			}
		}
		
		if(mana > 0){
			OoM = false;
		}

		if (hero.remove && gameover == 0) {
			
			gameover = System.currentTimeMillis();
		}

		if (gameover > 0) {
			if (System.currentTimeMillis() - gameover > 3000) {
				if(checkp || life !=0){
					soundlib.stopLoopingSound();
					startGame();
				}else
					stopGame();
			}
		}
		
		if(EnemyCounter != 0 || init){
			ex.setLoop(0,0);
		}else{
			ex.setLoop(1, 3);
			ex.setLoop(3, 3);
		}
		if(checkp){
			check.setLoop(1,1);
		}
		if(lvl == 5){
			walkthr = false;
			ex.setLoop(0,0);
			/*if(!p1){
			if(hero.getX()==plate1.getX()&& hero.getY()+16==plate1.getY()){
				if(!p1&&!p2&&!p3&&!p4){
				p1 = true;
				}else{
					p1 = false; p2 = false; p3 = false; p4 = false;
					plate1.setLoop(0,0);plate2.setLoop(0,0);plate3.setLoop(0,0);plate4.setLoop(0,0);
					thun.setLoop(0,0);
				}
			}}
			if(!p2){
			if(hero.getX()==plate2.getX()&& hero.getY()+16==plate2.getY()){
				if(p1&& !p2 && !p3 && !p4){
					System.out.println("Geschaltet");
					p2 = true;
				}else{
					p1 = false; p2 = false; p3 = false; p4 = false;
					plate1.setLoop(0,0);plate2.setLoop(0,0);plate3.setLoop(0,0);plate4.setLoop(0,0);
					thun.setLoop(0,0);
				}
			}}
			if(!p3){
			if(hero.getX()==plate3.getX()&& hero.getY()+16==plate3.getY()){
				if(p1 && p2 && !p3 && !p4){
					p3 = true;
				}else{
					p1 = false; p2 = false; p3 = false; p4 = false;
					plate1.setLoop(0,0);plate2.setLoop(0,0);plate3.setLoop(0,0);plate4.setLoop(0,0);
					thun.setLoop(0,0);
				}
			}}
			if(!p4){
			if(hero.getX()==plate4.getX()&& hero.getY()+16==plate4.getY()){
				if(p1 && p2 && p3 && !p4){
					p4 = true;
				}else{
					p1 = false; p2 = false; p3 = false; p4 = false;
					plate1.setLoop(0,0);plate2.setLoop(0,0);plate3.setLoop(0,0);plate4.setLoop(0,0);
					thun.setLoop(0,0);
				}
			}}
			if(p1&&p2&&p3&&p4){
				ex.setLoop(1, 3);
				ex.setLoop(3, 3);
				walkthr = true;
			}else
				walkthr = false;
			if(p1){
				plate1.setLoop(1, 1);
				thun.setLoop(1,1);
			}
			if(p2){
				plate2.setLoop(1,1);
				thun.setLoop(2,2);
			}
			if(p3){
				plate3.setLoop(1,1);
				thun.setLoop(3,3);
			}
			if(p4){
				plate4.setLoop(1,1);
				thun.setLoop(4,4);
			}*/
		}
	}
	
	private void Clear(){
		actors.clear();
		enviroment.clear();
		collision.clear();
		painter.clear();
		painter2.clear();
		painter3.clear();
	}

	public void ground() {
		for (int row = 0; row < leveldata.length; row++) {
			for (int col = 0; col < leveldata[row].length; col++) {
				posy = row;
				posx = col;
				int rc = leveldata[row][col];
				if (rc == 0 || rc == 13 || rc == 16) {
					ground = new Tile(floor, posx * Tilesize, posy * Tilesize,
							1, this);
					enviroment.add(ground);
				} else if (rc == 1 || rc == 14) {
					wl = new TileBlock(wall, posx * Tilesize, posy * Tilesize,
							0, this);
					collision.add(wl);
				} else if (rc == 11) {
					wlb = new Tile(wall, posx * Tilesize, posy * Tilesize,
							0, this);
					enviroment.add(wlb);
				} else if (rc == 12) {
					check = new Tile(cp, posx * Tilesize, posy * Tilesize,
							0, this);
					enviroment.add(check);
					check.setLoop(0, 0);
				} else if (rc == 2) {
					ps = new Tile(pstart, posx * Tilesize, posy * Tilesize, 0,
							this);
					enviroment.add(ps);
				} else if (rc == 3) {
					ground = new Tile(floor, posx * Tilesize, posy * Tilesize,
							1, this);
					enviroment.add(ground);
				} else if (rc == 4) {
					wt = new TileBlock(water, posx * Tilesize, posy * Tilesize,
							500, this);
					collision.add(wt);
				} else if (rc == 7) {
					ground = new Tile(floor, posx * Tilesize, posy * Tilesize,
							0, this);
					enviroment.add(ground);
					savex = col;
					savey = row;
				} else if (rc == 8) {
					ps = new Tile(pstart, posx * Tilesize, posy * Tilesize, 0,
							this);
					enviroment.add(ps);
				} else if (rc == 9) {
					ex = new Tile(exit, posx * Tilesize, posy * Tilesize, 0,
							this);
					enviroment.add(ex);
				} else if (rc == 30 || rc == 31 || rc == 32 || rc ==33) {
					ground = new Tile(floor, posx * Tilesize, posy * Tilesize,
							1, this);
					enviroment.add(ground);
				} else if (rc == 42) {
					ex = new Tile(exit, posx * Tilesize, posy * Tilesize, 1,
							this);
					enviroment.add(ex);
				}
				if (rc == 2 && lvl > 1) {
					ground = new Tile(floor, posx * Tilesize, posy * Tilesize,
							1, this);
					enviroment.add(ground);
				}
			}
		}

	}

	public void SpawnEnemy() {
		for (int row = 0; row < leveldata.length; row++) {
			for (int col = 0; col < leveldata[row].length; col++) {
				if (leveldata[row][col] == 3) {
					moveEX = col;
					moveEY = row;
					int rn = (int)(Math.random()*5);
					System.out.println("Randomnr: "+rn);
					if(rn >= 0 && rn <2){
						ene = new Enemy(enemy, Tilesize * moveEX,
								Tilesize * moveEY, 100, this);
						actors.add(ene);
						EnemyCounter++;
					}else if(rn >=2 && rn<=4){
						moveEX = col;
						moveEY = row;
						sli = new Enemy(sl, Tilesize * moveEX,
								Tilesize * moveEY, 100, this);
						actors.add(sli);
						EnemyCounter++;
					}
				}
			}
		}
	}

	public void SpawnPlayer() {
		for (int row = 0; row < leveldata.length; row++) {
			for (int col = 0; col < leveldata[row].length; col++) {
				if (leveldata[row][col] == 2) {
					moveY = row;
					moveX = col;
					hero = new Player(player, Tilesize * moveX,
							(moveY * Tilesize), 100, this);
					actors.add(hero);
					hero.setLoop(0, 0);
					sword = new Item(sw, hero.getX(), hero.getY(), 100, this, false);
					actors.add(sword);
					sword.setLoop(0, 0);
					if(lvl == 1){
						hero.phealth = 130;
						hero.mana= 100;
						hero.armor = 100;
					}else{
						hero.phealth = this.phealth;
						hero.mana = this.mana;
						hero.armor = this.armor;
					}
				}
				
			}
		}
	}
	
	public void SpawnNPC() {
		for (int row = 0; row < leveldata.length; row++) {
			for (int col = 0; col < leveldata[row].length; col++) {
				if (leveldata[row][col] == 30) {
					moveEX = col;
					moveEY = row;
					npc = new NPC(np, Tilesize * moveEX, Tilesize * moveEY-Tilesize, 100, this);
					actors.add(npc);

				}else if(leveldata[row][col] == 31){
					moveEX = col;
					moveEY = row;
					shopowner = new NPC(np, Tilesize * moveEX, Tilesize * moveEY-Tilesize, 100, this);
					actors.add(shopowner);
				}
			}
		}
	}
	
	public void SpawnBoss() {
		for (int row = 0; row < leveldata.length; row++) {
			for (int col = 0; col < leveldata[row].length; col++) {
				if (leveldata[row][col] == 33) {
					moveEX = col;
					moveEY = row;
					bs = new EnemyBoss(BS, Tilesize * moveEX-Tilesize, Tilesize * moveEY-Tilesize, 100, this);
					actors.add(bs);

				}
			}
		}
	}
	
	public void SpawnKristallRoom(){
		for (int row = 0; row < leveldata.length; row++) {
			for (int col = 0; col < leveldata[row].length; col++) {
				int rc = leveldata[row][col];
				if(rc == 13){
					krist = new TileBlock(kr, Tilesize*col-Tilesize/2, Tilesize*row-3*Tilesize, 100, this);
					collision.add(krist);
				}else if(rc == 14){
					thun = new TileBlock(thu, Tilesize*col, Tilesize*row-Tilesize, 100, this);
					collision.add(thun);
					thun.setLoop(0,0);
				}else if(rc == 16){
					ball = new TileBlock(bl, Tilesize*col, Tilesize*row, 1,this);
					collision.add(ball);
				}else if(rc == 151){
					plate1 = new Tile(cp, Tilesize*col, Tilesize*row, 1,this);
					enviroment.add(plate1);
					plate1.setLoop(0, 0);
				}else if(rc == 152){
					plate2 = new Tile(cp, Tilesize*col, Tilesize*row, 1,this);
					enviroment.add(plate2);
					plate2.setLoop(0, 0);
				}else if(rc == 153){
					plate3 = new Tile(cp, Tilesize*col, Tilesize*row, 1,this);
					enviroment.add(plate3);
					plate3.setLoop(0, 0);
				}else if(rc == 154){
					plate4 = new Tile(cp, Tilesize*col, Tilesize*row, 1,this);
					enviroment.add(plate4);
					plate4.setLoop(0,0);
				}
			}
		}
	}
	
	public void SpawnItem(double x, double y, boolean b) {
		double xi = x;
		double yi = y;
		System.out.println("drop");
		item = new Item(IT, xi, yi+16, 100, this, b);
		actors.add(item);
	}
	
	public void createBoltBoss(double xb, double yb){	
		double x;
		double y;
		x=xb;		
		y=yb+33;
		mbb = new MagicBolt(Bolt, x, y, 100, this, true);
		actors.add(mbb);
	}
	

	public void createBolt() {
		
		if (dir == 1) { // hoch
			x = hero.getX();
			y = hero.getY() - 8;
		} else if (dir == 2) { // links
			x = hero.getX() - 8;
			y = hero.getY() + Tilesize;
		} else if (dir == 3) { // runter
			x = hero.getX();
			y = hero.getY() + 24;
		} else if (dir == 4) { // rechts
			x = hero.getX() + 8;
			y = hero.getY() + Tilesize;
		}
		mb = new MagicBolt(Bolt, x, y, 100, this, false);

		ListIterator<Sprite> it = actors.listIterator();
		it.add(mb);
		hero.redMana(25);
	}

	public void generateItem(boolean h, int hp, boolean m, int mp){
		
		if(h == true){
			int rn =(int) (Math.random()*hp);
			System.out.println(rn);
			if(rn%2 == 0 && rn >= 3){
				System.out.println("Health Porion");
				hero.addHealth(10, this.phealth);
			}
		}
		if(m == true){
			int rn =(int) (Math.random()*mp);
			System.out.println(rn);
			if(rn%2 == 1 && rn >=3){
				System.out.println("Mana Potion");
				hero.addMana(10);
			}
		}
	}
	
	
	private void startGame() {
		if(checkp){
			lvl = 6;
		}else
			lvl = 1;
		read();
		if (status) {
			if(!checkp){
				if (GUI.running || join) {
					socketClient = new GameClient(this, JOptionPane.showInputDialog("Please enter the IP: "));
					socketClient.start();
				}
			}
			if(checkp || life != 0){
				EnemyCounter = 0;
			}
				doInitializations();
			if(checkp||life != 0){
				if(check != null)
					check.setLoop(1, 1);
				this.phealth = 130;
				this.mana = 100;
				this.armor = 100;
			}
			soundlib.loopSound("test");
			setStarted(true);
			dead = false;
			finished = false;

		}
	}

	public void stopGame() {
		b0.setVisible(true);
		b1.setVisible(true);
		b2.setVisible(true);
		b3.setVisible(true);
		setStarted(false);
		lvl = 1;
		join = false;
		OoM = false;
		checkp = false;
		phealth = 130;
		mana = 100;
		armor = 100;
		Coins = 0;
		Clear();
		soundlib.stopLoopingSound();
	}

	private void checkKeys() {

		if (up) {

			oldY = moveY;
			oldX = moveX;

			if (leveldata[moveY - 1][moveX] == 1|| leveldata[moveY - 1][moveX] == 11|| leveldata[moveY - 1][moveX] == 16) {
				moveY = oldY;
			} else if (leveldata[moveY - 1][moveX] == 4 || leveldata[moveY - 1][moveX] == 30|| leveldata[moveY-1][moveX] == 31|| leveldata[moveY-1][moveX] == 99) {
				moveY = oldY;
			} else if((leveldata[moveY -1][moveX]==9 && EnemyCounter != 0 || !walkthr) || leveldata[moveY -1][moveX]==9 && !walkthr){
				moveY = oldY;
			} else
				moveY--;
		}

		if (down) {

			oldY = moveY;
			oldX = moveX;

			if (leveldata[moveY + 1][moveX] == 1|| leveldata[moveY + 1][moveX] == 11|| leveldata[moveY + 1][moveX] == 16) {
				moveY = oldY;
			} else if (leveldata[moveY + 1][moveX] == 4|| leveldata[moveY + 1][moveX] == 30|| leveldata[moveY+1][moveX] == 31|| leveldata[moveY+1][moveX] == 99) {
				moveY = oldY;
			} else if(leveldata[moveY +1][moveX]==9 && EnemyCounter != 0){
				moveY = oldY;
			}else
				moveY++;
		}

		if (left) {

			oldY = moveY;
			oldX = moveX;

			if (leveldata[moveY][moveX - 1] == 1|| leveldata[moveY][moveX-1] == 11|| leveldata[moveY][moveX-1] == 16) {
				moveX = oldX;
			} else if (leveldata[moveY][moveX - 1] == 4|| leveldata[moveY][moveX-1] == 30|| leveldata[moveY][moveX-1] == 31|| leveldata[moveY][moveX-1] == 99) {
				moveX = oldX;
			} else if(leveldata[moveY][moveX-1]==9 && EnemyCounter != 0){
				moveX = oldX;
			}else
				moveX--;
		}

		if (right) {

			oldY = moveY;
			oldX = moveX;

			if (leveldata[moveY][moveX + 1] == 1|| leveldata[moveY][moveX+1] == 11|| leveldata[moveY][moveX+1] == 16) {
				moveX = oldX;
			} else if (leveldata[moveY][moveX + 1] == 4|| leveldata[moveY][moveX+1] == 30|| leveldata[moveY][moveX+1] == 31|| leveldata[moveY][moveX+1] == 99) {
				moveX = oldX;
			} else if(leveldata[moveY][moveX+1]==9 && EnemyCounter != 0){
				moveX = oldX;
			}else
				moveX++;
		}

	}

	private void computeDelta() {

		delta = System.nanoTime() - last;
		last = System.nanoTime();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (started) {
			g.setColor(Color.white);
			g.fillRect(0, Height, Width, frame.getHeight() - Height);
			g.setColor(Color.red);
			g.drawString("HP: " + (Math.round(phealth) * 100 / 130), 2, 253);
			g.setColor(Color.blue);
			g.drawString("Mana: " + mana, 50, 253);
			g.setColor(Color.black);
			g.drawString("Coins: " + Coins, 120, 253);
			g.drawString("Def: "+ (int)hero.armor, 185, 253);
		}

		if (dead) {
			g.setColor(Color.red);
			g.drawString(gameov, 20, Height / 2);
		}
		if (finished) {
			g.setColor(Color.red);
			g.drawString(finish, 40, Height / 2);
			repaint();
		}
		if (!started) {
			return;
		}
		if(init == true){
			if(npc != null){
				if(npc.dis==1 && page != 0){
						switch(page){
						case 1:
							npc1.setText("Hey listen! ");
							break;
						case 2:
							npc1.setText("I used to be an adventurer, "+
									 "just like you, until... ");
							break;
						case 3:
							npc1.setText("It's dangerous outside! Take this.");
							init = false;
							break;
						}
						npc1.setVisible(true);
					}else{
						npc1.setVisible(false);
					}
			}
		}
		if(init == false){
			if(npc != null){
				if(npc.dis==1 && page != 0){
					switch(page){
					case 1:
						npc1.setText("Be careful outside");
						break;
					case 2:
						npc1.setText("Use \"Space\" to attack your enemies, "+
								"or \"F\" to use Magic.");
						break;
					case 3:
						npc1.setText("Now go.");
						break;
					}
					npc1.setVisible(true);
				}else{
					npc1.setVisible(false);
				}
			}
					
		}
		if(shopowner != null){
			if(shopowner.dis==1 && page != 0){
				switch(page){
				case 1:
					shop.setText("What do you wann buy?\n" +
							"1) Health Potion: 5 Coins\n" +
							"2) Mana Potion: 7 Coins\n" +
							"3) Armor: 30 Coins");
					showtext = true;
					break;
				case 2:
					shop.setText("You dont have enough Coins.");
				}
				shop.setVisible(true);
			}else{
				shop.setVisible(false);
				showtext = false;
			}
		}
		
		for (ListIterator<Enviroment> ev = painter2.listIterator(); ev
				.hasNext();) {
			Enviroment e = ev.next();
			e.drawObjects(g);
		}
		for (ListIterator<Sprite> co = painter3.listIterator(); co.hasNext();) {
			Sprite r = co.next();
			r.drawObjects(g);
		}
		for (ListIterator<Sprite> it = painter.listIterator(); it.hasNext();) {
			Sprite r = it.next();
			r.drawObjects(g);
		}

	}

	public BufferedImage[] loadPics(String path, int pics) { // Methode bekommt
																// Speicherort
																// und Anzahl
																// der
																// Einzelbilder
																// übergeben

		BufferedImage[] anim = new BufferedImage[pics]; // Erzeugung des
														// Image-Arrays mit der
														// größe der
														// Einzelbilder
		BufferedImage source = null; // lädt das ganze Bild

		URL pic_url = getClass().getClassLoader().getResource(path); // Ermittelung
																		// der
																		// URL
																		// des
																		// Speicherortes,
																		// wird
																		// als
																		// Pfadangabe
																		// übergeben

		try {
			source = ImageIO.read(pic_url); // Quellbild wird über ImageIO
											// geladen
		} catch (IOException e) {
		}

		for (int x = 0; x < pics; x++) {
			anim[x] = source.getSubimage(x * source.getWidth() / pics, 0,
					source.getWidth() / pics, source.getHeight()); // die
																	// Methode
																	// getSubimage
																	// zerlegt
																	// das
																	// Quellbild
																	// in die
																	// Anzahl
																	// der
																	// angegebenen
																	// Einzelbilder.
		}

		return anim;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!dead) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				up = true;
				dir = 1;
				hero.setLoop(0, 2);
				down = false;
				left = false;
				right = false;
			}

			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				down = true;
				dir = 3;
				hero.setLoop(6, 8);
				up = false;
				left = false;
				right = false;
			}

			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				left = true;
				dir = 2;
				hero.setLoop(9, 11);
				down = false;
				up = false;
				right = false;
			}

			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				right = true;
				dir = 4;
				hero.setLoop(3, 5);
				down = false;
				left = false;
				up = false;
			}
			if(e.getKeyCode()== KeyEvent.VK_SPACE){
				if(dir==1){
					yy = -4;
					xx = 0;
					sword.setLoop(1,3);
					sup = true;
				}else if(dir == 2){
					xx = -16;
					yy = +16;
					sword.setLoop(7,9);
					sleft = true;
				}else if(dir == 3){
					yy = +32;
					xx = 0;
					sword.setLoop(4,6);
					sdown = true;
				}else if(dir == 4){
					xx = +16;
					yy = +16;
					sword.setLoop(10,12);
					sright = true;
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			up = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			down = false;			
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			left = false;			
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (isStarted()) {
				stopGame();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_F1){
			hero.addMana(100);
		}
		if (e.getKeyCode() == KeyEvent.VK_F) {
			if (!dead) {
				if (!OoM && mana >= 25) {
					if (dir != 0) {
						createBolt();
					}
				}
			}
		}
		if(e.getKeyCode()== KeyEvent.VK_SPACE){
			if(dir==1){
				sword.setLoop(0,0);
			}else if(dir == 2){
				sword.setLoop(0,0);
			}else if(dir == 3){
				sword.setLoop(0,0);
			}else if(dir == 4){
				sword.setLoop(0,0);
			}
			sup = false;
			sleft = false;
			sdown = false;
			sright = false;
		}
		if(lvl == 1){
			if(npc.dis == 1 && lvl==1){
				if(e.getKeyCode()== KeyEvent.VK_E){
					page++;
					if(page>3)
						page=0;
				}
			}else
				page = 0;
		}
		if(lvl>1){
			if(shopowner != null && shopowner.dis == 1){
				if(e.getKeyCode()==KeyEvent.VK_E){
					page++;
					if(page>1)
						page=0;
				}
			}else
				page = 0;
		}
		if(e.getKeyCode() == KeyEvent.VK_1 && this.Coins >=5 && showtext == true){
			hero.addHealth(25, this.phealth);
			Coins = Coins - 5;
			page = 1;
		}
		if(e.getKeyCode() == KeyEvent.VK_1 && this.Coins <5 && showtext == true){
			page = 2;
		}
		if(e.getKeyCode() == KeyEvent.VK_2 && this.Coins >=7 && showtext == true){
			hero.addMana(25);
			Coins = Coins - 7;
		}
		if(e.getKeyCode() == KeyEvent.VK_2 && this.Coins <7 && showtext == true){
			page = 2;
		}
		if(e.getKeyCode() == KeyEvent.VK_3 && this.Coins >=30 && showtext == true){
			System.out.println("Armor Platzhalter");
			Coins = Coins - 30;
		}
		if(e.getKeyCode() == KeyEvent.VK_3 && this.Coins <30 && showtext == true){
			page = 2;
		}
	}

	@SuppressWarnings("resource")
	public void read() {
		try {
			String sTemp;
			String rest = null;
			int i, j;
			leveldata = new int[Height / Tilesize][Width / Tilesize];
			switch (lvl) {
			case 1:
				rest = "lvl1.level";
				break;
			case 2:
				rest = "lvl2.level";
				break;
			case 3:
				rest = "lvl3.level";
				break;
			case 4:
				rest = "lvl4.level";
				break;
			case 5:
				rest = "lvl5.level";
				break;
			case 6:
				rest = "lvl6.level";
				break;
			case 7:
				rest = "lvl7.level";
				break;
			case 8:
				rest = "lvl8.level";
				break;
			case 9:
				rest = "lvl9.level";
				break;
			case 10:
				rest = "lvlBoss1.level";
				boss = true;
				break;
			default:
				System.out.println("Level existiert nicht");
				frame.dispose();
				break;
			}
			BufferedReader oReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File("res/lvl/" + rest)))); // Zeile
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
			status = true;
		} catch (FileNotFoundException e) {

			e.printStackTrace(); // Fehler ausdrucken

		} catch (IOException e) {

			e.printStackTrace(); // Fehler ausdrucken

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

}
