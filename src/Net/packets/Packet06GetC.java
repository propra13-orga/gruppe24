package Net.packets;

@SuppressWarnings("serial")
public class Packet06GetC extends Packet {

	private double x, y;
	private int id;
	String username;
	
	public Packet06GetC(double x, double y, int id, String name) {
		super(06);
		
        this.x = x;
        this.y = y;
        this.id = id;
        this.username = name;
	}
	
	
    public void setX(double wert){
    	x = wert;
    }
    
    public void setY(double wert){
    	y = wert;
    }
    
    public int getId(){
    	return id;
    }
    
    public double getX(){
    	return x;
    }
    
    public double getY(){
    	return y;
    }
    
    public String getUsername(){
    	return username;
    }
}
