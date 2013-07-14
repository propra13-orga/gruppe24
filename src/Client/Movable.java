package Client;

/**********************************************************************************************
 * Das Interface enthält 2 Methoden zum Bewegen unserer Objekte: Die Methode move(long delta) *
 * für die eigentliche Bewegung und die Methode doLogic(long delta) für Logikoperationen,     * 
 * wie z. B. Kollisionserkennung, etc														  *
 **********************************************************************************************/
public interface Movable {
	
	public void doLogic(long delta);
	
	public void move(long delta);
}
