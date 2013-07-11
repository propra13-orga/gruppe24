package Net.packets;


@SuppressWarnings("serial")
public class Packet03Map extends Packet {
		int levelid;
		int[][] leveldata;

	    public Packet03Map(int[][] lvl) {
	        super(03);
	        this.leveldata = lvl;
	    }
	    public Packet03Map() {
	        super(03);
	    }
	    
	    public void setLevel(int[][] lvl){
	    	this.leveldata = lvl;
	    }
	    
	    public int[][] getLevel(){
	    	return leveldata;
	    }
}
