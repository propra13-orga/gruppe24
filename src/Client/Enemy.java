package Client;

import java.awt.image.BufferedImage;

public class Enemy extends Sprite implements Runnable {

	private static final long serialVersionUID = 1L;
	
	int oldX;
	int oldY;
	int j;
	int dis;
	int id;
	static int hx;
	static int hy;
	
	double deltaX;
	double deltaY;
	
	boolean resting;
	boolean moving;
	boolean MP = false;
	
	long start;

	public Enemy(BufferedImage[] i, double x, double y, long delay, GamePanel p, int id) {
		super(i, x, y, delay, p);
		this.id = id;

	}
	
	public Enemy(BufferedImage[] i, double x, double y, long delay, GamePanel p, boolean isClient) {
		super(i, x, y, delay, p);
		this.MP = isClient;
	}

	@Override
	public void doLogic(long delta) {
		super.doLogic(delta);
		
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
	
	private int mDirection = -1;
	private int mSteps = 0;
	
	private void moveRandom() {
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

	private void chasePlayer() {
		/*if (!parent.dead) {
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
		}*/
	}

	@SuppressWarnings("static-access")
	private void moveene(int i){
		switch(i){
		case 0:
				oldY = ((int) (y / parent.Tilesize));	
				if (parent.leveldata[((int) (y / parent.Tilesize)) - 1][((int) (x / parent.Tilesize))] == 1) {
					mDirection = -1;
				} else if (parent.leveldata[((int) (y / parent.Tilesize)) - 1][((int) (x / parent.Tilesize))] == 4) {
					mDirection = -1;
				} else
				y--;
			break;
		case 1:
				oldY = ((int) (y / parent.Tilesize));
	
				if (parent.leveldata[((int) (y / parent.Tilesize)) + 1][((int) (x / parent.Tilesize))] == 1) {
					mDirection = -1;
				} else if (parent.leveldata[((int) (y / parent.Tilesize)) + 1][((int) (x / parent.Tilesize))] == 4) {
					mDirection = -1;
				} else
				y++;
			break;
		case 2:
				oldX = ((int) (x / parent.Tilesize));
	
				if (parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize) + 1)] == 1) {
					mDirection = -1;
				} else if (parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize) + 1)] == 4) {
					mDirection = -1;
				} else
				x++;
			break;
		case 3:
				oldX = ((int) (x / parent.Tilesize));
	
				if (parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize) - 1)] == 1) {
					mDirection = -1;
				} else if (parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize) - 1)] == 4) {
					mDirection = -1;
				} else
				x--;
			break;
		}
	}
	
	@SuppressWarnings("static-access")
	private void MoveYEh() {

		oldY = ((int) (y / parent.Tilesize));

		if (parent.leveldata[((int) (y / parent.Tilesize))- 1][((int) (x / parent.Tilesize)) ] == 1) {
			y = oldY * parent.Tilesize;
		} else if (parent.leveldata[((int)(y/parent.Tilesize))-1][((int)(x/parent.Tilesize))]== 2 || parent.leveldata[((int) (y / parent.Tilesize))-1][((int) (x / parent.Tilesize))] == 3 || parent.leveldata[((int) (y / parent.Tilesize) - 1)][((int) (x / parent.Tilesize))] == 4) {
			y = oldY * parent.Tilesize;
		} else
			y--;
		oldY = (int) y;
		//parent.leveldata[(int)y/parent.Tilesize][(int)x/parent.Tilesize]=3;

	}
	@SuppressWarnings("static-access")
	private void MoveYEd() {

		oldY = ((int) (y / parent.Tilesize));

		if (parent.leveldata[((int) (y / parent.Tilesize)) + 1][((int) (x / parent.Tilesize))] == 1) {
			y = oldY * parent.Tilesize;
		} else if (parent.leveldata[((int)(y/parent.Tilesize))+1][((int)(x/parent.Tilesize))]== 2 || parent.leveldata[((int) (y / parent.Tilesize))+1][((int) (x / parent.Tilesize))] == 3 || parent.leveldata[((int) (y / parent.Tilesize)) + 1][((int) (x / parent.Tilesize))] == 4) {
			y = oldY * parent.Tilesize;
		} else
			y++;
		oldY = (int) y;
		//parent.leveldata[(int)y/parent.Tilesize][(int)x/parent.Tilesize]=3;

	}
	@SuppressWarnings("static-access")
	private void MoveXEr() {

		oldX = ((int) (x / parent.Tilesize));

		if (parent.leveldata[((int) (y / parent.Tilesize))][((int)(x / parent.Tilesize)) + 1] == 1) {
			x = oldX * parent.Tilesize;
		} else if (parent.leveldata[((int)(y/parent.Tilesize))][((int)(x/parent.Tilesize))+1]== 2 || parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize)) + 1] == 3 || parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize)) + 1] == 4) {
			x = oldX * parent.Tilesize;
		} else
			x++;		
		oldX = (int)x;
		//parent.leveldata[(int)y/parent.Tilesize][(int)x/parent.Tilesize]=3;

	}
	@SuppressWarnings("static-access")
	private void MoveXEl() {

		oldX = ((int) (x / parent.Tilesize));

		if (parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize)) - 1] == 1) {
			x = oldX * parent.Tilesize;
		} else if (parent.leveldata[((int)(y/parent.Tilesize))][((int)(x/parent.Tilesize))-1]== 2 || parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize)) - 1] == 3 || parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize)) - 1] == 4) {
			x = oldX * parent.Tilesize;
		} else
			x--;
		oldX = (int)x;
		//parent.leveldata[(int)y/parent.Tilesize][(int)x/parent.Tilesize]=3;

	}

	@Override
	public boolean collidedWith(Sprite s) {
		return false;
	}

	public void remove() {
		this.remove();		
	}
	
	public static void getCoor(int x, int y){
		hx = x;
		hy = y;
	}

	@Override
	public void run() {
		
	}

	public int getID() {
		return id;
	}
}
