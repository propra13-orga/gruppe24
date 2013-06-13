package Client;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class MagicBolt extends Sprite {

	private static final long serialVersionUID = 1L;

	boolean bUp = false;
	boolean bDown = false;
	boolean bLeft = false;
	boolean bRight = false;

	Rectangle2D.Double target;
	EnemyBoss bs;
	GamePanel gp;

	public MagicBolt(BufferedImage[] i, double x, double y, long delay,
			GamePanel p, boolean b) {
		super(i, x, y, delay, p);
		this.x = x;
		this.y = y;
		if(!b){
			if (parent.dir == 1) {
				bUp = true;
			}
			if (parent.dir == 3) {
				bDown = true;
			}
			if (parent.dir == 2) {
				bLeft = true;
			}
			if (parent.dir == 4) {
				bRight = true;
			}
		}else{
			bDown = true;
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void doLogic(long delta) {
		super.doLogic(delta);

		if (bUp && getY() > 0) {
			y -= 10;
		}
		if (bDown && getY() / parent.Tilesize < 15) {
			y += 10;
		}
		if (bLeft && getX() > 0) {
			x -= 10;
		}
		if (bRight && getX() / parent.Tilesize < 15) {
			x += 10;
		}
		if (getX() == 0) {
			remove = true;
		}
		if (getX() / parent.Tilesize == 15) {
			remove = true;
		}
		if (getY() == 0) {
			remove = true;
		}
		if (getY() / parent.Tilesize == 15) {
			remove = true;
		}

	}

	@Override
	public boolean collidedWith(Sprite s) {

		if (this.intersects(s)) {
			if (s instanceof Enemy) {
				// parent.createExplosion((int)getX(),(int)getY());
				// parent.createExplosion((int)s.getX(),(int)s.getY());
				remove = true;
				if(s.calcDmg(50, false)==true){
					s.remove = true;
					parent.SpawnItem(s.getX(), s.getY());
					parent.EnemyCounter--;
					parent.Coins = parent.Coins+2;
				}else
					s.calcDmg(50, false);
				
				return true;
			}
			if (s instanceof EnemyBoss) {
				// parent.createExplosion((int)getX(),(int)getY());
				// parent.createExplosion((int)s.getX(),(int)s.getY());
				remove = true;
				if(s.calcDmg(25, true)==true){
					s.remove = true;
					parent.finished = true;
					parent.stopGame();
				}else
					s.calcDmg(25, true);
				
				return true;
			}
		}
		if (this.intersects(s)) {
			if (s instanceof TileBlock) {
				remove = true;
				return true;
			}
		}
		return false;
	}

}
