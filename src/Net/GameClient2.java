package Net;

import java.io.*;
import java.net.*;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javax.swing.JOptionPane;
 
public class GameClient2 {
  /*
    Im 'worker' des Hauptprogramms wird wie folgt verfahren:
    o Bilde Instanz von 'FutureTask', gib ihr als Parameter eine Instanz von
      'ClientHandler' mit, die das Interface 'Callable' (�hnlich 'Runnable')
      implementiert.
    o �bergib die 'FutureTask' an einen neuen Thread und starte diesen.
      Im Thread wird nun die 'call'-Methode aus dem Interface 'Callable' des
      ClientHandlers abgearbeitet.
    o Dabei wird die komplette Kommunikation mit dem Server durchgef�hrt.
      Die 'call'-Methode gibt nun das Ergebnis vom Server an die 'FutureTask'
      zur�ck, wo es im Hauptprogramm zur Verf�gung steht. Hier kann beliebig
      oft und an beliebigen Stellen abgefragt werden, ob das Ergebnis bereits
      vorliegt.
      
      ObjectOutPutStream
  */
  String werte;
  
  public GameClient2(String werte) {
    this.werte = werte;
  }
  void worker() throws Exception {
    System.out.println("worker:" + Thread.currentThread());
    //Klasse die 'Callable' implementiert
    ClientHandler ch = new ClientHandler(werte);
    boolean weiter = false;
    do {  //2 Durchl�ufe
      int j = 0;
      //call-Methode 'ch' von ClientHandler wird mit 'FutureTask' asynchron
      //abgearbeitet, das Ergebnis kann dann von der 'FutureTask' abgeholt
      //werden.
      FutureTask<String> ft = new FutureTask<String>(ch);
      Thread tft = new Thread(ft);
      tft.start();
 
      //pr�fe ob der Thread seine Arbeit getan hat
      while (!ft.isDone()) {
        j++;  //z�hle die Thread-Wechsel
        Thread.yield();  //andere Threads (AndererThread) k�nnen drankommen
      }
      System.out.println("not isDone:" + j);
      System.out.println(ft.get());  //Ergebnis ausgeben
      if (werte.compareTo("Exit") == 0)
        break;
      weiter = !weiter;
      if (weiter) {
        //2. Aufruf f�r Client-Anforderung, letzten Wert modifizieren
        ch.setWerte(werte.substring(0,werte.length()-4) + "1813");
      }
    } while (weiter);
  }
}
 
//Enth�lt die call-Methode f�r die FutureTask (entspricht run eines Threads)
class ClientHandler implements Callable<String> {
  String ip = JOptionPane.showInputDialog("IP Address: ");  //localhost
  int port = 1337;
  String werte;
 
  public ClientHandler(String werte) {
    this.werte = werte;
  }
  void setWerte(String s) {
    werte = s;
  }
  public String call() throws Exception {  //run the service
    System.out.println("ClientHandler:" + Thread.currentThread());
    //verl�ngere k�nstlich die Bearbeitung der Anforderung, um das Wechselspiel
    //der Threads zu verdeutlichen
    Thread.sleep(2000);
    return RequestServer(werte);
  }
 
  //Socket �ffnen, Anforderung senden, Ergebnis empfangen, Socket schliessen
  String RequestServer(String par) throws IOException {
    String empfangeneNachricht;
    String zuSendendeNachricht;
 
    Socket socket = new Socket(ip,port);  //verbindet sich mit Server
    zuSendendeNachricht = par;
    //Anforderung senden
    schreibeNachricht(socket, zuSendendeNachricht);
    //Ergebnis empfangen
    empfangeneNachricht = leseNachricht(socket);
    socket.close();
    return empfangeneNachricht;
  }
  void schreibeNachricht(Socket socket, String nachricht) throws IOException {
    PrintWriter printWriter =  new PrintWriter( new OutputStreamWriter(socket.getOutputStream()));
    printWriter.print(nachricht);
    printWriter.flush();
  }
  String leseNachricht(Socket socket) throws IOException {
    BufferedReader bufferedReader =
      new BufferedReader(
        new InputStreamReader(
          socket.getInputStream()));
    char[] buffer = new char[100];
    //blockiert bis Nachricht empfangen
    int anzahlZeichen = bufferedReader.read(buffer, 0, 100);
    String nachricht = new String(buffer, 0, anzahlZeichen);
    return nachricht;
  }
}
 
class AndererThread implements Runnable {
  public void run() {
    System.out.println("  AndererThread:" + Thread.currentThread());
    int n = 0;
    int w = 25000000;
    //hinreichend viel CPU-Zeit verbrauchen
    for (int i = 1; i <= 10; i++)
      for (int j = 1; j <= w; j++) {
        if (j % w == 0)
          System.out.println("  n=" + (++n));
      }
  }
}