package Net.packets;

@SuppressWarnings("serial")
public class Packet07Finish extends Packet{
	String Username;

	public Packet07Finish(String Username) {
		super(07);
		this.Username = Username;
	}

	public String getUsername(){
		return Username;
	}
}
