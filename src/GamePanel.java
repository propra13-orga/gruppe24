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
	private JButton b0;
	private JButton b1;

	JFrame frame;

	long delta = 0;
	long last = 0;
	long gameover = 0;

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

	boolean up;
	boolean down;
	boolean left;
	boolean right;
	boolean started;
	boolean status = false;
	boolean dead = false;
	boolean finished = false;

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
	static int Tilesize = 16;

	static int Width = 15 * Tilesize;
	static int Height = 15 * Tilesize;

	Timer timer;

	public static void main(String[] args) {
		new GamePanel(Width, Height);
	}

	public GamePanel(int w, int h) {
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.BLACK);

		frame = new JFrame("Insert Name here");
		frame.setLocation(960 - (Width / 2), 600 - (Height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		b0 = new JButton("Start");
		b1 = new JButton("Close");
		b0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				b0.setVisible(false);
				b1.setVisible(false);
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

		frame.add(b0, BorderLayout.EAST);
		frame.add(b1, BorderLayout.WEST);
		frame.add(this);
		frame.addKeyListener(this);
		frame.pack();
		frame.setResizable(false);
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
		soundlib.loadSound("test", "sounds/Test.wav");

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
		
		hero.setFrame(moveX * Tilesize, moveY * Tilesize-Tilesize, Tilesize, Tilesize);
		
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
		if(leveldata[moveY][moveX]==42){
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
	}

	public void ground() {
		for (int row = 0; row < leveldata.length; row++) {
			for (int col = 0; col < leveldata[row].length; col++) {
				if (leveldata[row][col] == 0) {
					posy = row;
					posx = col;
					BufferedImage[] floor = loadPics("pics/floor.gif", 1);
					ground = new Tile(floor, posx * Tilesize, posy * Tilesize, 1, this);
					enviroment.add(ground);
				}
				if (leveldata[row][col] == 1) {
					posy = row;
					posx = col;
					BufferedImage[] wall = loadPics("pics/wall.gif", 1);
					wl = new TileBlock(wall, posx * Tilesize, posy * Tilesize, 0, this);
					collision.add(wl);
				}
				if (leveldata[row][col] == 2) {
					posy = row;
					posx = col;
					BufferedImage[] pstart = loadPics("pics/pstart.png", 1);
					ps = new Tile(pstart, posx * Tilesize, posy * Tilesize, 0, this);
					enviroment.add(ps);
				}
				if (leveldata[row][col] == 3) {
					posy = row;
					posx = col;
					BufferedImage[] floor = loadPics("pics/floor.gif", 1);
					ground = new Tile(floor, posx * Tilesize, posy * Tilesize, 1, this);
					enviroment.add(ground);
				}
				if (leveldata[row][col] == 4) {
					posy = row;
					posx = col;
					BufferedImage[] water = loadPics("pics/water.gif", 2);
					wt = new TileBlock(water, posx * Tilesize, posy * Tilesize, 500, this);
					collision.add(wt);
				}
				if (leveldata[row][col] == 7) {
					posy = row;
					posx = col;
					BufferedImage[] floor = loadPics("pics/floor.gif", 1);
					ground = new Tile(floor, posx * Tilesize, posy * Tilesize, 0, this);
					enviroment.add(ground);
					savex = col;
					savey = row;
				}
				if (leveldata[row][col] == 8) {
					posy = row;
					posx = col;
					BufferedImage[] pstart = loadPics("pics/pstart.png", 1);
					ps = new Tile(pstart, posx * Tilesize, posy * Tilesize, 0, this);
					enviroment.add(ps);
				}
				if (leveldata[row][col] == 9) {
					posy = row;
					posx = col;
					BufferedImage[] pstart = loadPics("pics/pstart.png", 1);
					ps = new Tile(pstart, posx * Tilesize, posy * Tilesize, 0, this);
					enviroment.add(ps);
				}
				if (leveldata[row][col] == 2 && lvl > 1) {
					posy = row;
					posx = col;
					BufferedImage[] floor = loadPics("pics/floor.gif", 1);
					ground = new Tile(floor, posx * Tilesize, posy * Tilesize, 1, this);
					enviroment.add(ground);
				}
				if(leveldata[row][col] == 42){
					posy = row;
					posx = col;
					BufferedImage[] exit = loadPics("pics/exit.png", 1);
					ex = new Tile(exit, posx*Tilesize, posy*Tilesize, 1, this);
					enviroment.add(ex);
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
					ene = new Enemy(enemy, Tilesize * moveEX, Tilesize * moveEY, 100, this);
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
					
					BufferedImage[] player = loadPics("pics/player.png", 1);
					hero = new Player(player, Tilesize * moveX, (moveY * Tilesize), 100, this);
					actors.add(hero);
				}
			}
		}
	}

	private void createBolt() {

		if (dir == 1) {				//hoch
			x = hero.getX();
			y = hero.getY() - 8;
		} else if (dir == 2) {		//links
			x = hero.getX() - 8;
			y = hero.getY() +Tilesize;
		} else if (dir == 3) {		//runter
			x = hero.getX();
			y = hero.getY() +24;
		} else if (dir == 4) {		//rechts
			x = hero.getX() + 8;
			y = hero.getY() +Tilesize;
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
			soundlib.loopSound("test");
			setStarted(true);
			dead = false;
			finished = false;
		}
	}

	private void stopGame() {
		b0.setVisible(true);
		b1.setVisible(true);
		setStarted(false);
		lvl = 1;
		timer.stop();
		soundlib.stopLoopingSound();
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
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.red);

		if(dead) {
			g.setColor(Color.red);
			g.drawString(gameov, 20, Height / 2);
		}
		if(finished){
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
			if (e.getKeyCode() == KeyEvent.VK_F) {
				if (!dead) {
					if (dir != 0) {
						createBolt();
					}
				}
			}
	}

	@SuppressWarnings("resource")
	public void read() {

		java.lang.String sTemp;
		int i, j;
		leveldata = new int[15][15];
		switch (lvl) {
		case 1:
			try { // Datei öffnen

				java.io.BufferedReader oReader = new java.io.BufferedReader(
						new java.io.InputStreamReader(
								new java.io.FileInputStream(new java.io.File(
										"res/lvl/lvl1.level")))); // Zeile für
																	// Zeile
																	// einlesen

				i = 0;

				while ((sTemp = oReader.readLine()) != null) {

					// Zeile in Einzelteile zerlegen (wir trennen durch ;

					java.util.StringTokenizer stWerte = new java.util.StringTokenizer(
							sTemp, ";");

					j = 0;

					// Nun eintragen in den Array. Es wird nicht überprüft, ob
					// die Grenzen überschritten werden!

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
			try { // Datei öffnen

				java.io.BufferedReader oReader = new java.io.BufferedReader(
						new java.io.InputStreamReader(
								new java.io.FileInputStream(new java.io.File(
										"res/lvl/lvl2.level")))); // Zeile für
																	// Zeile
																	// einlesen

				i = 0;

				while ((sTemp = oReader.readLine()) != null) {

					// Zeile in Einzelteile zerlegen (wir trennen durch ;

					java.util.StringTokenizer stWerte = new java.util.StringTokenizer(
							sTemp, ";");

					j = 0;

					// Nun eintragen in den Array. Es wird nicht überprüft, ob
					// die Grenzen überschritten werden!

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
			try { // Datei öffnen

				java.io.BufferedReader oReader = new java.io.BufferedReader(
						new java.io.InputStreamReader(
								new java.io.FileInputStream(new java.io.File(
										"res/lvl/lvl3.level")))); // Zeile für
																	// Zeile
																	// einlesen

				i = 0;

				while ((sTemp = oReader.readLine()) != null) {

					// Zeile in Einzelteile zerlegen (wir trennen durch ;

					java.util.StringTokenizer stWerte = new java.util.StringTokenizer(
							sTemp, ";");

					j = 0;

					// Nun eintragen in den Array. Es wird nicht überprüft, ob
					// die Grenzen überschritten werden!

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
