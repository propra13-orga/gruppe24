package Client;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**************************************************************************
 * Basis Klasse f�r alle gezeichneten Objekte mit Ausnahme der Enviroment *
 **************************************************************************/
public abstract class Sprite extends Rectangle2D.Double implements Drawable, Movable{

	private static final long serialVersionUID = 1L;
	long delay;				//Instanzvariable zum umschalten zwischen den Bildern des Image-Arrays in millisekunden
	long animation = 0;
	GamePanel parent;		//Referenz auf GamePanel
	EnemyBoss bs;
	BufferedImage[] pics;	//Image-Array zum speicher unserer Animation in Einzelbildern
	int currentpic = 0; 	//Z�hler f�r das aktuelle anzuzeigende Bild
	
	protected double dmg; 	//Instanzvariable f�r die horizontale Ver�nderung
	protected double dx;
	protected double dy;
	protected double health = 100;
	protected double bosshealth = 250;
	protected double phealth;
	protected double armor = 100;
	protected int mana = 100;
    
	double d;
	
    int loop_from;
	int loop_to;
	
	boolean remove;
	boolean b;
        
	
	public Sprite(BufferedImage[] i, double x, double y, long delay, GamePanel p){ //�bergabe des Image-Arrays, den Positionswerten, der Verz�gerung der animation und Referenz auf GamePanel
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
	
	/****************************************************************************************************************
	 * Zeichnung des Aktuellen Bildes mit �bergabe der X-y-Paramter in ganzen Zahlen (gibt ja keine halben Pixel) *
	 ****************************************************************************************************************/
	public void drawObjects(Graphics g){	
		g.drawImage(pics[currentpic], (int)x, (int)y, null); 
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
		if(currentpic>loop_to){
			currentpic=loop_from;
		}
		
	}
     
	/************************************************
	 * Mit setLoop kann man bestimmen welcher		*
	 * Animations abschnitt gezeichent werden soll. *
	 * (Von Bild-x bis Bild-y)						*
	 ************************************************/
	public void setLoop(int from, int to){
		loop_from = from;
		loop_to   = to;
		currentpic = from;
	}
     
	/******************************************************************
	 * Ver�nderung der Postions des Objektes in abh�ngigkeit der Zeit *
	 * um eine Gleichf�rmige Bewegung des Objektes zu gew�hrleisten   *
	 ******************************************************************/
	public void move(long delta){
		
		if(dx!=0){
			x += dx*(delta/1e9);
		}
		
		if(dy != 0){
			y += dy*(delta/1e9);
		}
	}
	
	/**
	 * Berechnung des Schadens. Boolean b gibt an ob normale Gegner oder Boss Schaden berechnet werden soll
	 */
	public boolean calcDmg(double dmg, boolean b) {
		if(b == false){
				this.health = health - dmg;
		}
		if(b == true){
			this.bosshealth = bosshealth - dmg;
		}
		if(health <= 0 || bosshealth <= 0){
			return true;
		}else
			return false;
	}
	
	/**
	 * Berechnung des Schadens den der Player erleidet
	 */
	public boolean calcDmgPlayer(){
		this.d = ((130*5)/100)-armor;
		if(this.d <= 0){
			this.d = 1;
		}
		this.phealth = phealth -d;
		this.armor = armor -((100*15)/100);
		if(this.armor<0){
			this.armor = 0;
		}
		if(this.phealth<0){
			this.phealth=0;
		}
		if(phealth==0){
			return true;
		}else
			return false;
	}
	
	/*************************************************************************************
	 * Funtkion zur reduzierung des Manavorrates von Objekten (Spieler oder auch Gegner) *
	 *************************************************************************************/
	public void redMana(int red){
		this.mana = mana - red;

		if(this.mana>100){
			this.mana = 100;
		}else if(this.mana<=0){
			this.mana = 0;
			parent.OoM = true;
		}
		parent.mana = this.mana;
	}
	
	/******************************************
	 * Funktion zur erh�hung des Manavorrates *
	 ******************************************/
	public void addMana(int ad){
		this.mana = mana + ad;
		if(this.mana>100){
			this.mana = 100;
		}
		parent.mana = this.mana;
		if(this.mana > 0){
			parent.OoM = false;
		}
	}
	
	/***********************************************
	 * Funktion zur erh�hung der Spielergesundheit *
	 ***********************************************/
	public void addHealth(int ad, double h){
		this.phealth = h + ad;
		if(this.phealth>130){
			this.phealth = 130;
		}
		parent.phealth = this.phealth;
	}
	
    public abstract boolean collidedWith(Sprite s);
    
    
    /**************************************************************************
     * Diese Methode dient zur Pixelgenauen Kollisionsanalyse				  *
     * Es wird geguckt ob in der Schnittfl�che der 2 sich schneidenen Objekte *
     * Pixel befinden die nicht transparent sind  							  *
     **************************************************************************/
    public boolean checkOpaqueColorCollisions(Sprite s){
    	
    	//Rechteck zur erstellung der Schnittmenge
    	Rectangle2D.Double cut = (Double) this.createIntersection(s);
    	
    	if((cut.width<1)||(cut.height<1)){
    		return false;
    	}
    	
    	//Rechtecke in Bezug auf die jeweiligen Images
    	Rectangle2D.Double sub_me = getSubRec(this,cut);
    	Rectangle2D.Double sub_him = getSubRec(s,cut);
    	
    	//Schnittmenge der beiden Bilder wird ermittelt
    	BufferedImage img_me = pics[currentpic].getSubimage((int)sub_me.x, (int)sub_me.y, (int)sub_me.width, (int)sub_me.height);
    	BufferedImage img_him = s.pics[s.currentpic].getSubimage((int)sub_him.x,(int)sub_him.y, (int)sub_him.width, (int)sub_him.height);
    	
    	//�berpr�fung ob nicht transparente Pixel in beiden Bildern vorhanden sind
    	for(int i=0;i<img_me.getWidth();i++){
    		for(int n=0;n<img_him.getHeight();n++){    			
    			int rgb1 = img_me.getRGB(i, n);
    			int rgb2 = img_him.getRGB(i, n);
    			
    			//Wenn Pixel gefunden wurde gib Kollsion zur�ck
    			if(isOpaque(rgb1)&& isOpaque(rgb2)){
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    protected Rectangle2D.Double getSubRec(Rectangle2D.Double source, Rectangle2D.Double part){
    	
    	//Rechtecke erzeugen
    	Rectangle2D.Double sub = new Rectangle2D.Double();
    	
    	//getX mit Rechteck vergleichen
    	if(source.x>part.x){
    		sub.x = 0;
    	}else{
    		sub.x = part.x - source.x;
    	}
    	
    	if(source.y>part.y){
    		sub.y = 0;
    	}else{
    		sub.y = part.y - source.y;
    	}
    	
    	sub.width = part.width;
    	sub.height = part.height;
    	
    	return sub;
    	
    	//Die Methode errechnet die Koordinaten der Schnittmenge bezogen auf die Position des ersten Rechtecks (source)
    }
    
    protected boolean isOpaque(int rgb){
    	
    	//Durch den �bergebenen RGB Wert wird der Alpha-Wert per Bitverschiebung ermittelt
    	
    	int alpha = (rgb >> 24) & 0xff;
    	//red 	= (rgb >> 16) & 0xff;
    	//green = (rgb >>  8) & 0xff;
    	//blue	= (rgb ) & 0xff;
    	
    	if(alpha==0){
    		return false;
    	}
    	
    	return true;
    }
        
}
