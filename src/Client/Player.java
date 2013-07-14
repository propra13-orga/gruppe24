package Client;

import java.awt.image.BufferedImage;

/**************************************************************************************
 * Die Player.class ist die Basis für alle Logik-Operationen die vom Player ausgehen  *
 **************************************************************************************/
public class Player extends Sprite{

	private static final long serialVersionUID = 1L;
	int dir;

	public Player(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
		super(i, x, y, delay, p);			
		this.parent = p;
	}

	@Override
	public void doLogic(long delta) {
		super.doLogic(delta);
		
		NPC.getCoor((int)this.x, (int)this.y);
		Enemy.getCoor((int)this.x, (int)this.y);
		EnemyBoss.getCoor((int)this.x, (int)this.y);
		this.dir = parent.dir;
		if(this.dir==1){
			this.setLoop(0,2);
		}else if(this.dir==2){
			this.setLoop(9,11);
		}else if(this.dir==3){
			this.setLoop(6,8);
		}else if(this.dir==4){
			this.setLoop(3,5);
		}
	}
	
	
	/**
	 * Pixelgenaue Kollisionsanalyse mit dem Player und seiner Umgebung
	 * 1. Player mit Gegner
	 * 2. Player mit gegnerischem MagicBolt
	 * 3. Player mit Items
	 */
	@Override
	public boolean collidedWith(Sprite s) {
		if(remove){
			return false;
		}
		if (this.intersects(s)) {
			if (s instanceof Enemy) {
				if(this.checkOpaqueColorCollisions(s)==true){
					parent.phealth=this.phealth;
					if(this.calcDmgPlayer()==true){
						remove = true;
						parent.life--;
						if(parent.life < 0){parent.life=0;}
						parent.dead = true;
					}else
						this.calcDmgPlayer();				
					return true;
				}
			}
			
			if (s instanceof MagicBolt) {
				if(this.checkOpaqueColorCollisions(s)==true){
					parent.phealth=this.phealth;
					s.remove = true;
					if(this.calcDmgPlayer()==true){
						remove = true;
						parent.dead = true;
					}else
						this.calcDmgPlayer();	
					return true;
				}
			}
			
			if( s instanceof Item && s != parent.sword){
				if(this.checkOpaqueColorCollisions(s)==true){
					s.remove = true;
					if(s.b == true){
						parent.generateItem(true, 5, true, 20);
					}else
						parent.generateItem(true, 9, true, 9);
					return true;
				}
			}
		}
		return false;
	}
}
