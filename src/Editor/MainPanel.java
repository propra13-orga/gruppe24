package Editor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class MainPanel extends JFrame {

	private static final long serialVersionUID = 1L;

	JButton LvlErstellen, LvlLaden, DungeonBauen;
	JButton Objekte, LvlSpeichern, Reset;
	
	File imageFileboden = new File("res/pics/floor.gif");
	File imageFilewand = new File("res/pics/wall.gif");
	File imageFilewater = new File("res/pics/water2.png");
	File imageFileenemy = new File("res/pics/Enemy.png");
	File imageFilestart = new File("res/pics/pstart.png");
	File imageFiledoor = new File("res/pics/door2.png");
	File imageFileexit = new File("res/pics/exit.png");
	File imageFilecheck = new File("res/pics/checkpoint2.png");
	File imageFilespawn = new File("res/pics/spawnpunkt.gif");
	File imageFileback = new File("res/pics/back.gif");
	
	String neu = "res/lvl/leer.level";
		
	int[][] Leveldata;
	
	public MainPanel(){
		setSize(400, 300);
		setLocation(200, 200);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		final JPanel Hauptpanel = new JPanel();
		Hauptpanel.setLayout(new GridLayout(3,1));
		LvlErstellen = new JButton("Level erstellen");
		LvlLaden = new JButton("Level laden");
		DungeonBauen = new JButton("Dungeon erstellen");
		Hauptpanel.add(LvlErstellen);
		Hauptpanel.add(LvlLaden);
		Hauptpanel.add(DungeonBauen);
		add(Hauptpanel);
		LvlErstellen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EditorErstellen(neu);
				Hauptpanel.setVisible(false);
				pack();
			}
		});
		
		LvlLaden.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String levelLadenPath = "res/lvl/" + laden();
				EditorErstellen(levelLadenPath);
				Hauptpanel.setVisible(false);
				pack();
			}
		});	
	}
	
	
	public void EditorErstellen(String pfad){
		JPanel	Editor = new JPanel();
		Editor.setLayout(new BorderLayout());
		Editor.setBackground(Color.black);
		Editor.setVisible(true);
		add(Editor);
		JPanel EdiP = new JPanel();
		JPanel Oben = new JPanel();
		EdiP.setLayout(new GridLayout(1,2));
		EdiP.setLayout(new GridLayout(1,2));
		LvlSpeichern = new JButton("Level speichern");
		LvlLaden = new JButton("Level laden");
		Reset = new JButton("Neu");
		Objekte = new JButton("Level Strukturen");
		Oben.add(Objekte);
		Oben.add(Reset);
		EdiP.add(LvlLaden);
		EdiP.add(LvlSpeichern);
		add(BorderLayout.SOUTH,EdiP);
		add(BorderLayout.NORTH,Oben);
		
		try {
		    BufferedImage boden = ImageIO.read(imageFileboden);
		    BufferedImage wall = ImageIO.read(imageFilewand);
		    BufferedImage enemy = ImageIO.read(imageFileenemy);
		    BufferedImage water = ImageIO.read(imageFilewater);
		    BufferedImage check = ImageIO.read(imageFilecheck);
		    BufferedImage start = ImageIO.read(imageFilestart);
		    BufferedImage door = ImageIO.read(imageFiledoor);
		    BufferedImage exit = ImageIO.read(imageFileexit);
		    BufferedImage spawn = ImageIO.read(imageFilespawn);
		    BufferedImage back = ImageIO.read(imageFileback);
		    JPanel centerPanel;
		    centerPanel = new JPanel();
			centerPanel.setLayout(new GridLayout(15, 15));
			centerPanel.setPreferredSize( new Dimension(240, 240) );
			centerPanel.setVisible(true);
			loadLevel(pfad);
			int objekt;
			final ImageIcon[]icon = new ImageIcon[10] ;
			icon[0]  = new ImageIcon(wall);
			icon[1] = new ImageIcon(boden); 
			icon[2]  = new ImageIcon(start); 
		 	icon[3]  = new ImageIcon(enemy); 
		 	icon[4]  = new ImageIcon(exit); 
		 	icon[5]  = new ImageIcon(water); 
		 	icon[6]  = new ImageIcon(door);
		 	icon[7]  = new ImageIcon(check); 
		 	icon[8]  = new ImageIcon(spawn); 
		 	icon[9]  = new ImageIcon(back); 
			final JButton [][] button = new JButton [15][15];
			 for (int i = 0; i < 15; i++) { 
				 for (int j = 0; j < 15; j++) {
					 final int i_final = i;
					 final int j_final = j;
					 objekt = Leveldata[i][j];
					 button[i][j] = new JButton();
					 
					 System.out.println(objekt);
					 switch(objekt){
					 case 1:						  
				            button[i][j].setIcon(icon[0]); 
				            centerPanel.add(button[i][j]);
				            break;
					 case 0:											 	
						 	button[i][j].setIcon(icon[1]); 
						 	centerPanel.add(button[i][j]);
						 	break;
					 case 2:
						 button[i][j].setIcon(icon[8]); 
						 	centerPanel.add(button[i][j]);
						 	break;
					 case 8:						 	
				            button[i][j].setIcon(icon[2]); 
				            centerPanel.add(button[i][j]);
				            break;
					 case 3:						 
				            button[i][j].setIcon(icon[3]); 
				            centerPanel.add(button[i][j]);
				            break;
					 case 42:							
				            button[i][j].setIcon(icon[4]); 
				            centerPanel.add(button[i][j]);
				            break;
					 case 4:						 	
				            button[i][j].setIcon(icon[5]); 
				            centerPanel.add(button[i][j]);
				            break;
					 case 9:						 	 
				            button[i][j].setIcon(icon[6]); 
				            centerPanel.add(button[i][j]);
				            break;
					 case 13:				
						 	button[i][j].setIcon(icon[7]); 
				            centerPanel.add(button[i][j]);
				            break;
					 case 7:				
						 	button[i][j].setIcon(icon[9]); 
				            centerPanel.add(button[i][j]);
				            break;
					 }
					 button[i][j].addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							System.out.print(i_final + " ");
							System.out.print(j_final+ " ");
							System.out.println(Leveldata[i_final][j_final]);
							if((Leveldata[i_final][j_final])==0){
								button[i_final][j_final].setIcon(icon[0]);
								Leveldata[i_final][j_final] =  1;
							} else if ((Leveldata[i_final][j_final])==1) {
								button[i_final][j_final].setIcon(icon[3]);
								Leveldata[i_final][j_final] =  3;
								}
							else if ((Leveldata[i_final][j_final])==3) {
								button[i_final][j_final].setIcon(icon[5]);
								Leveldata[i_final][j_final] =  4;
								}
							else if ((Leveldata[i_final][j_final])==4) {
								button[i_final][j_final].setIcon(icon[2]);
								Leveldata[i_final][j_final] =  8;
								}
							else if ((Leveldata[i_final][j_final])==8) {
								button[i_final][j_final].setIcon(icon[6]);
								Leveldata[i_final][j_final] =  9;
								}
							else if ((Leveldata[i_final][j_final])==9) {
								button[i_final][j_final].setIcon(icon[4]);
								Leveldata[i_final][j_final] =  42;
								}
							else if ((Leveldata[i_final][j_final])==42) {
								button[i_final][j_final].setIcon(icon[7]);
								Leveldata[i_final][j_final] =  13;
								}
							else if ((Leveldata[i_final][j_final])==13) {
								button[i_final][j_final].setIcon(icon[8]);
								Leveldata[i_final][j_final] =  2;
								}
							else if ((Leveldata[i_final][j_final])==2) {
								button[i_final][j_final].setIcon(icon[9]);
								Leveldata[i_final][j_final] =  7;
								}
							else{
								button[i_final][j_final].setIcon(icon[1]);
								Leveldata[i_final][j_final] =  0;
							}
							}
					});
				}
			 }				
			add(BorderLayout.CENTER, centerPanel);
		}
		catch(IOException ioex) {
		    //abort
		    System.exit(1);	}
		LvlLaden.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String levelLadenPath = "res/lvl/" + laden();
				EditorErstellen(levelLadenPath);
				pack();
			}
		});	
		LvlSpeichern.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				speichern();				
			}
		});
	}
	
	public void loadLevel(String path){
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
			Leveldata = new int [15][15];
			int i = 0;
			String sTemp;
			try {
				while ((sTemp = input.readLine()) != null) {

					// Zeile in Einzelteile zerlegen (wir trennen durch ;
					java.util.StringTokenizer stWerte = new StringTokenizer(sTemp,";");
					int j = 0;

					// Nun eintragen in den Array. Es wird nicht überprüft, ob
					// die Grenzen überschritten werden!
					while (stWerte.hasMoreTokens()) {
						Leveldata[i][j] = Integer.parseInt(stWerte.nextToken());
						j++;
					}
					i++;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String laden(){
		JFileChooser ladenWahl = new JFileChooser("res/lvl/");
		ladenWahl.showOpenDialog(null);
		System.out.println(ladenWahl.getSelectedFile().getName());
		return ladenWahl.getSelectedFile().getName();
		}
	
	@SuppressWarnings("resource")
	public void speichern(){
		JFileChooser speicherWahl = new JFileChooser("res/lvl/");
		speicherWahl.showSaveDialog(null);
		File file = new File("C:/Users/markus/workspace/GameEditor/res/lvl/" + speicherWahl.getSelectedFile().getName() );
		try {
			file.createNewFile();
			FileWriter inputStream = new FileWriter(file);
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 15; j++) {
					if (Leveldata[i][j]==0) {
						inputStream.write("0;");
					}else if(Leveldata[i][j]==1){
						inputStream.write("1;");
					}else if(Leveldata[i][j]==3){
						inputStream.write("3;");
					}else if(Leveldata[i][j]==4){
						inputStream.write("4;");
					}else if(Leveldata[i][j]==42){
						inputStream.write("42;");
					}else if(Leveldata[i][j]==8){
						inputStream.write("8;");
					}else if(Leveldata[i][j]==9){
						inputStream.write("9;");
					}else if(Leveldata[i][j]==2){
						inputStream.write("2;");
					}else if(Leveldata[i][j]==7){
						inputStream.write("7;");
					}else
						inputStream.write("13;");
				}
				inputStream.write(System.getProperty("line.separator"));
			}
			inputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new MainPanel();
	}
}
