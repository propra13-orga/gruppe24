package Net.packets;


@SuppressWarnings("serial")
public class Packet04Enemy extends Packet {

	private int id;
	private double x,y;
    
    public Packet04Enemy(double x, double y, int type) {
        super(04);
        this.x = x;
        this.y = y;
        this.id = type;
    }

    public double getX() {
        return x;
    }
    
    public double getY(){
    	return y;
    }
    
    public int getID(){
    	return id;
    }

}