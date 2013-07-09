package Client;

import java.awt.image.BufferedImage;


public class Item extends Sprite{
    
    private static final long serialVersionUID = 1L;
    
    boolean b;
    
    public Item(BufferedImage[] i, double x, double y, long delay, GamePanel p, boolean b){
        super(i, x, y, delay, p);
        this.b = b;
    }
    
    @Override
    public void doLogic(long delta){
        super.doLogic(delta);

    }

	@Override
	public boolean collidedWith(Sprite s) {
		if (this.intersects(s) && this == parent.sword) {
			if (s instanceof Enemy) {
				if(parent.sup || parent.sdown || parent.sleft || parent.sright){
					if(s.calcDmg(50, false)==true){
						s.remove = true;
						parent.SpawnItem(s.getX(), s.getY(),false);
						parent.EnemyCounter--;
						parent.Coins = parent.Coins+2;
					}else
						s.calcDmg(50, false);				
					return true;
				}
			}
		}
		return false;
	}
    
}
