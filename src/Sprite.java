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
	
	protected double dmg; //Instanzvariable für die horizontale Veränderung
	protected double dx;
	protected double dy;
	protected double health = 100;
        
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
	
	public boolean calcDmg(double dmg) {
		this.health = health - dmg;
		if(health <= 0){
			return true;
		}else
			return false;
	}
	
	
    public abstract boolean collidedWith(Sprite s);
    
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
    	
    	//Überprüfung ob nicht transparente Pixel in beiden Bildern vorhanden sind
    	for(int i=0;i<img_me.getWidth();i++){
    		for(int n=0;n<img_him.getHeight()-16;n++){
    			
    			int rgb1 = img_me.getRGB(i, n);
    			int rgb2 = img_him.getRGB(i, n);
    			
    			//Wenn Pixel gefunden wurde gib Kollsion zurück
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
    	
    	//Durch den übergebenen RGB Wert wird der Alpha-Wert per Bitverschiebung ermittelt
    	
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
