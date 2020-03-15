package map;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Line 
{
	private String id;
	private Stop start, end; // možná stačí využít seznam - vzít první a poslední prvek
	private List<Street> streets;
	private List<Vehicle> vehicles;
	
	public Line(String id, List<Street> streets)
	{
		this.streets = streets;
		this.id = id;
		
		List<Stop> streetStops = this.streets.get(0).getStops();
		this.start = streetStops.get(0);
		this.end = streetStops.get(streetStops.size() - 1);
	}
	
	public Line() {}
	
	public void setId(String name)
	{
		this.id = name; 
	}
	
	public void addStreet(Street street)
	{
		this.streets.add(street);
	}
	
	public void addVehicle(Vehicle vehicle)
	{
		this.vehicles.add(vehicle);
	}
	
	public String getId()
	{
		return this.id;
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
