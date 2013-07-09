package Client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {
	
	int dir;
	boolean up, down, left, right;
	GamePanel parent;
	
	public InputHandler(GamePanel game){
		game.addKeyListener(this);
	}
	
	public void keyPressed(KeyEvent e){
		if (!parent.dead) {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				up = true;
				this.dir = 1;
				parent.hero.setLoop(0, 2);
				down = false;
				left = false;
				right = false;
			}

			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				down = true;
				dir = 3;
				parent.hero.setLoop(6, 8);
				up = false;
				left = false;
				right = false;
			}

			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				left = true;
				dir = 2;
				parent.hero.setLoop(9, 11);
				down = false;
				up = false;
				right = false;
			}

			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				right = true;
				dir = 4;
				parent.hero.setLoop(3, 5);
				down = false;
				left = false;
				up = false;
			}
			if(e.getKeyCode()== KeyEvent.VK_SPACE){
				if(dir==1){
					parent.yy = -4;
					parent.xx = 0;
					parent.sword.setLoop(1,3);
					parent.sup = true;
				}else if(dir == 2){
					parent.xx = -16;
					parent.yy = +16;
					parent.sword.setLoop(7,9);
					parent.sleft = true;
				}else if(dir == 3){
					parent.yy = +32;
					parent.xx = 0;
					parent.sword.setLoop(4,6);
					parent.sdown = true;
				}else if(dir == 4){
					parent.xx = +16;
					parent.yy = +16;
					parent.sword.setLoop(10,12);
					parent.sright = true;
				}
			}
		}
	}
	
	public void keyReleased(KeyEvent e){
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			up = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			down = false;			
		}

		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			left = false;			
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (parent.isStarted()) {
				parent.stopGame();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_F1){
			parent.hero.addMana(100);
		}
		if (e.getKeyCode() == KeyEvent.VK_F) {
			if (!parent.dead) {
				if (!parent.OoM && parent.mana >= 25) {
					if (dir != 0) {
						parent.createBolt();
					}
				}
			}
		}
		if(e.getKeyCode()== KeyEvent.VK_SPACE){
			if(dir==1){
				parent.sword.setLoop(0,0);
			}else if(dir == 2){
				parent.sword.setLoop(0,0);
			}else if(dir == 3){
				parent.sword.setLoop(0,0);
			}else if(dir == 4){
				parent.sword.setLoop(0,0);
			}
			parent.sup = false;
			parent.sleft = false;
			parent.sdown = false;
			parent.sright = false;
		}
		if(parent.lvl == 1){
			if(parent.npc.dis == 1 && parent.lvl==1){
				if(e.getKeyCode()== KeyEvent.VK_E){
					parent.page++;
					if(parent.page>3)
						parent.page=0;
				}
			}else
				parent.page = 0;
		}
		if(parent.lvl>1){
			if(parent.shopowner != null && parent.shopowner.dis == 1){
				if(e.getKeyCode()==KeyEvent.VK_E){
					parent.page++;
					if(parent.page>1)
						parent.page=0;
				}
			}else
				parent.page = 0;
		}
		if(e.getKeyCode() == KeyEvent.VK_1 && parent.Coins >=5 && parent.showtext == true){
			parent.hero.addHealth(25, parent.phealth);
			parent.Coins = parent.Coins - 5;
			parent.page = 1;
		}
		if(e.getKeyCode() == KeyEvent.VK_1 && parent.Coins <5 && parent.showtext == true){
			parent.page = 2;
		}
		if(e.getKeyCode() == KeyEvent.VK_2 && parent.Coins >=7 && parent.showtext == true){
			parent.hero.addMana(25);
			parent.Coins = parent.Coins - 7;
		}
		if(e.getKeyCode() == KeyEvent.VK_2 && parent.Coins <7 && parent.showtext == true){
			parent.page = 2;
		}
		if(e.getKeyCode() == KeyEvent.VK_3 && parent.Coins >=30 && parent.showtext == true){
			parent.Coins =parent. Coins - 30;
		}
		if(e.getKeyCode() == KeyEvent.VK_3 && parent.Coins <30 && parent.showtext == true){
			parent.page = 2;
		}
	}
	
	public void keyTyped(KeyEvent e){
	}
	
}
