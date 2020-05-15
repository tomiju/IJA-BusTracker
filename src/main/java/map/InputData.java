package map;

import java.util.ArrayList;

/**
 *
 * Vstupni data ze souboru.
 * Obsahuje seznam zastavek, ulic, linek a vozidel.
 * @author Tomas Julina (xjulin08)
 * @author Tomas Kantor (xkanto14)
 *
 */
public class InputData implements Cloneable // pomocna trida pro zpracovani input dat z YAML souboru
{
	private ArrayList<Stop> stops;
	private ArrayList<Street> streets;
	private ArrayList<BusLine> lines;
	private ArrayList<Vehicle> vehicles;

	public InputData(ArrayList<Stop> stops, ArrayList<Street> streets, ArrayList<Vehicle> vehicles, ArrayList<BusLine> busLines)
	{
		this.streets = streets;
		this.stops = stops;
		this.vehicles = vehicles;
		this.lines = busLines;
	}

	public InputData() {}

	/**
     * Ziska seznam ulic.
     * @return ArrayList seznam ulic
     */
	public ArrayList<Street> getStreets()
	{
		return this.streets;
	}

	/**
     * Ziska seznam zastavek.
     * @return ArrayList seznam zastavek
     */
	public ArrayList<Stop> getStops()
	{
		return this.stops;
	}

	/**
     * Ziska seznam vozidel.
     * @return ArrayList seznam vozidel
     */
	public ArrayList<Vehicle> getVehicles()
	{
		return this.vehicles;
	}

	/**
     * Ziska seznam linek.
     * @return ArrayList seznam linek
     */
	public ArrayList<BusLine> getLines()
	{
		return this.lines;
	}
}
