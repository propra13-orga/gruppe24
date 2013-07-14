package Net.packets;


/**************************************************************************************
 * Packet zur übermittelung der Coordinaten der Gegner auf dem Server an die Clienten *
 **************************************************************************************/
@SuppressWarnings("serial")
public class Packet05EnemyMove extends Packet {

	int id;
	double x,y;
	
	public Packet05EnemyMove(double x, double y, int id) {
        super(05);
        this.x = x;
        this.y = y;
        this.id = id;
    }

	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public int getID(){
		return id;
	}
	
	
}
