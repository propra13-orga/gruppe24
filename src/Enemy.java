import java.awt.image.BufferedImage;

public class Enemy extends Sprite {

	private static final long serialVersionUID = 1L;
	int oldX;
	int oldY;

	// static boolean hit = false;
	// static boolean dead = false;
	// int life;

	public Enemy(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
		super(i, x, y, delay, p);

		/*
		 * life = 100; if (hit) { System.out.println("test"); reduceLife(); if
		 * (life == 0) { dead = true; } hit = false; }
		 */
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

	/*
	 * private void reduceLife() { life = life - 50; System.out.println("hit");
	 * }
	 */

	private void MoveYEh() {

		oldY = ((int) (y / 16));

		if (parent.leveldata[((int) (y / 16)) - 1][((int) (x / 16))] == 1) {
			y = oldY * 16;
		} else if (parent.leveldata[((int) (y / 16)) - 1][((int) (x / 16))] == 4) {
			y = oldY * 16;
		} else
			y--;
		oldY = (int) y;

	}

	private void MoveYEd() {

		oldY = ((int) (y / 16));

		if (parent.leveldata[((int) (y / 16)) + 1][((int) (x / 16))] == 1) {
			y = oldY * 16;
		} else if (parent.leveldata[((int) (y / 16)) + 1][((int) (x / 16))] == 4) {
			y = oldY * 16;
		} else
			y++;
		oldY = (int) y;

	}

	private void MoveXEr() {

		oldX = ((int) (x / 16));

		if (parent.leveldata[((int) (y / 16))][((int)(x / 16)) + 1] == 1) {
			x = oldX * 16;
		} else if (parent.leveldata[((int) (y / 16))][((int) (x / 16)) + 1] == 4) {
			x = oldX * 16;
		} else
			x++;

	}

	private void MoveXEl() {

		oldX = ((int) (x / 16));

		if (parent.leveldata[((int) (y / 16))][((int) (x / 16)) - 1] == 1) {
			x = oldX * 16;
		} else if (parent.leveldata[((int) (y / 16))][((int) (x / 16)) - 1] == 4) {
			x = oldX * 16;
		} else
			x--;

	}

	@Override
	public boolean collidedWith(Sprite s) {

		if (this.intersects(s)) {
			if (s instanceof Enemy) {

				if (s.getY() > this.getY()) {
					s.y = oldY;
					this.y = this.oldY;
				}

				return true;
			}
			if(s instanceof Player){
				s.remove = true;
				return true;
			}
		}
		/*
		 * if (this.intersects(s)) { if (s instanceof MagicBolt) { reduceLife();
		 * if (life == 0) { dead = true; } return true; } }
		 */
		return false;
	}
}
