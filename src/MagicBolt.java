import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MagicBolt extends Sprite{
	
	private static final long	serialVersionUID	= 1L;
        
        boolean bUp =true;
        boolean bDown = true;
        boolean bLeft = true;
        boolean bRight = true;
        boolean wait = false;

	Rectangle2D.Double target;
	
	public MagicBolt(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
		super(i, x, y, delay, p);
                
                this.x = x;
		this.y = y;
		
	}

	@Override
	public void doLogic(long delta) {
		super.doLogic(delta);
		
               
	
            if(!wait){
                if(parent.dirU){
                    
                    if(getY()>0){
                        y=y-10;
                    }
                }
            }
            if(!wait){
                if(parent.dirD){
                    if(getY()/16<15){
                        y=y+10;
                    }
                }
            }
            if(!wait){
                if(parent.dirL){
                    if(getX()>0){
                        x=x-10;
                    }
                }
            }
            if(!wait){
                if(parent.dirR){
                    if(getX()/16<15){
                        x=x+10;
                    }
                }
            }
                if(getX()==0){
                    remove = true;
                    parent.dirL=false;
                }
                if(getX()/16==15){
                    remove = true;
                    parent.dirR=false;
                }
                if(getY()==0){
                    remove = true;
                    parent.dirU=false;
                }
                if(getY()/16==15){
                    remove = true;
                    parent.dirD=false;
                }
        }
        

	@Override
	public boolean collidedWith(Sprite s) {

		
		if(this.intersects(s)){
                    if(s instanceof Enemy){
                        //parent.createExplosion((int)getX(),(int)getY());
			//parent.createExplosion((int)s.getX(),(int)s.getY());
			remove   = true;
			s.remove = true;
                        System.out.println("Test");
			return true;
                    }
		}
                if(this.intersects(s)){
                    if(s instanceof TileBlock){
                        remove = true;
                        return true;
                    }
                }
		
		
		return false;
	}

}
