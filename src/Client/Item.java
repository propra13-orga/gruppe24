package Client;

import java.awt.image.BufferedImage;

/*******************************
 * Basis Klasse für alle Items *
 *******************************/

public class Item extends Sprite {

	private static final long serialVersionUID = 1L;

	boolean b;
	String atribut;

	public Item(BufferedImage[] i, double x, double y, long delay, GamePanel p,
			boolean b, String atr) {
		super(i, x, y, delay, p);
		this.b = b;
		this.atribut = atr;
	}

	@Override
	public void doLogic(long delta) {
		super.doLogic(delta);
	}

	@Override
	public boolean collidedWith(Sprite s) {
		/********************************************
		 * Kollisionsabfrage von Schwert und Gegner *
		 ********************************************/
		
		if (this.intersects(s) && this == parent.sword) {
			if (s instanceof Enemy) {
				if (parent.sup || parent.sdown || parent.sleft || parent.sright) {
					if (((Enemy) s).str.equals(this.atribut)) {
						System.out.println("Immun");
						return false;
					} else if (((Enemy) s).wea.equals(this.atribut)) {
						if (s.calcDmg(75, false) == true) {
							s.remove = true;
							parent.SpawnItem(s.getX(), s.getY(), false);
							parent.EnemyCounter--;
							parent.Coins = parent.Coins + 2;
						} else
							s.calcDmg(75, false);
						return true;
					} else {
						if (s.calcDmg(45, false) == true) {
							s.remove = true;
							parent.SpawnItem(s.getX(), s.getY(), false);
							parent.EnemyCounter--;
							parent.Coins = parent.Coins + 2;
						} else
							s.calcDmg(45, false);
						return true;
					}
				}
			}
		}
		return false;
	}

}
