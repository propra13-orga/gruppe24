package Net.packets;

@SuppressWarnings("serial")
public class Packet02Move extends Packet {

    private String username;
    public double x, y;

    private int movingDir = 1;

    public Packet02Move(String username, double x, double y, int movingDir) {
        super(02);
        this.username = username;
        this.x = x;
        this.y = y;
        this.movingDir = movingDir;
    }

    public String getUsername() {
        return username;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public int getMovingDir() {
        return movingDir;
    }

}