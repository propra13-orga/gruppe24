
public class Level {
    static int [][] leveldata; 

    static int lvl = 1;
    static void read() {
        //static int lvl = l;
        java.lang.String sTemp; 
        int i,j; 
        leveldata = new int [15][15]; 
        switch(lvl){
            case 1: try {             // Datei öffnen
            
            java.io.BufferedReader oReader = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(new java.io.File("C:\\Users\\Tobias\\Documents\\NetBeansProjects\\JavaGame\\src\\lvl\\lvl1.txt"))));  // Zeile für Zeile einlesen 

            i=0; 

            while ((sTemp = oReader.readLine()) != null) { 
                
                // Zeile in Einzelteile zerlegen (wir trennen durch ; 

                java.util.StringTokenizer stWerte = new java.util.StringTokenizer(sTemp,";"); 

                j=0; 
                
                // Nun eintragen in den Array. Es wird nicht überprüft, ob die Grenzen überschritten werden! 

                while(stWerte.hasMoreTokens()) { 

                    leveldata[i][j] = java.lang.Integer.parseInt(stWerte.nextToken()); 

                    j++; 

                } 

                i++; 

            } 

        } catch (java.io.FileNotFoundException e) { 

            e.printStackTrace();  //Fehler ausdrucken 

        } catch (java.io.IOException e) { 

            e.printStackTrace();  //Fehler ausdrucken 

        }
                break;
            case 2: try {             // Datei öffnen
            
            java.io.BufferedReader oReader = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(new java.io.File("C:\\Users\\Tobias\\Documents\\NetBeansProjects\\JavaGame\\src\\lvl\\lvl2.txt"))));  // Zeile für Zeile einlesen 

            i=0; 

            while ((sTemp = oReader.readLine()) != null) { 
                
                // Zeile in Einzelteile zerlegen (wir trennen durch ; 

                java.util.StringTokenizer stWerte = new java.util.StringTokenizer(sTemp,";"); 

                j=0; 
                
                // Nun eintragen in den Array. Es wird nicht überprüft, ob die Grenzen überschritten werden! 

                while(stWerte.hasMoreTokens()) { 

                    leveldata[i][j] = java.lang.Integer.parseInt(stWerte.nextToken()); 

                    j++; 

                } 

                i++; 

            } 

        } catch (java.io.FileNotFoundException e) { 

            e.printStackTrace();  //Fehler ausdrucken 

        } catch (java.io.IOException e) { 

            e.printStackTrace();  //Fehler ausdrucken 

        }
                break;
            case 3: try {             // Datei öffnen
            
            java.io.BufferedReader oReader = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(new java.io.File("C:\\Users\\Tobias\\Documents\\NetBeansProjects\\JavaGame\\src\\lvl\\lvl3.txt"))));  // Zeile für Zeile einlesen 

            i=0; 

            while ((sTemp = oReader.readLine()) != null) { 
                
                // Zeile in Einzelteile zerlegen (wir trennen durch ; 

                java.util.StringTokenizer stWerte = new java.util.StringTokenizer(sTemp,";"); 

                j=0; 
                
                // Nun eintragen in den Array. Es wird nicht überprüft, ob die Grenzen überschritten werden! 

                while(stWerte.hasMoreTokens()) { 

                    leveldata[i][j] = java.lang.Integer.parseInt(stWerte.nextToken()); 

                    j++; 

                } 

                i++; 

            } 

        } catch (java.io.FileNotFoundException e) { 

            e.printStackTrace();  //Fehler ausdrucken 

        } catch (java.io.IOException e) { 

            e.printStackTrace();  //Fehler ausdrucken 

        }
                break;
            default: System.out.println("Level existiert nicht");
        }      

    } 
}
