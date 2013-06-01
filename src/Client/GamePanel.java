package Client;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;
import Server.*;

public class GamePanel extends JPanel implements Runnable, KeyListener,
		ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton b0;
	private JButton b1;
	private JButton b2;
	private JButton b3;

	JFrame frame;

	long delta = 0;
	long last = 0;
	long gameover = 0;
	long mreg;

	Player hero;
	Enemy ene;
	Tile ground;
	Tile ps;
	Tile ex;
	TileBlock wl;
	TileBlock wt;
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

	SoundLib soundlib;

	double x;
	double y;
	double phealth = 130;

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

	int moveX;
	int moveY;
	int moveEX;
	int moveEY;
	int dir = 0;
	int oldX;
	int oldY;
	int posx;
	int posy;
	int savex;
	int savey;
	int mana = 100;
	static int Tilesize = 16;
	static int Width = 15 * Tilesize;
	static int Height = 15 * Tilesize;

	public String Username;

	BufferedImage[] floor = loadPics("pics/floor.gif", 1);
	BufferedImage[] wall = loadPics("pics/wall.gif", 1);
	BufferedImage[] pstart = loadPics("pics/pstart.png", 1);
	BufferedImage[] water = loadPics("pics/water.gif", 2);
	BufferedImage[] exit = loadPics("pics/exit.png", 1);

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
		TRun();

	}

	private synchronized void TRun() {
		Thread th = new Thread(this);
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
		}
		SpawnEnemy();
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
				Thread.sleep(70);
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

		if (leveldata[moveY][moveX] == 9) {
			lvl++;
			read();
			actors.clear();
			enviroment.clear();
			collision.clear();
			ground();
			SpawnPlayer();
			SpawnEnemy();
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
			SpawnEnemy();
		}
		if (leveldata[moveY][moveX] == 42) {
			finished = true;
			stopGame();

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

		if (hero.remove && gameover == 0) {
			gameover = System.currentTimeMillis();
		}

		if (gameover > 0) {
			if (System.currentTimeMillis() - gameover > 3000) {
				stopGame();
			}
		}

		if (started && mreg == 0) {
			mreg = System.currentTimeMillis();
		}

		if (mreg > 0) {
			if (System.currentTimeMillis() - mreg > 5000) {
				mana = mana + 5;
				OoM = false;
			}
			if (mana > 100) {
				mana = 100;
				hero.mana = 100;
				mreg = 0;
			}
		}

	}

	public void ground() {
		for (int row = 0; row < leveldata.length; row++) {
			for (int col = 0; col < leveldata[row].length; col++) {
				posy = row;
				posx = col;
				if (leveldata[row][col] == 0) {
					ground = new Tile(floor, posx * Tilesize, posy * Tilesize,
							1, this);
					enviroment.add(ground);
				} else if (leveldata[row][col] == 1) {
					wl = new TileBlock(wall, posx * Tilesize, posy * Tilesize,
							0, this);
					collision.add(wl);
				} else if (leveldata[row][col] == 2) {
					ps = new Tile(pstart, posx * Tilesize, posy * Tilesize, 0,
							this);
					enviroment.add(ps);
				} else if (leveldata[row][col] == 3) {
					ground = new Tile(floor, posx * Tilesize, posy * Tilesize,
							1, this);
					enviroment.add(ground);
				} else if (leveldata[row][col] == 4) {
					wt = new TileBlock(water, posx * Tilesize, posy * Tilesize,
							500, this);
					collision.add(wt);
				} else if (leveldata[row][col] == 7) {
					ground = new Tile(floor, posx * Tilesize, posy * Tilesize,
							0, this);
					enviroment.add(ground);
					savex = col;
					savey = row;
				} else if (leveldata[row][col] == 8) {
					ps = new Tile(pstart, posx * Tilesize, posy * Tilesize, 0,
							this);
					enviroment.add(ps);
				} else if (leveldata[row][col] == 9) {
					ps = new Tile(pstart, posx * Tilesize, posy * Tilesize, 0,
							this);
					enviroment.add(ps);
				} else if (leveldata[row][col] == 42) {
					ex = new Tile(exit, posx * Tilesize, posy * Tilesize, 1,
							this);
					enviroment.add(ex);
				}
				if (leveldata[row][col] == 2 && lvl > 1) {
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
					BufferedImage[] enemy = loadPics("pics/Enemy.png", 1);
					ene = new Enemy(enemy, Tilesize * moveEX,
							Tilesize * moveEY, 100, this);
					actors.add(ene);

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
					BufferedImage[] player = loadPics("pics/player.png", 12);
					hero = new Player(player, Tilesize * moveX,
							(moveY * Tilesize), 100, this);
					actors.add(hero);
					hero.setLoop(0, 0);
				}
			}
		}
	}

	private void createBolt() {

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

		BufferedImage[] Bolt = loadPics("pics/Bolt.png", 3);
		MagicBolt mb = new MagicBolt(Bolt, x, y, 100, this);

		ListIterator<Sprite> it = actors.listIterator();
		it.add(mb);
		hero.redMana(25);
	}

	private void startGame() {
		read();
		if (status) {
			if (GUI.running || join) {
				socketClient = new GameClient(this,
						JOptionPane.showInputDialog("Please enter the IP: "));
				socketClient.start();
				this.Username = JOptionPane
						.showInputDialog("Please enter your Playername");
			}
			doInitializations();
			System.out.println("Start");
			soundlib.loopSound("test");
			setStarted(true);
			dead = false;
			finished = false;

		}
	}

	private void stopGame() {
		b0.setVisible(true);
		b1.setVisible(true);
		b2.setVisible(true);
		b3.setVisible(true);
		setStarted(false);
		lvl = 1;
		join = false;
		soundlib.stopLoopingSound();
	}

	private void checkKeys() {

		if (up) {

			oldY = moveY;
			oldX = moveX;

			if (leveldata[moveY - 1][moveX] == 1) {
				moveY = oldY;
			} else if (leveldata[moveY - 1][moveX] == 4) {
				moveY = oldY;
			} else
				moveY--;
		}

		if (down) {

			oldY = moveY;
			oldX = moveX;

			if (leveldata[moveY + 1][moveX] == 1) {
				moveY = oldY;
			} else if (leveldata[moveY + 1][moveX] == 4) {
				moveY = oldY;
			} else
				moveY++;
		}

		if (left) {

			oldY = moveY;
			oldX = moveX;

			if (leveldata[moveY][moveX - 1] == 1) {
				moveX = oldX;
			} else if (leveldata[moveY][moveX - 1] == 4) {
				moveX = oldX;
			} else
				moveX--;
		}

		if (right) {

			oldY = moveY;
			oldX = moveX;

			if (leveldata[moveY][moveX + 1] == 1) {
				moveX = oldX;
			} else if (leveldata[moveY][moveX + 1] == 4) {
				moveX = oldX;
			} else
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

	private BufferedImage[] loadPics(String path, int pics) { // Methode bekommt
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
			}

			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				down = true;
				dir = 3;
				hero.setLoop(6, 8);
			}

			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				left = true;
				dir = 2;
				hero.setLoop(9, 11);
			}

			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				right = true;
				dir = 4;
				hero.setLoop(3, 5);
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			hero.setLoop(0, 0);
			up = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			hero.setLoop(6, 6);
			down = false;			
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			hero.setLoop(9, 9);
			left = false;			
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			hero.setLoop(3, 3);
			right = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (isStarted()) {
				stopGame();
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_F) {
			if (!dead) {
				if (!OoM) {
					if (dir != 0) {
						createBolt();
					}
				}
			}
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
