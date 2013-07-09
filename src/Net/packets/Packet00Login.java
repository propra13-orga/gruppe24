package Net.packets;

@SuppressWarnings("serial")
public class Packet00Login extends Packet {

	private String username;
    private double x, y;
    
    public Packet00Login(String username) {
        super(00);
        this.username = username;
    }

    public Packet00Login(String username, double x, double y) {
        super(00);
        this.username = username;
        this.x = x;
        this.y = y;
    }

    public String getUsername() {
        return username;
    }

    public double getX() {
        return x;
    }
    
    public void setX(double wert){
    	x = wert;
    }

    public double getY() {
        return y;
    }
    
    public void setY(double wert){
    	y = wert;
    }

}