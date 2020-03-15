package map;

import java.util.List;

public class Timetable 
{
	private List<TimetableEntry> entries; // list se zastávkami a časy, kdy na nich jsem
	
	public Timetable(List<TimetableEntry> entries)
	{
		this.entries = entries;
	}
	
	public Timetable() {}
	
	public List<TimetableEntry> getEntries()
	{
		return this.entries;
	}
}
