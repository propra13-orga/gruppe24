package Client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Player extends Sprite{

	private static final long serialVersionUID = 1L;
	private String username;//

	public Player(BufferedImage[] i, double x, double y, long delay, GamePanel p) {//
		super(i, x, y, delay, p);	
		
		
	}

	@Override
	public void doLogic(long delta) {
		super.doLogic(delta);


	}
	
	BufferedImage[] loadPics(String path, int pics) { // Methode bekommt Speicherort und Anzahl der Einzelbilder �bergeben

		BufferedImage[] anim = new BufferedImage[pics]; // Erzeugung des Image-Arrays mit der gr��e der Einzelbilder
		BufferedImage source = null; // l�dt das ganze Bild

		URL pic_url = getClass().getClassLoader().getResource(path); // Ermittelung der URL des Speicherortes, wird als Pfadangabe �bergeben

		try {
			source = ImageIO.read(pic_url); // Quellbild wird �ber ImageIO geladen
		} catch (IOException e) {}

		for (int x = 0; x < pics; x++) {
			anim[x] = source.getSubimage(x * source.getWidth() / pics, 0,
					source.getWidth() / pics, source.getHeight()); // die Methode getSubimage zerlegt das Quellbild in die Anzahl der angegebenen Einzelbilder.
		}

		return anim;
	}
	@Override
	public boolean collidedWith(Sprite s) {
		if(remove){
			return false;
		}
		if (this.intersects(s)) {
			if (s instanceof Enemy) {
				parent.phealth=this.phealth;
				if(this.calcDmgPlayer()==true){
					remove = true;
					parent.dead = true;
				}else
					this.calcDmgPlayer();				
				return true;
			}
		}
		
		return false;
	}
    public String getUsername() {//
        return this.username;//
    }//
}