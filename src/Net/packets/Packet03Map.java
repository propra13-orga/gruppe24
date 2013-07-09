package Net.packets;


@SuppressWarnings("serial")
public class Packet03Map extends Packet {
		int levelid;

	    public Packet03Map(int levelid) {
	        super(03);
	        this.levelid = levelid;
	    }
}
