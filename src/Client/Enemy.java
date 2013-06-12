package Client;

import java.awt.image.BufferedImage;

public class Enemy extends Sprite {

	private static final long serialVersionUID = 1L;
	int oldX;
	int oldY;
	
	double deltaX;
	double deltaY;
	int dis;
	int rn;
	int rm;
	static int hx;
	static int hy;

	public Enemy(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
		super(i, x, y, delay, p);

	}

	@Override
	public void doLogic(long delta) {
		super.doLogic(delta);
		
		deltaX =Math.abs((this.getX()/16)-(hx/16));
		deltaY =Math.abs(this.getY()/16-hy/16);
		dis =(int) Math.sqrt((deltaX*deltaX)+(deltaY*deltaY));
		rn = (int) (Math.random()*9);
		rm = (int) (Math.random()*15);

		if(dis <= 4){
			if (!parent.dead) {
				if (getY() > parent.hero.getY()) {
				MoveYEh();
				}
				if (getY() < parent.hero.getY()) {
				MoveYEd();
				}
				if (getX() > parent.hero.getX()) {
				MoveXEl();
				}
				if (getX() < parent.hero.getX()) {
				MoveXEr();
				}
			}
		}else if(rn%2 == 0){
			MoveYEh();
		}else if(rm%3 == 1){
			MoveXEl();
		}else if(rn%2 == 1){
			MoveYEd();
		}else if(rm%5 == 0){
			MoveXEr();
		}
	}
	@SuppressWarnings("static-access")
	private void MoveYEh() {

		oldY = ((int) (y / parent.Tilesize));

		if (parent.leveldata[((int) (y / parent.Tilesize)) - 1][((int) (x / parent.Tilesize))] == 1) {
			y = oldY * parent.Tilesize;
		} else if (parent.leveldata[((int) (y / parent.Tilesize)) - 1][((int) (x / parent.Tilesize))] == 4) {
			y = oldY * parent.Tilesize;
		} else
			y--;
		oldY = (int) y;

	}
	@SuppressWarnings("static-access")
	private void MoveYEd() {

		oldY = ((int) (y / parent.Tilesize));

		if (parent.leveldata[((int) (y / parent.Tilesize)) + 1][((int) (x / parent.Tilesize))] == 1) {
			y = oldY * parent.Tilesize;
		} else if (parent.leveldata[((int) (y / parent.Tilesize)) + 1][((int) (x / parent.Tilesize))] == 4) {
			y = oldY * parent.Tilesize;
		} else
			y++;
		oldY = (int) y;

	}
	@SuppressWarnings("static-access")
	private void MoveXEr() {

		oldX = ((int) (x / parent.Tilesize));

		if (parent.leveldata[((int) (y / parent.Tilesize))][((int)(x / parent.Tilesize)) + 1] == 1) {
			x = oldX * parent.Tilesize;
		} else if (parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize)) + 1] == 4) {
			x = oldX * parent.Tilesize;
		} else
			x++;
		
		oldX = (int)x;

	}
	@SuppressWarnings("static-access")
	private void MoveXEl() {

		oldX = ((int) (x / parent.Tilesize));

		if (parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize)) - 1] == 1) {
			x = oldX * parent.Tilesize;
		} else if (parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize)) - 1] == 4) {
			x = oldX * parent.Tilesize;
		} else
			x--;
		
		oldX = (int)x;

	}

	@Override
	public boolean collidedWith(Sprite s) {
		return false;
	}

	public void remove() {
		this.remove();		
	}
	
	public static void getCoor(int x, int y){
		hx = x;
		hy = y;
	}
}
