package map;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * Trida reprezentujici jizdni rad.
 * Jizdni rad se sklada z jednotlivych zapisu.
 * @author Tomas Julina (xjulin08)
 * @author Tomas Kantor (xkanto14)
 *
 */
public class Timetable 
{
	private List<TimetableEntry> entries; // list se zastavkami a casy, kdy na nich jsem
	
	@JsonIgnore
	private int index = 0;
	
	@JsonIgnore
	private TimetableEntry next;
	
	public Timetable(List<TimetableEntry> entries)
	{
		this.entries = entries;
	}
	
	public Timetable() {}
	
	/**
	 * Vrati seznam zapisu (obsah jizdniho radu).
	 * @return List seznam zapisu
	 */
	public List<TimetableEntry> getEntries()
	{
		return this.entries;
	}
	
	/**
	 * Vrati zaznam nasledujici zastavky.
	 * @return TimetableEntry zaznam nasledujici zastavky
	 */
	@JsonIgnore
	public TimetableEntry nextStop()
	{
		this.index++;

		if (this.index >= entries.size()) 
		{
			this.index = 0;
		}

		return (next = entries.get(this.index));
	}

	/**
	 * Vrati zaznam nasledujici zastavky.
	 * @return TimetableEntry zaznam nasledujici zastavky
	 */
	@JsonIgnore
	public TimetableEntry previousStop()
	{
		this.index--;

		if (this.index < 0) 
		{
			this.index = entries.size() - 1;
		}

		return (next = entries.get(this.index));
	}
	
	/**
	 * Pomocna funkce pro restart simulace
	 */
	public void resetIndex()
	{
		this.index = 0;
		this.next = null;
	}
}
