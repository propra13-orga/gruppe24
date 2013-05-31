import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;

import javax.imageio.ImageIO;


public class PlayerMP extends Player {
	
	private static final long serialVersionUID = 1L;
    public InetAddress ipAddress;
    public int port;

	
	public PlayerMP(BufferedImage[] i, double x, double y, long delay,GamePanel p) {
		super(i, x, y, delay, p);
    }
}
