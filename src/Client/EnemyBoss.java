package Client;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/*******************************************
 * Die EnemyBoss.class ist ähnlich wie die *
 * Enemy.class, nur dient sie als Basis    *
 * für alle zukünftigen Bosse des Spiels   *
 *******************************************/


@SuppressWarnings("serial")
public class EnemyBoss extends Sprite{
	
	Rectangle2D.Double t;
	boolean locked = false;
	GamePanel gp;
	
	int oldX;
	int oldY;
	double deltaX;
	double deltaY;
	double dh;
	int dis;
	int xx;
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
			t = new Rectangle2D.Double(getX()+getWidth()/2, getY(), getX()/16+1, (getY()/16+8)*16);
		}
		if(dis <= 10){
			if(!locked&&parent.hero.intersects(t)){
				locked = true;
				System.out.println("Target locked");
			}
		}
		
		dh = (100*this.bosshealth)/250;
		System.out.println("DH: "+(int)dh);
		if(dh > 75){
			xx = 15;
		}else if(dh <= 75 && dh > 40){
			xx = 25;
		}else if(dh<=40){
			xx = 5;
		}
	}
	
	@SuppressWarnings("static-access")
	public void MoveXEr() {
		
		/************************************************************************
		 * Kollisons abfrage für Wände und dem Boss auf der X-Achse nach rechts *
		 ************************************************************************/

		oldX = ((int) (x / parent.Tilesize));

		if (parent.leveldata[((int) (y / parent.Tilesize))][((int)(x / parent.Tilesize)) + 1] == 1) {
			x = oldX * parent.Tilesize;
		} else if (parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize)) + 1] == 4) {
			x = oldX * parent.Tilesize;
		} else
			x= x+xx;
		
		oldX = (int)x;

	}
	@SuppressWarnings("static-access")
	public void MoveXEl() {
		
		/***********************************************************************
		 * Kollisons abfrage für Wände und dem Boss auf der X-Achse nach links *
		 ***********************************************************************/

		oldX = ((int) (x / parent.Tilesize));

		if (parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize)) - 1] == 1) {
			x = oldX * parent.Tilesize;
		} else if (parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize)) - 1] == 4) {
			x = oldX * parent.Tilesize;
		} else
			x=x-xx;
		
		oldX = (int)x;

	}
	

	@Override
	public boolean collidedWith(Sprite s) {
		return false;
	}

	public static void getCoor(int x, int y) {
		/***************************************************************
		 * getCoor gibt der EnemyBoss.class die Spielerposition wieder *
		 ***************************************************************/
		
		hx = x;
		hy = y;
	}
	
	

}
