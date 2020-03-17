package map;

/**
 * 
 * Třída reprezentující obsah jízdního řádu.
 * Jízdní řád se skládá z názvu zastávky a časového údaje příjezdu vozidla na zastávku.
 * @author Tomáš Julina (xjulin08)
 * @author Tomáš Kantor (xkanto14)
 *
 */
public class TimetableEntry 
{
	private Stop stop; // na které zastávce jsem
	private String time; // asi bude potřeba data type s TIME?
	
	public TimetableEntry(Stop stop, String time)
	{
		this.stop = stop;
		this.time = time; // zatím
	}
	
	public TimetableEntry() {}
	
	/**
	 * Vrátí zastávku.
	 * @return Stop zastávka
	 */
	public Stop getStop()
	{
		return this.stop;
	}
	
	/**
	 * Vrátí čas.
	 * @return String čas
	 */
	public String getTime()
	{
		return this.time;
	}
}
