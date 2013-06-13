package Client;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

@SuppressWarnings("serial")
public class EnemyBoss extends Sprite{
	
	Rectangle2D.Double t;
	Rectangle2D.Double t2;
	boolean locked = false;
	GamePanel gp;
	
	int oldX;
	int oldY;
	double deltaX;
	double deltaY;
	int dis;
	static int hx, hy;

	
	public EnemyBoss(BufferedImage[] i, double x, double y, long delay,
			GamePanel p) {
		super(i, x, y, delay, p);

	}
	public void remove() {
		this.remove();		
	}
	@Override
	public void doLogic(long delta){
		super.doLogic(delta);

		deltaX =Math.abs((this.getX()/16)-(hx/16));
		deltaY =Math.abs(this.getY()/16-hy/16);
		dis =(int) Math.sqrt((deltaX*deltaX)+(deltaY*deltaY));
		if(this.remove != true){
			t = new Rectangle2D.Double(getX(), getY(), getX()/16+1, (getY()/16+8)*16);
			t2 = new Rectangle2D.Double(getX()+getWidth()-8, getY(), getX()/16+1, (getY()/16+8)*16);
		}
		if(dis <= 5){
			if(!locked&&parent.hero.intersects(t)){
				locked = true;
				System.out.println("Target locked");
			}else if(!locked&&parent.hero.intersects(t2)){
				locked = true;
				System.out.println("Target locked");
			}
		}
	}
	
	
	@SuppressWarnings("static-access")
	public void MoveXEr() {

		oldX = ((int) (x / parent.Tilesize));

		if (parent.leveldata[((int) (y / parent.Tilesize))][((int)(x / parent.Tilesize)) + 1] == 1) {
			x = oldX * parent.Tilesize;
		} else if (parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize)) + 1] == 4) {
			x = oldX * parent.Tilesize;
		} else
			x= x+15;
		
		oldX = (int)x;

	}
	@SuppressWarnings("static-access")
	public void MoveXEl() {

		oldX = ((int) (x / parent.Tilesize));

		if (parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize)) - 1] == 1) {
			x = oldX * parent.Tilesize;
		} else if (parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize)) - 1] == 4) {
			x = oldX * parent.Tilesize;
		} else
			x=x-15;
		
		oldX = (int)x;

	}
	

	@Override
	public boolean collidedWith(Sprite s) {
		return false;
	}

	public static void getCoor(int x, int y) {
		hx = x;
		hy = y;
	}
	
	

}
