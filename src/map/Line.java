package map;

import java.util.ArrayList;
import java.util.List;

public class Line 
{
	private String name;
	private Stop start, end; // možná stačí využít seznam - vzít první a poslední prvek
	private List<Street> streets;
	private List<Vehicle> vehicles;
	
	public Line(List<Street> streets, List<Vehicle> vehicles)
	{
		this.streets = streets;
		this.vehicles = vehicles;
		
		List<Stop> streetStops = this.streets.get(0).getStops();
		this.start = streetStops.get(0);
		this.end = streetStops.get(streetStops.size() - 1);
	}
	
	public void setName(String name)
	{
		this.name = name; 
	}
	
	public void addStreet(Street street)
	{
		this.streets.add(street);
	}
	
	public void addVehicle(Vehicle vehicle)
	{
		this.vehicles.add(vehicle);
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public Stop getStart()
	{
		return this.start;
	}
	
	public Stop getEnd()
	{
		return this.end;
	}
	
	public List<Street> getStreets()
	{
		return this.streets;
	}
	
	public List<Vehicle> getVehicles()
	{
		return this.vehicles;
	}
}
