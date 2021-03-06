package Client;

import java.awt.image.BufferedImage;

/***********************************
 * Die Enemy Class dient als Basis *
 * s�mtlicher GegnerTypen mit      *
 * der passenden Logik dahinter	   *
 ***********************************/
public class Enemy extends Sprite {

	private static final long serialVersionUID = 1L;
	
	private int oldX;
	private int oldY;
	private int dis;
	private int id;
	private int type;
	private static int hx;
	private static int hy;
	
	double deltaX;
	double deltaY;
	
	boolean resting;
	boolean moving;
	boolean MP = false;
		
	String str, wea;

	public Enemy(BufferedImage[] i, double x, double y, long delay, GamePanel p, int type, int id, String strength, String weakness) {
		super(i, x, y, delay, p);
		this.type = type;
		this.id = id;
		this.x = x;
		this.y = y;
		this.str = strength;
		this.wea = weakness;
	}
	
	public Enemy(BufferedImage[] i, double x, double y, long delay, GamePanel p, String strength, String weakness) {
		super(i, x, y, delay, p);
		this.x = x;
		this.y = y;
		this.str = strength;
		this.wea = weakness;
	}
	
	public Enemy(BufferedImage[] i, double x, double y, long delay, GamePanel p, boolean isClient, int id, String strength, String weakness) {
		super(i, x, y, delay, p);
		this.MP = isClient;
		this.id = id;
		this.x = x;
		this.y = y;
		this.str = strength;
		this.wea = weakness;
	}

	@Override
	public void doLogic(long delta) {
		/*******************************************
		 * Logik Funktion f�r die jeweilige Klasse *
		 *******************************************/
		super.doLogic(delta);
		if(!MP){
			deltaX =Math.abs((this.getX()/16)-(hx/16));
			deltaY =Math.abs(this.getY()/16-hy/16);
			dis =(int) Math.sqrt((deltaX*deltaX)+(deltaY*deltaY));
			
			if(dis <= 4){
				chasePlayer();			
			}else{
				if(!resting){
					moving = true;
					moveRandom();
					resting = true;					
				}
			}
				resting = false;	
		}
	}
	
	private int mDirection = -1;
	private int mSteps = 0;
	
	private void moveRandom() {
		if(!MP){
			if (mDirection == -1) {
				mDirection = (int) (Math.random() * 4);
			}
			
			if (mSteps < 10) {
				moveene(mDirection);
				if(this == parent.sli){
					this.setLoop(0, 3);
				}
				mSteps++;
			} else {
				mDirection = -1;
				mSteps = 0;
			}
		}
	}

	private void chasePlayer() {
		if(!MP){
			if (!parent.dead) {
				if (getY() > parent.hero.getY()) {
				MoveYEh();
				if(this == parent.sli){
					this.setLoop(0, 3);
				}
				}
				if (getY() < parent.hero.getY()) {
				MoveYEd();
				if(this == parent.sli){
					this.setLoop(0, 3);
				}
				}
				if (getX() > parent.hero.getX()) {
				MoveXEl();
				if(this == parent.sli){
					this.setLoop(0, 3);
				}
				}
				if (getX() < parent.hero.getX()) {
				MoveXEr();
				if(this == parent.sli){
					this.setLoop(0, 3);
				}
				}
			}
		}
	}

