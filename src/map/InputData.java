package map;

import java.util.List;

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
	
	public List<Street> getStreets()
	{
		return this.streets;
	}
	
	public List<Stop> getStops()
	{
		return this.stops;
	}
	
	public List<Vehicle> getVehicles()
	{
		return this.vehicles;
	}
	
	public List<BusLine> getLines()
	{
		return this.lines;
	}
}
