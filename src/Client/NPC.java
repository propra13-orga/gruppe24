package Client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class NPC extends Sprite {
	

	Player hero;
	GamePanel gp;
	Graphics g;
	
	double deltaX;
	double deltaY;
	int dis;
	static int hx;
	static int hy;

	public NPC(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
		super(i, x, y, delay, p);

	}
	@Override
	public void doLogic(long delta){
		super.doLogic(delta);
		
		deltaX =Math.abs((this.getX()/16)-(hx/16));
		deltaY =Math.abs(this.getY()/16-hy/16);
		dis =(int) Math.sqrt((deltaX*deltaX)+(deltaY*deltaY));
		
	}

	@Override
	public boolean collidedWith(Sprite s) {
		return false;
	}
	
	public static void getCoor(int x, int y){
		hx = x;
		hy = y;
	}

}
