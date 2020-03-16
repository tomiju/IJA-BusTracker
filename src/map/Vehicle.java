package map;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import drawable.VehicleView;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Vehicle 
{
	private String id;
	private BusLine line;
	private Timetable timetable;
	private Coordinate currentPosition;
	private Street currentStreet;
	private int speed;
	
	private VehicleView vehicleView;
	
	// aktuální ulice se dá dopočítat - aktuální koordináty -> v lince jsou všechny ulice po trase - spočítat na které přímce leží bod daný aktuálními koordináty
	
	public Vehicle(Timetable timetable, BusLine line, String id)
	{
		this.timetable = timetable;
		this.line = line;
		this.speed = 0;
		this.id = id;
		
		this.setCurrentStreet(currentStreet);
		this.setCurrentPosition(line.getStart().getCoordinate());
		
		//line.addVehicle(this);
		
		this.addVehicleToLine();
	}
	
	public Vehicle() {}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public void setLine(BusLine line)
	{
		this.line = line;
	}
	
	public Timetable getTimetable()
	{
		return this.timetable;
	}
	
	public BusLine getLine()
	{
		return this.line;
	}
	
	public String getId()
	{
		return this.id;
	}
	
	@JsonIgnore
	public Coordinate getCurrentPosition()
	{
		return this.currentPosition;
	}
	
	@JsonIgnore
	public Street getCurrentStreet()
	{
		return this.currentStreet;
	}
	
	public void addVehicleToLine()
	{
		this.line.addVehicle(this);
	}
	
	@JsonIgnore
	public VehicleView getVehicle()
	{
		return this.vehicleView;
	}
	
	@JsonIgnore
	public void setVehicle(VehicleView vehicle)
	{
		this.vehicleView = vehicle;
	}
	
	@JsonIgnore
	public void setCurrentStreet(Street street)
	{
		this.currentStreet= street;
	}
	
	@JsonIgnore
	public void setCurrentPosition(Coordinate position)
	{
		this.currentPosition = position;
	}
}
