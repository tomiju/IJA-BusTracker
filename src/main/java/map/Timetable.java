/**
 *
 * Trida reprezentujici jizdni rad.
 * Jizdni rad se sklada z jednotlivych zapisu.
 * @author Tomas Julina (xjulin08)
 * @author Tomas Kantor (xkanto14)
 *
 */

package map;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Timetable
{
	private ArrayList<TimetableEntry> entries; // ArrayList se zastavkami a casy, kdy na nich jsem

	@JsonIgnore
	private int index = 0;

	@JsonIgnore
	private TimetableEntry next;

	public Timetable(ArrayList<TimetableEntry> entries)
	{
		this.entries = entries;
	}

	public Timetable() {}

	/**
	 * Vrati seznam zapisu (obsah jizdniho radu).
	 * @return ArrayList seznam zapisu
	 */
	public ArrayList<TimetableEntry> getEntries()
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

		if (this.index > entries.size() - 1)
		{
			this.index = 0;
		}

		return (this.next = entries.get(this.index));
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

		return (this.next = entries.get(this.index));
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
