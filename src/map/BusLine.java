package map;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class BusLine 
{
	private String id;
	private Stop start, end; // možná stačí využít seznam - vzít první a poslední prvek
	private List<Street> streets;
	private List<Vehicle> vehicles;
	
	public BusLine(String id, List<Street> streets)
	{
		this.streets = streets;
		this.id = id;
		this.vehicles = new ArrayList<>();
		
		List<Stop> streetStops = this.streets.get(0).getStops();
		this.start = streetStops.get(0);
		this.end = streetStops.get(streetStops.size() - 1);
	}
	
	public BusLine() {}
	
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
	
	@JsonIgnore
	public void setLineFocus(Pane map, List<Street> streets)
	{
		for (Street street : streets) 
		{
			Line line = street.getStreetView().getLine();
			line.setStroke(Color.BLUE);
			map.getChildren().remove(line);
			map.getChildren().add(line);
		}
	}
	
	@JsonIgnore
	public void unsetLineFocus(Pane map, List<Street> streets)
	{
		for (Street street : streets) 
		{
			Line line = street.getStreetView().getLine();
			line.setStroke(Color.BLACK);
			map.getChildren().remove(line);
			map.getChildren().add(line);
		}
	}
}
