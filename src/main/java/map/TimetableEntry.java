package map;

/**
 * 
 * Trida reprezentujici obsah jizdniho radu.
 * Jizdni rad se sklada z nazvu zastavky a casoveho udaje prijezdu vozidla na zastavku.
 * @author Tomas Julina (xjulin08)
 * @author Tomas Kantor (xkanto14)
 *
 */
public class TimetableEntry 
{
	private Stop stop; // na ktere zastavce jsem
	private String time;
	
	public TimetableEntry(Stop stop, String time)
	{
		this.stop = stop;
		this.time = time;
	}
	
	public TimetableEntry() {}
	
	/**
	 * Vrati zastavku.
	 * @return Stop zastavka
	 */
	public Stop getStop()
	{
		return this.stop;
	}
	
	/**
	 * Vrati cas prijezdu na zastavku.
	 * @return String cas prijezdu
	 */
	public String getTime()
	{
		return this.time;
	}
}
