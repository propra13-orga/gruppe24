import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Player extends Sprite {

	private static final long serialVersionUID = 1L;
	//BufferedImage[] player[];

	public Player(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
		super(i, x, y, delay, p);
	}

	@Override
	public void doLogic(long delta) {
		super.doLogic(delta);

		/*switch (parent.dir) {

		case 1:
			BufferedImage[1] player = parent.loadPics("pics/player.gif", 4);
			break;
		case 2:
			parent.player[2] = loadPics("pics/playerleft", 3);
			break;
		case 3:
		}*/

	}
	
	BufferedImage[] loadPics(String path, int pics) { // Methode bekommt Speicherort und Anzahl der Einzelbilder übergeben

		BufferedImage[] anim = new BufferedImage[pics]; // Erzeugung des Image-Arrays mit der größe der Einzelbilder
		BufferedImage source = null; // lädt das ganze Bild

		URL pic_url = getClass().getClassLoader().getResource(path); // Ermittelung der URL des Speicherortes, wird als Pfadangabe übergeben

		try {
			source = ImageIO.read(pic_url); // Quellbild wird über ImageIO geladen
		} catch (IOException e) {}

		for (int x = 0; x < pics; x++) {
			anim[x] = source.getSubimage(x * source.getWidth() / pics, 0,
					source.getWidth() / pics, source.getHeight()); // die Methode getSubimage zerlegt das Quellbild in die Anzahl der angegebenen Einzelbilder.
		}

		return anim;
	}
	@Override
	public boolean collidedWith(Sprite s) {
		if (this.intersects(s)) {
			if (s instanceof Enemy) {
				remove = true;
				System.out.println("Tot");
				parent.gameover = 1;
				return true;
			}
		}
		
		return false;
	}
}
