package map;

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
	
	public Stop getStop()
	{
		return this.stop;
	}
	
	public String getTime()
	{
		return this.time;
	}
}
