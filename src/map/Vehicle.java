package map;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Vehicle 
{
	private String id;
	private BusLine line;
	private Timetable timetable;
	private Coordinate currentPosition;
	private Street currentStreet;
	private int speed;
	
	// aktuální ulice se dá dopočítat - aktuální koordináty -> v lince jsou všechny ulice po trase - spočítat na které přímce leží bod daný aktuálními koordináty
	
	public Vehicle(Timetable timetable, BusLine line, String id)
	{
		this.timetable = timetable;
		this.line = line;
		this.speed = 0;
		this.id = id;
		this.currentPosition = line.getStart().getCoordinate();
		this.currentStreet = line.getStart().getStreet(); // počáteční ulice
		
		this.addVehicleToLine();
	}
	
	public Vehicle() {}
	
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
	
	public Coordinate currentPosition()
	{
		return this.currentPosition;
	}
	
	public Street getCurrentStreet()
	{
		return this.currentStreet;
	}
	
	public void addVehicleToLine()
	{
		this.line.addVehicle(this);
	}
}
