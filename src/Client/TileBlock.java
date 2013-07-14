package Client;

import java.awt.image.BufferedImage;

/**********************************************************************
 * Basis Klasse für Umgebungsobjekte mit denen man interagieren kann. *
 **********************************************************************/
public class TileBlock extends Sprite{
    
    private static final long serialVersionUID = 1L;
    
    boolean test = true;
    
    public TileBlock(BufferedImage[] i, double x, double y, long delay, GamePanel p){
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
