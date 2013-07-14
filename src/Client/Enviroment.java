package Client;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Grundsätzlich die selbe Klasse wie die Sprite.class   
 * nur können sich diese Objekte nicht bewegen.		     
 * Die Enviroment.class ist nur für Hintergrunde Elemente
 * wie den Boden zuständig								
 */
public abstract class Enviroment extends Rectangle2D.Double implements Drawable{

	private static final long serialVersionUID = 1L;
	GamePanel parent2;
	private BufferedImage[] pics2;
	private int currentpic2 = 0;
	private	long delay2;
	private long animation2 = 0;

	protected double dxx;
	protected double dyy;

	private int loop_from2;
	private int loop_to2;

	public Enviroment(BufferedImage[] j, double xx, double yy, long delay2,
			GamePanel p2) {
		pics2 = j;
		this.x = xx;
		this.y = yy;
		this.delay2 = delay2;
		this.width = pics2[0].getWidth();
		this.height = pics2[0].getHeight();
		parent2 = p2;
		loop_from2 = 0;
		loop_to2 = pics2.length - 1;
	}

	/****************************************************************************************************************
	 * Zeichnung des Aktuellen Bildes mit übergabe der X-y-Paramter in ganzen Zahlen (gibt ja keine halben Pixel) *
	 ****************************************************************************************************************/
	public void drawObjects(Graphics g) {
		g.drawImage(pics2[currentpic2], (int) x, (int) y, null);
	}

	public void doLogic(long delta) {
		animation2 += (delta / 1000000);
		if (animation2 > delay2) {
			animation2 = 0;
			computeAnimation();
		}

	}

	private void computeAnimation() {		
		currentpic2++;
		if (currentpic2 > loop_to2) {
			currentpic2 = loop_from2;
		}

	}

	/************************************************
	 * Mit setLoop kann man bestimmen welcher		*
	 * Animations abschnitt gezeichent werden soll. *
	 * (Von Bild-x bis Bild-y)						*
	 ************************************************/
	public void setLoop(int from2, int to2) {	
		loop_from2 = from2;
		loop_to2 = to2;
		currentpic2 = from2;
	}


}
