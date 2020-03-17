package map;

import java.util.List;

/**
 * 
 * Třída reprezentující jízdní řád.
 * Jízdní řád se skládá z jednotlivých zápisů.
 * @author Tomáš Julina (xjulin08)
 * @author Tomáš Kantor (xkanto14)
 *
 */
public class Timetable 
{
	private List<TimetableEntry> entries; // list se zastávkami a časy, kdy na nich jsem
	
	public Timetable(List<TimetableEntry> entries)
	{
		this.entries = entries;
	}
	
	public Timetable() {}
	
	/**
	 * Vrátí seznam zápisů (obsah jízdního řádu)
	 * @return List<TimetableEntry> seznam zápisů
	 */
	public List<TimetableEntry> getEntries()
	{
		return this.entries;
	}
}
