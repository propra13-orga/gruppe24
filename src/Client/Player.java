package Client;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;

public class Player extends Sprite{

	private static final long serialVersionUID = 1L;
	public String Username;

	public Player(BufferedImage[] i, double x, double y, long delay, GamePanel p) {//
		super(i, x, y, delay, p);	
		
		
	}

	@Override
	public void doLogic(long delta) {
		super.doLogic(delta);
		
		NPC.getCoor((int)this.x, (int)this.y);
		Enemy.getCoor((int)this.x, (int)this.y);
		EnemyBoss.getCoor((int)this.x, (int)this.y);

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
			
			if (s instanceof MagicBolt) {
				parent.phealth=this.phealth;
				s.remove = true;
				if(this.calcDmgPlayer()==true){
					remove = true;
					parent.dead = true;
				}else
					this.calcDmgPlayer();	
				System.out.println("test dmg");
				return true;
			}
			
			if( s instanceof Item){
				s.remove = true;
				Random r = new Random();
				int rn = r.nextInt((int)System.currentTimeMillis());
				if(rn%2 == 0){
					System.out.println("Health Porion");					
				}
				if(rn%2 == 1){
					System.out.println("Mana Potion");
				}
				return true;
			}
		}
		return false;
	}
    public String getUsername() {//
        return this.Username;//
    }//
}
