import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ListIterator;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable, KeyListener,
		ActionListener {

	private static final long serialVersionUID = 1L;

	// final static int [][]leveldata ={
	// {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
	// {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
	// {1, 0, 3, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
	// {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 3, 0, 1},
	// {1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1},
	// {1, 0, 0, 0, 1, 4, 4, 4, 4, 4, 1, 0, 0, 0, 1},
	// {1, 0, 0, 0, 1, 4, 4, 4, 4, 4, 1, 0, 0, 0, 1},
	// {1, 0, 0, 0, 1, 4, 4, 4, 4, 4, 1, 0, 0, 0, 1},
	// {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1},
	// {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1},
	// {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1},
	// {1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1},
	// {1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
	// {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
	// {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

	JFrame frame;

	long delta = 0;
	long last = 0;
	long fps = 0;
	long gameover = 0;

	Player hero;
	Enemy ene;
	Tile ground;
	Tile ps;
	TileBlock wl;
	TileBlock wt;
	Vector<Sprite> actors;
	Vector<Sprite> painter;
	Vector<Sprite> collision;
	Vector<Sprite> painter3;
	Vector<Enviroment> enviroment;
	Vector<Enviroment> painter2;

	int[][] leveldata;
	int lvl = 1;

	SoundLib soundlib;
	
	double x;
	double y;

	boolean up;
	boolean down;
	boolean left;
	boolean right;
	boolean started;
	boolean status = false;
	int speed = 50;
	int moveX;
	int moveY;
	int moveEX;
	int moveEY;

	int dir = 0;
	// boolean dirU = false;
	// boolean dirD = false;
	// boolean dirL = false;
	// boolean dirR = false;
	int oldX;
	int oldY;
	int posx;
	int posy;

	static int Width = 15 * 16;
	static int Height = 15 * 16;
	Timer timer;

	public static void main(String[] args) {
		new GamePanel(Width, Height);
	}

	public GamePanel(int w, int h) {
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.BLACK);

		final JButton b0 = new JButton("Start");
		b0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				b0.setVisible(false);
				frame.pack();
				startGame();
				frame.requestFocus();
			}
		});

		frame = new JFrame("Insert Name here");
		frame.setLocation(960 - (Width / 2), 600 - (Height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(this);
		frame.add(b0, BorderLayout.EAST);
		frame.addKeyListener(this);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
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
		// soundlib.loadSound("test", "res/sounds/Test.wav");

		ground();
		SpawnPlayer();
		SpawnEnemy();
		System.out.println("" + moveX + "," + moveY);

		timer = new Timer(300, this);
		timer.start();

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
				Thread.sleep(90);
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

		hero.setFrame(moveX * 16, moveY * 16, 16, 16);
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

	}

	public void ground() {
		for (int row = 0; row < leveldata.length; row++) {
			for (int col = 0; col < leveldata[row].length; col++) {
				if (leveldata[row][col] == 0) {
					posy = row;
					posx = col;
					BufferedImage[] floor = loadPics("pics/floor.png", 1);
					ground = new Tile(floor, posx * 16, posy * 16, 1, this);
					enviroment.add(ground);
				}
				if (leveldata[row][col] == 1) {
					posy = row;
					posx = col;
					BufferedImage[] wall = loadPics("pics/wall.png", 1);
					wl = new TileBlock(wall, posx * 16, posy * 16, 0, this);
					collision.add(wl);
				}
				if (leveldata[row][col] == 2) {
					posy = row;
					posx = col;
					BufferedImage[] pstart = loadPics("pics/pstart.png", 1);
					ps = new Tile(pstart, posx * 16, posy * 16, 0, this);
					enviroment.add(ps);
				}
				if (leveldata[row][col] == 3) {
					posy = row;
					posx = col;
					BufferedImage[] floor = loadPics("pics/floor.png", 1);
					ground = new Tile(floor, posx * 16, posy * 16, 1, this);
					enviroment.add(ground);
				}
				if (leveldata[row][col] == 4) {
					posy = row;
					posx = col;
					BufferedImage[] water = loadPics("pics/water.gif", 2);
					wt = new TileBlock(water, posx * 16, posy * 16, 500, this);
					collision.add(wt);
				}
				if (leveldata[row][col] == 9) {
					posy = row;
					posx = col;
					BufferedImage[] pstart = loadPics("pics/pstart.png", 1);
					ps = new Tile(pstart, posx * 16, posy * 16, 0, this);
					enviroment.add(ps);
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

					BufferedImage[] enemy = loadPics("pics/player.gif", 4);
					ene = new Enemy(enemy, 16 * moveEX, 16 * moveEY, 100, this);
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

					BufferedImage[] player = loadPics("pics/player.gif", 4); // Player
																				// Image
																				// wird
																				// geladen
					hero = new Player(player, 16 * moveX, moveY * 16, 100, this);
					actors.add(hero);
				}
			}
		}
	}

	private void createBolt() {

		if(dir == 1){
			x = hero.getX();
			y = hero.getY()-8;
		}else
		if(dir == 2){
			x = hero.getX()-8;
			y = hero.getY();
		}else
		if(dir == 3){
			x = hero.getX();
			y = hero.getY()+8;
		}else
		if(dir == 4){
			x = hero.getX()+8;
			y = hero.getY();
		}

		BufferedImage[] Bolt = loadPics("pics/Bolt.png", 3);
		MagicBolt mb = new MagicBolt(Bolt, x, y, 100, this);

		ListIterator<Sprite> it = actors.listIterator();
		it.add(mb);

	}

	private void startGame() {
		read();
		if (status) {
			doInitializations();
			System.out.println("Start");
			// soundlib.loopSound("test");
			setStarted(true);
		}
	}

	private void stopGame() {
		setStarted(false);
		lvl = 1;
		timer.stop();
		// soundlib.stopLoopingSound();
	}

	public void MoveYh() {

		oldY = moveY;
		oldX = moveX;

		if (leveldata[moveY - 1][moveX] == 1) {
			moveY = oldY;
		} else if (leveldata[moveY - 1][moveX] == 4) {
			moveY = oldY;
		} else
			moveY--;

	}

	public void MoveYd() {

		oldY = moveY;
		oldX = moveX;

		if (leveldata[moveY + 1][moveX] == 1) {
			moveY = oldY;
		} else if (leveldata[moveY + 1][moveX] == 4) {
			moveY = oldY;
		} else
			moveY++;

	}

	private void MoveXl() {

		oldY = moveY;
		oldX = moveX;

		if (leveldata[moveY][moveX - 1] == 1) {
			moveX = oldX;
		} else if (leveldata[moveY][moveX - 1] == 4) {
			moveX = oldX;
		} else
			moveX--;

	}

	private void MoveXr() {

		oldY = moveY;
		oldX = moveX;

		if (leveldata[moveY][moveX + 1] == 1) {
			moveX = oldX;
		} else if (leveldata[moveY][moveX + 1] == 4) {
			moveX = oldX;
		} else
			moveX++;
	}

	private void checkKeys() {

		if (up) {
			MoveYh();
		}

		if (down) {
			MoveYd();
		}

		if (left) {
			MoveXl();
		}

		if (right) {
			MoveXr();
		}

	}

	private void computeDelta() {

		delta = System.nanoTime() - last;
		last = System.nanoTime();
		fps = ((long) 1e9) / delta;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.red);
		g.drawString("FPS: " + Long.toString(fps), 20, 10);

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
																// �bergeben

		BufferedImage[] anim = new BufferedImage[pics]; // Erzeugung des
														// Image-Arrays mit der
														// gr��e der
														// Einzelbilder
		BufferedImage source = null; // l�dt das ganze Bild

		URL pic_url = getClass().getClassLoader().getResource(path); // Ermittelung
																		// der
																		// URL
																		// des
																		// Speicherortes,
																		// wird
																		// als
																		// Pfadangabe
																		// �bergeben

		try {
			source = ImageIO.read(pic_url); // Quellbild wird �ber ImageIO
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

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			up = true;
			dir = 1;
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			down = true;
			dir = 3;
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			left = true;
			dir = 2;
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right = true;
			dir = 4;
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

		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (!isStarted()) {
				startGame();
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (isStarted()) {
				stopGame();
			} else {
				frame.dispose();
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_F) {
			if (dir != 0) {
				createBolt();
			}
		}
	}

	public void read() {

		java.lang.String sTemp;
		int i, j;
		leveldata = new int[15][15];
		switch (lvl) {
		case 1:
			try { // Datei �ffnen

				java.io.BufferedReader oReader = new java.io.BufferedReader(
						new java.io.InputStreamReader(
								new java.io.FileInputStream(new java.io.File(
										"res/lvl/lvl1.level")))); // Zeile f�r
																	// Zeile
																	// einlesen

				i = 0;

				while ((sTemp = oReader.readLine()) != null) {

					// Zeile in Einzelteile zerlegen (wir trennen durch ;

					java.util.StringTokenizer stWerte = new java.util.StringTokenizer(
							sTemp, ";");

					j = 0;

					// Nun eintragen in den Array. Es wird nicht �berpr�ft, ob
					// die Grenzen �berschritten werden!

					while (stWerte.hasMoreTokens()) {

						leveldata[i][j] = java.lang.Integer.parseInt(stWerte
								.nextToken());

						j++;

					}

					i++;

				}
				status = true;
			} catch (java.io.FileNotFoundException e) {

				e.printStackTrace(); // Fehler ausdrucken

			} catch (java.io.IOException e) {

				e.printStackTrace(); // Fehler ausdrucken

			}
			break;
		case 2:
			try { // Datei �ffnen

				java.io.BufferedReader oReader = new java.io.BufferedReader(
						new java.io.InputStreamReader(
								new java.io.FileInputStream(new java.io.File(
										"res/lvl/lvl2.level")))); // Zeile f�r
																	// Zeile
																	// einlesen

				i = 0;

				while ((sTemp = oReader.readLine()) != null) {

					// Zeile in Einzelteile zerlegen (wir trennen durch ;

					java.util.StringTokenizer stWerte = new java.util.StringTokenizer(
							sTemp, ";");

					j = 0;

					// Nun eintragen in den Array. Es wird nicht �berpr�ft, ob
					// die Grenzen �berschritten werden!

					while (stWerte.hasMoreTokens()) {

						leveldata[i][j] = java.lang.Integer.parseInt(stWerte
								.nextToken());

						j++;

					}

					i++;

				}
				status = true;
			} catch (java.io.FileNotFoundException e) {

				e.printStackTrace(); // Fehler ausdrucken

			} catch (java.io.IOException e) {

				e.printStackTrace(); // Fehler ausdrucken

			}
			break;
		case 3:
			try { // Datei �ffnen

				java.io.BufferedReader oReader = new java.io.BufferedReader(
						new java.io.InputStreamReader(
								new java.io.FileInputStream(new java.io.File(
										"res/lvl/lvl3.level")))); // Zeile f�r
																	// Zeile
																	// einlesen

				i = 0;

				while ((sTemp = oReader.readLine()) != null) {

					// Zeile in Einzelteile zerlegen (wir trennen durch ;

					java.util.StringTokenizer stWerte = new java.util.StringTokenizer(
							sTemp, ";");

					j = 0;

					// Nun eintragen in den Array. Es wird nicht �berpr�ft, ob
					// die Grenzen �berschritten werden!

					while (stWerte.hasMoreTokens()) {

						leveldata[i][j] = java.lang.Integer.parseInt(stWerte
								.nextToken());

						j++;

					}

					i++;

				}
				status = true;
			} catch (java.io.FileNotFoundException e) {

				e.printStackTrace(); // Fehler ausdrucken

			} catch (java.io.IOException e) {

				e.printStackTrace(); // Fehler ausdrucken

			}
			break;
		default:
			System.out.println("Level existiert nicht");
			frame.dispose();
			break;
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

}
