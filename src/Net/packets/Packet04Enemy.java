package Net.packets;


@SuppressWarnings("serial")
public class Packet04Enemy extends Packet {

	private int id;
	private int type;
	private double x,y;
	String str, wea;
    
    public Packet04Enemy(double x, double y, int type, int id, String strenth, String weakness) {
        super(04);
        this.x = x;
        this.y = y;
        this.type = type;
        this.id = id;
        this.str = strenth;
        this.wea = weakness;
    }

    public double getX() {
        return x;
    }
    
    public double getY(){
    	return y;
    }
    
    public int getType(){
    	return type;
    }
    
    public int getID(){
    	return id;
    }
    
    public String getStrength(){
    	return str;
    }
    
    public String getWeakness(){
    	return wea;
    }

}