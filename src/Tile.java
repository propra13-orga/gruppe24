import java.awt.image.BufferedImage;

public class Tile extends Enviroment {

	private static final long serialVersionUID = 1L;

	boolean test = true;

	public Tile(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
		super(i, x, y, delay, p);
	}

	@Override
	public void doLogic(long delta) {
		super.doLogic(delta);

	}

}
