package Net.packets;


/*******************************************************************
 * Packet zum übermitteln der Level daten vom Server an die Player *
 *******************************************************************/
@SuppressWarnings("serial")
public class Packet03Map extends Packet {
		int levelid;
		int[][] leveldata;
		String txt;
		boolean init;

	    public Packet03Map(int[][] lvl, boolean init) {
	        super(03);
	        this.leveldata = lvl;
	        this.init = init;
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
	    
	    public boolean getBool(){
	    	return init;
	    }
	    
	    public String getString(){
	    	return txt;
	    }
}
