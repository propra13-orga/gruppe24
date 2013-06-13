package Client;

import java.awt.image.BufferedImage;
import java.net.InetAddress;


public class PlayerMP extends Player {
	
	private static final long serialVersionUID = 1L;
    public InetAddress ipAddress;
    public int port;

	
	public PlayerMP(BufferedImage[] i, double x, double y, long delay,GamePanel p) {
		super(i, x, y, delay, p);
    }
}
