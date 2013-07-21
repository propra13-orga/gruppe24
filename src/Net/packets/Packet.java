package Net.packets;

import java.io.Serializable;

/***************************************************
 * Die Klasse sortiert die Packet anhand deren IDs *
 ***************************************************/
@SuppressWarnings("serial")
public abstract class Packet implements Serializable  {

    public static enum PacketTypes {
        INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02), MAP(03), ENEMY(04), EnemyMove(05), GetC(06), Finish(07);

        private int packetId;

        private PacketTypes(int packetId) {
            this.packetId = packetId;
        }

        public int getId() {
            return packetId;
        }
    }

    public byte packetId;

    public Packet(int packetId) {
        this.packetId = (byte) packetId;
    }
}