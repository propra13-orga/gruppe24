package Server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import Client.*;



public class GUI extends JPanel implements Runnable {
	
	private static final long serialVersionUID = 1L;
	private JButton b0;
	private JButton b1;
	
	private GameServer socketServer;
	private GamePanel parent;
	
	JFrame console;
	
	
	public static boolean running = false;
	
	public GUI(int w, int h) {
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.BLACK);

		console = new JFrame("Output");
		console.setLocation(100,100);
		console.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		b0 = new JButton("Start");
		b1 = new JButton("Stop");
		b0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				b0.setVisible(true);
				b1.setVisible(true);
				b0.setEnabled(false);
				console.pack();
				console.requestFocus();
				startServer();

			}
		});
		b1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				stopServer();
				
			}
		});

		console.add(b0, BorderLayout.EAST);
		console.add(b1, BorderLayout.WEST);
		console.add(this);
		console.pack();
		console.setResizable(false);
		console.setVisible(true);
	}

	private synchronized void startServer() {
		//Thread th = new Thread(this);
		//th.start();
		new Thread(this).start();
		socketServer = new GameServer(parent);
		socketServer.start();
		running = true;
		
	}
	
	private synchronized void stopServer(){
		socketServer.interrupt();
		running = false;	
		console.dispose();
		parent.stopGame();

	}

	@Override
	public void run() {
		
	}

}
