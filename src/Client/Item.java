package Client;

import java.awt.image.BufferedImage;


public class Item extends Sprite{
    
    private static final long serialVersionUID = 1L;
    
    public Item(BufferedImage[] i, double x, double y, long delay, GamePanel p){
        super(i, x, y, delay, p);
    }
    
    @Override
    public void doLogic(long delta){
        super.doLogic(delta);

    }

	@Override
	public boolean collidedWith(Sprite s) {		
		return false;
	}
    
}