	private void moveene(int i){
		if(!MP){
			switch(i){
			case 0:
					oldY = ((int) (y / 16));	
					if (parent.leveldata[((int) (y / 16)) - 1][((int) (x / 16))] == 1) {
						mDirection = -1;
					} else if (parent.leveldata[((int) (y / 16)) - 1][((int) (x / 16))] == 4) {
						mDirection = -1;
					} else
					y--;
				break;
			case 1:
					oldY = ((int) (y / 16));
		
					if (parent.leveldata[((int) (y / 16)) + 1][((int) (x / 16))] == 1) {
						mDirection = -1;
					} else if (parent.leveldata[((int) (y / 16)) + 1][((int) (x / 16))] == 4) {
						mDirection = -1;
					} else
					y++;
				break;
			case 2:
					oldX = ((int) (x / 16));
		
					if (parent.leveldata[((int) (y / 16))][((int) (x / 16) + 1)] == 1) {
						mDirection = -1;
					} else if (parent.leveldata[((int) (y / 16))][((int) (x / 16) + 1)] == 4) {
						mDirection = -1;
					} else
					x++;
				break;
			case 3:
					oldX = ((int) (x / 16));
		
					if (parent.leveldata[((int) (y / 16))][((int) (x / 16) - 1)] == 1) {
						mDirection = -1;
					} else if (parent.leveldata[((int) (y / 16))][((int) (x / 16) - 1)] == 4) {
						mDirection = -1;
					} else
					x--;
				break;
			}
		}
	}
	
	private void MoveYEh() {

		oldY = ((int) (y / 16));

		if (parent.leveldata[((int) (y / 16))- 1][((int) (x / 16)) ] == 1) {
			y = oldY * 16;
		} else if (parent.leveldata[((int)(y/16))-1][((int)(x/16))]== 2 || parent.leveldata[((int) (y / 16))-1][((int) (x / 16))] == 3 || parent.leveldata[((int) (y / 16) - 1)][((int) (x / 16))] == 4) {
			y = oldY * 16;
		} else
			y--;
		oldY = (int) y;

	}
	
	private void MoveYEd() {

		oldY = ((int) (y / 16));

		if (parent.leveldata[((int) (y / 16)) + 1][((int) (x / 16))] == 1) {
			y = oldY * 16;
		} else if (parent.leveldata[((int)(y/16))+1][((int)(x/16))]== 2 || parent.leveldata[((int) (y / 16))+1][((int) (x / 16))] == 3 || parent.leveldata[((int) (y / 16)) + 1][((int) (x / 16))] == 4) {
			y = oldY * 16;
		} else
			y++;
		oldY = (int) y;

	}
	
	private void MoveXEr() {

		oldX = ((int) (x / 16));

		if (parent.leveldata[((int) (y / 16))][((int)(x / 16)) + 1] == 1) {
			x = oldX * 16;
		} else if (parent.leveldata[((int)(y/16))][((int)(x/16))+1]== 2 || parent.leveldata[((int) (y / 16))][((int) (x / 16)) + 1] == 3 || parent.leveldata[((int) (y / 16))][((int) (x / 16)) + 1] == 4) {
			x = oldX * 16;
		} else
			x++;		
		oldX = (int)x;

	}
	private void MoveXEl() {

		oldX = ((int) (x / 16));

		if (parent.leveldata[((int) (y / 16))][((int) (x / 16)) - 1] == 1) {
			x = oldX * 16;
		} else if (parent.leveldata[((int)(y/16))][((int)(x/16))-1]== 2 || parent.leveldata[((int) (y / 16))][((int) (x / 16)) - 1] == 3 || parent.leveldata[((int) (y / 16))][((int) (x / 16)) - 1] == 4) {
			x = oldX * 16;
		} else
			x--;
		oldX = (int)x;

	}

	@Override
	public boolean collidedWith(Sprite s) {
		return false;
	}

	public void remove() {
		this.remove();		
	}
	
	public static void getCoor(int x, int y){
		/****************************************************************************
		 * getCoor dient dazu der Enemy.class die Position des Spielers mitzuteilen *
		 ****************************************************************************/
		hx = x;
		hy = y;
	}

	public int getID() {
		/**********************************
		 * gibt die ID des Gegners wieder *
		 **********************************/
		return id;
	}
	
	public int getType(){
		/*****************************
		 * gibt die Gegnerart wieder *
		 *****************************/
		return type;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public String getStrength(){
		return str;
	}
	
	public String getWeakness(){
		return wea;
	}
}
