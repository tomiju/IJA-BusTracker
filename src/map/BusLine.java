package map;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import drawable.StreetView;
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
	
		/*Stop start1 = this.streets.get(0).getStops().get(0);
		Stop end1 = this.streets.get(this.streets.size() - 1).getStops().get(this.streets.get(this.streets.size() - 1).getStops().size() - 1);
		this.start = start1;
		this.end = end1; //this.streets.get(this.streets.size() - 1).getStops().get(this.streets.get(this.streets.size() - 1).getStops().size() - 1);*/
	}
	
	public BusLine() {}
	
	public void setId(String name)
	{
		this.id = name; 
	}
	
	public void setStart(Stop start)
	{
		this.start = start;
	}
	
	public void setEnd(Stop stop)
	{
		this.end = stop;
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
	
	@JsonIgnore
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
