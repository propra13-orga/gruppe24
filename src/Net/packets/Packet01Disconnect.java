package Net.packets;

@SuppressWarnings("serial")
public class Packet01Disconnect extends Packet {

    private String username;


    public Packet01Disconnect(String username) {
        super(01);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}