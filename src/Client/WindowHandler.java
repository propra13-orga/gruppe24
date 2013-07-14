package Client;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import Net.Client;
import Net.packets.Packet01Disconnect;

/***********************************************************************************************
 *WindowHandler.class wird gebraucht um ein sauberes schlieﬂen im Multiplayer zu gew‰hrleisten * 
 ***********************************************************************************************/
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

    /*******************************************************************
     * Sendet ein Disconnect Packet an den Server mit dem Spielernamen *
     *******************************************************************/
    @Override
    public void windowClosing(WindowEvent event) {
        Packet01Disconnect packet = new Packet01Disconnect(game.player1.getUsername());
        try {
			game.c.getOutput().writeObject(packet);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
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