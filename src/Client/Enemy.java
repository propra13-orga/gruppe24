package Client;

import java.awt.image.BufferedImage;

public class Enemy extends Sprite {

	private static final long serialVersionUID = 1L;
	int oldX;
	int oldY;

	public Enemy(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
		super(i, x, y, delay, p);

	}

	@Override
	public void doLogic(long delta) {
		super.doLogic(delta);
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
}
