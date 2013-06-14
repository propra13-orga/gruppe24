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
		return false;
	}
    
}