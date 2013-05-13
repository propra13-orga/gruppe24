import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MagicBolt extends Sprite {

	private static final long serialVersionUID = 1L;

	boolean bUp = false;
	boolean bDown = false;
	boolean bLeft = false;
	boolean bRight = false;

	Rectangle2D.Double target;

	public MagicBolt(BufferedImage[] i, double x, double y, long delay,
			GamePanel p) {
		super(i, x, y, delay, p);

		this.x = x;
		this.y = y;

		if (parent.dir==1) {
			bUp = true;
		}
		if(parent.dir==3){
			bDown = true;
		}
		if(parent.dir==2){
			bLeft = true;
		}
		if(parent.dir==4){
			bRight = true;
		}
	}

	@Override
	public void doLogic(long delta) {
		super.doLogic(delta);
			
		if(bUp && getY() > 0){
			y -= 10;
		}
		if (bDown && getY()/16 < 15){
			y += 10;
		}
		if (bLeft && getX() > 0) {
			x -= 10;
		}
		if (bRight && getX() / 16 < 15) {
			x += 10;
		}
		if (getX() == 0) {
			remove = true;
		}
		if (getX() / 16 == 15) {
			remove = true;
		}
		if (getY() == 0) {
			remove = true;
		}
		if (getY() / 16 == 15) {
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
				s.remove = true;
				System.out.println("Test");
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
