package Client;

import java.awt.image.BufferedImage;
import java.net.InetAddress;

import javax.swing.JOptionPane;

/**************************************************************************
 * Die selbe Klasse wie die Player.class nur erweitert für den Multiplayer*
 **************************************************************************/
public class PlayerMP extends Player {
	
	private static final long serialVersionUID = 1L;
    public InetAddress ipAddress;
    public int port;
    public String Username;
    GamePanel parent;
    int dir;
	public int id;
	
	public PlayerMP(BufferedImage[] i, double x, double y, String username, InetAddress ipAddress, int port, long delay,GamePanel p, int id) {
		super(i, x, y, delay, p);
		
		this.Username = username;
		this.ipAddress = ipAddress;
		this.port = port;
		this.parent = p;
		this.id = id;
    }
	
	public PlayerMP(BufferedImage[] i, double x, double y, long delay, GamePanel p) {
		super(i, x, y, delay, p);
		if(p.lvl ==1){
			this.Username = JOptionPane.showInputDialog("Please enter your Playername");
		}
		this.parent = p;
		parent.dir = 1;
		
	}
	
	@Override
	public void doLogic(long delta) {
		super.doLogic(delta);
		
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

    public String getUsername() {
        return this.Username;
    }
    
    /********************************************
     * Setzt die "Blickrichtung" der Spielfigur *
     ********************************************/
	public void setMovingDir(int movingDir) {
		parent.dir = movingDir;
		this.dir = movingDir;
	}
}
