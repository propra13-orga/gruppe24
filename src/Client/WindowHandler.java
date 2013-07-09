package Client;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import Net.Client;
import Net.packets.Packet00Login;
import Net.packets.Packet01Disconnect;

public class WindowHandler implements WindowListener {

    private final GamePanel game;
    Client c;
    public WindowHandler(GamePanel game) {
        this.game = game;
        this.game.frame.addWindowListener(this);
    }

    @Override
    public void windowActivated(WindowEvent event) {
    }

    @Override
    public void windowClosed(WindowEvent event) {
    }

    @Override
    public void windowClosing(WindowEvent event) {
        Packet01Disconnect packet = new Packet01Disconnect(game.player1.getUsername());
        try {
			game.c.getOutput().writeObject(packet);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        //packet.writeData(this.game.socketClient);
    }

    @Override
    public void windowDeactivated(WindowEvent event) {
    }

    @Override
    public void windowDeiconified(WindowEvent event) {
    }

    @Override
    public void windowIconified(WindowEvent event) {
    }

    @Override
    public void windowOpened(WindowEvent event) {
    }

}