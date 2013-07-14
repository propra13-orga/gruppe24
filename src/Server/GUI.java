package Server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import Client.*;
import Net.Server;



public class GUI extends JPanel implements Runnable {
	
	private static final long serialVersionUID = 1L;
	private JButton b0;
	private JButton b1;
	
	@SuppressWarnings("unused")
	private GamePanel parent;
	
	JFrame console;
	Server s;
	
	public static boolean running = false;
	
	public GUI(int w, int h, GamePanel parent) {
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.BLACK);
		this.parent = parent;

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
				try {
					startServer();
				} catch (IOException e) {
					e.printStackTrace();
				}

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

	private synchronized void startServer() throws IOException {
		new Thread(this).start();
		s = new Server();
		running = true;
		
	}
	
	private synchronized void stopServer(){
		running = false;	
		console.dispose();
	}

	@Override
	public void run() {
		
	}

}
