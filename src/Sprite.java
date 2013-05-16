import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


public abstract class Sprite extends Rectangle2D.Double implements Drawable, Movable{

	private static final long serialVersionUID = 1L;
	long delay;			//Instanzvariable zum umschalten zwischen den Bildern des Image-Arrays in millisekunden
	long animation = 0;
	GamePanel parent;	//Referenz auf GamePanel
	BufferedImage[] pics;	//Image-Array zum speicher unserer Animation in Einzelbildern
	int currentpic = 0; //Zähler für das aktuelle anzuzeigende Bild
	
	protected double dx; //Instanzvariable für die horizontale Veränderung
	protected double dy; //Instanzvariable für die vertikale Veränderung
        
    int loop_from;
	int loop_to;
	
	boolean remove;
        
	
	public Sprite(BufferedImage[] i, double x, double y, long delay, GamePanel p){ //Übergabe des Image-Arrays, den Positionswerten, der Verzögerung der animation und Referenz auf GamePanel
		pics = i;
		this.x = x;
		this.y = y;
		this.delay = delay;
		this.width = pics[0].getWidth();
		this.height = pics[0].getHeight();
		parent = p;
        loop_from = 0;
        loop_to = pics.length-1;
	}
	
	public void drawObjects(Graphics g){	
		g.drawImage(pics[currentpic], (int)x, (int)y, null); //Zeichnung des Aktuellen Bildes mit übergabe der X-y-Paramter in ganzen Zahlen (gibt ja keine halben Pixel)
	}
	
	public void doLogic(long delta){
		
		animation += (delta/1000000); //Umrechung von nano zu milli sec.
		if(animation > delay){
			animation = 0;
			computeAnimation();
		}
	}
	
	private void computeAnimation() { //Funtion zum durchlaufen des ImageArray => animation

		currentpic++;
		
		if(currentpic>=pics.length){
			currentpic=0;
		}
		
	}
        
	public void setLoop(int from, int to){
		loop_from = from;
		loop_to   = to;
		currentpic = from;
	}
        
	public void move(long delta){
		
		if(dx!=0){
			x += dx*(delta/1e9);
		}
		
		if(dy != 0){
			y += dy*(delta/1e9);
		}
	}
	
	public double getHorizontalSpeed(){
		return dx;
	}
	
	public void setHorizontalSpeed(double dx){
		this.dx = dx;
	}
	
	public double getVerticalSpeed(){
		return dy;
	}
	
	public void setVerticalSpeed(double dy){
		this.dy = dy;
	}
        
        public abstract boolean collidedWith(Sprite s);
}
