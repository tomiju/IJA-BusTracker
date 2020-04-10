package map;

import java.util.List;

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
	private List<Stop> stops;
	private List<Street> streets;
	private List<BusLine> lines;
	private List<Vehicle> vehicles;
	
	public InputData(List<Stop> stops, List<Street> streets, List<Vehicle> vehicles, List<BusLine> busLines) 
	{
		this.streets = streets;
		this.stops = stops;
		this.vehicles = vehicles;
		this.lines = busLines;
	}
	
	public InputData() {}
	
	/**
     * Ziska seznam ulic.
     * @return List seznam ulic
     */
	public List<Street> getStreets()
	{
		return this.streets;
	}
	
	/**
     * Ziska seznam zastavek.
     * @return List seznam zastavek
     */
	public List<Stop> getStops()
	{
		return this.stops;
	}
	
	/**
     * Ziska seznam vozidel.
     * @return List seznam vozidel
     */
	public List<Vehicle> getVehicles()
	{
		return this.vehicles;
	}
	
	/**
     * Ziska seznam linek.
     * @return List seznam linek
     */
	public List<BusLine> getLines()
	{
		return this.lines;
	}
}
