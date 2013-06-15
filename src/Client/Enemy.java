package Client;

import java.awt.image.BufferedImage;

public class Enemy extends Sprite {

	private static final long serialVersionUID = 1L;
	int oldX;
	int oldY;
	
	double deltaX;
	double deltaY;
	int dis;
	static int hx;
	static int hy;

	public Enemy(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
		super(i, x, y, delay, p);

	}

	@Override
	public void doLogic(long delta) {
		super.doLogic(delta);
		
		deltaX =Math.abs((this.getX()/16)-(hx/16));
		deltaY =Math.abs(this.getY()/16-hy/16);
		dis =(int) Math.sqrt((deltaX*deltaX)+(deltaY*deltaY));
		
		
		if(dis <= 4){
			chasePlayer();
			
		} else {
			moveRandom();
		}
		
//		else if((flag ==0) && (count <=2)){
//			moveene(flag);
//			count =+ 1;
//		}else if((flag ==1) && (count <=2)){
//			moveene(flag);
//			count =+ 1;
//		}else if((flag ==2) && (count <=2)){
//			moveene(flag);
//			count =+ 1;
//		}else if((flag ==3) && (count <=2)){
//			moveene(flag);
//			count =+ 1;
//		}
		
//		else if(rn == 0){
//			moveene(rn);
//			flag = rn;
//			
//		}else if(rn == 1){
//			moveene(rn);
//			flag = rn;
//			
//		}else if(rn == 2){
//			moveene(rn);
//			flag = rn;
//		
//		}else if(rn == 3){
//			moveene(rn);
//			flag = rn;
//			
//		}
//		if(count == 3)
//			count = 0;
		
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
			
		//	parent.leveldata[(int)y/parent.Tilesize][(int)x/parent.Tilesize]=3;
			break;
		case 1:
			oldY = ((int) (y / parent.Tilesize));

			if (parent.leveldata[((int) (y / parent.Tilesize)) + 1][((int) (x / parent.Tilesize))] == 1) {
				mDirection = -1;
			} else if (parent.leveldata[((int) (y / parent.Tilesize)) + 1][((int) (x / parent.Tilesize))] == 4) {
				mDirection = -1;
			} else
			y++;
		//	parent.leveldata[(int)y/parent.Tilesize][(int)x/parent.Tilesize]=3;
			break;
		case 2:
			oldY = ((int) (y / parent.Tilesize));

			if (parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize) + 1)] == 1) {
				mDirection = -1;
			} else if (parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize) + 1)] == 4) {
				mDirection = -1;
			} else
			x++;
		//	parent.leveldata[(int)y/parent.Tilesize][(int)x/parent.Tilesize]=3;
			break;
		case 3:
			oldY = ((int) (y / parent.Tilesize));

			if (parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize) - 1)] == 1) {
				mDirection = -1;
			} else if (parent.leveldata[((int) (y / parent.Tilesize))][((int) (x / parent.Tilesize) - 1)] == 4) {
				mDirection = -1;
			} else
			x--;
			//parent.leveldata[(int)y/parent.Tilesize][(int)x/parent.Tilesize]=3;
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
}
