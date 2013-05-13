import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Enviroment extends Rectangle2D.Double implements Drawable, Movable{

	private static final long	serialVersionUID	= 1L;
	GamePanel parent2;
        BufferedImage[] pics2;
	int currentpic2 = 0;
        long delay2;
        long animation2 = 0;
        
        protected double dxx;
	protected double dyy;
        
        int loop_from2;
	int loop_to2;

	public Enviroment(BufferedImage[] j, double xx, double yy, long delay2, GamePanel p2 ){
		pics2 = j;
		this.x = xx;
		this.y = yy;
                this.delay2=delay2;
                this.width = pics2[0].getWidth();
		this.height = pics2[0].getHeight();
                parent2 = p2;
                loop_from2 = 0;
                loop_to2 = pics2.length-1;
	}
	
	public void drawObjects(Graphics g) {
		g.drawImage(pics2[currentpic2], (int) x, (int) y, null);
	}
        
        public void doLogic(long delta) {
		
		animation2 += (delta/1000000);
		if (animation2 > delay2) {
			animation2 = 0;
			computeAnimation();
		}
		
	}

	private void computeAnimation(){
    
		currentpic2++;

        if(currentpic2>loop_to2){
            currentpic2 = loop_from2;
        }
		
	}
		
	public void setLoop(int from2, int to2){
		loop_from2 = from2;
		loop_to2   = to2;
		currentpic2 = from2;
	}
        
        public void move(long delta) {
		
            if(dxx!=0){
                x += dxx*(delta/1e9);
            }
    
            if(dyy!=0){
                y += dyy*(delta/1e9);
            }
    
	}

}
