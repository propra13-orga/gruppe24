package Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


public class Map {
	
	int[][] leveldata;
	int lvl = 1;
	
	
	@SuppressWarnings("resource")
	public void read() {
		try {
			String sTemp;
			String rest = null;
			int i, j;
			leveldata = new int[15*16 / 16][15*16/16];
			switch (lvl) {
			case 1:
				rest = "lvl1.level";
				break;
			case 2:
				rest = "lvl2.level";
				break;
			case 3:
				rest = "lvl3.level";
				break;
			case 4:
				rest = "lvl4.level";
				break;
			case 5:
				rest = "lvl5.level";
				break;
			case 6:
				rest = "lvl6.level";
				break;
			}
			BufferedReader oReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File("res/lvl/" + rest)))); // Zeile
																		// für
			// Zeile
			// einlesen

			i = 0;

			while ((sTemp = oReader.readLine()) != null) {

				// Zeile in Einzelteile zerlegen (wir trennen durch ;

				java.util.StringTokenizer stWerte = new StringTokenizer(sTemp,
						";");

				j = 0;

				// Nun eintragen in den Array. Es wird nicht überprüft, ob
				// die Grenzen überschritten werden!

				while (stWerte.hasMoreTokens()) {

					leveldata[i][j] = Integer.parseInt(stWerte.nextToken());
					j++;
				}
				i++;
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace(); // Fehler ausdrucken

		} catch (IOException e) {

			e.printStackTrace(); // Fehler ausdrucken

		}
		lvl ++;
	}
}
