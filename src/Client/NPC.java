package Client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class NPC extends Sprite {
	

	Player hero;
	GamePanel gp;
	Graphics g;

	public NPC(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
		super(i, x, y, delay, p);
		this.x = x;
		this.y = y;
	}
	@Override
	public void doLogic(long delta){
		super.doLogic(delta);		

		
	}

	@Override
	public boolean collidedWith(Sprite s) {
		return false;
	}

}
