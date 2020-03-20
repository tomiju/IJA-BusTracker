package map;

import java.util.List;

/**
 * 
 * Vstupní data ze souboru.
 * Obsahuje seznam zastávek, ulic, linek a vozidel.
 * @author Tomáš Julina (xjulin08)
 * @author Tomáš Kantor (xkanto14)
 *
 */
public class InputData // pomocná třída pro zpracování input dat z YAML souboru
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
     * Získá seznam ulic.
     * @return List<Street> seznam ulic
     */
	public List<Street> getStreets()
	{
		return this.streets;
	}
	
	/**
     * Získá seznam zastávek.
     * @return List<Stop> seznam zastávek
     */
	public List<Stop> getStops()
	{
		return this.stops;
	}
	
	/**
     * Získá seznam vozidle.
     * @return List<Vehicle> seznam vozidel
     */
	public List<Vehicle> getVehicles()
	{
		return this.vehicles;
	}
	
	/**
     * Získá seznam linek.
     * @return List<BusLine> seznam linek
     */
	public List<BusLine> getLines()
	{
		return this.lines;
	}
}
