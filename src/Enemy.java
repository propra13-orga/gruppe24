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
		
		oldX = (int)x;

	}

	private void MoveXEl() {

		oldX = ((int) (x / 16));

		if (parent.leveldata[((int) (y / 16))][((int) (x / 16)) - 1] == 1) {
			x = oldX * 16;
		} else if (parent.leveldata[((int) (y / 16))][((int) (x / 16)) - 1] == 4) {
			x = oldX * 16;
		} else
			x--;
		
		oldX = (int)x;

	}

	@Override
	public boolean collidedWith(Sprite s) {

		if (this.intersects(s)) {
			if(s instanceof Player){
				s.remove = true;
				return true;
			}
		}
		return false;
	}
}
