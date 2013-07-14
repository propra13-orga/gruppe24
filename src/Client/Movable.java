package Client;

/**********************************************************************************************
 * Das Interface enth�lt 2 Methoden zum Bewegen unserer Objekte: Die Methode move(long delta) *
 * f�r die eigentliche Bewegung und die Methode doLogic(long delta) f�r Logikoperationen,     * 
 * wie z. B. Kollisionserkennung, etc														  *
 **********************************************************************************************/
public interface Movable {
	
	public void doLogic(long delta);
	
	public void move(long delta);
}
