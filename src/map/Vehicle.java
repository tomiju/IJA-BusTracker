package map;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import drawable.VehicleView;

/**
 * 
 * Třída reprezentující vozidlo.
 * Skládá se z ID, linky, na které vozidlo jezdí, jízdního řádu, aktuální pozice a aktuální ulice, na které se vozidlo nachází.
 * @author Tomáš Julina (xjulin08)
 * @author Tomáš Kantor (xkanto14)
 *
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Vehicle 
{
	private String id;
	private BusLine line;
	private Timetable timetable;
	private Coordinate currentPosition;
	private Street currentStreet;
	//private int speed;
	
	private VehicleView vehicleView;
	
	// aktuální ulice se dá dopočítat - aktuální koordináty -> v lince jsou všechny ulice po trase - spočítat na které přímce leží bod daný aktuálními koordináty
	
	public Vehicle(Timetable timetable, BusLine line, String id)
	{
		this.timetable = timetable;
		this.line = line;
		//this.speed = 0;
		this.id = id;
		
		this.setCurrentStreet(currentStreet);
		this.setCurrentPosition(line.getStart().getCoordinate());
		
		this.addVehicleToLine();
	}
	
	public Vehicle() {}
	
	/**
     * Nastaví název vozidla.
     * @param id název
     */
	public void setId(String id)
	{
		this.id = id;
	}
	
	/**
     * Nastaví vozidlu linku.
     * @param line linka
     */
	public void setLine(BusLine line)
	{
		this.line = line;
	}
	
	/**
     * Nastaví grafickou reprezentaci vozidla.
     * @param VehicleView vehicle
     */
	@JsonIgnore
	public void setVehicle(VehicleView vehicle)
	{
		this.vehicleView = vehicle;
	}
	
	/**
     * Nastaví aktuální ulici.
     * @param street ulice
     */
	@JsonIgnore
	public void setCurrentStreet(Street street)
	{
		this.currentStreet= street;
	}
	
	/**
     * Nastaví aktuální pozici (souřadnice).
     * @param position souřadnice
     */
	@JsonIgnore
	public void setCurrentPosition(Coordinate position)
	{
		this.currentPosition = position;
	}
	
	/**
     * Získá jízdní řád.
     * @return Timetable jízdní řád
     */
	public Timetable getTimetable()
	{
		return this.timetable;
	}
	
	/**
     * Získá linku.
     * @return BusLine linka
     */
	public BusLine getLine()
	{
		return this.line;
	}
	
	/**
     * Získá název vozidla.
     * @return String název
     */
	public String getId()
	{
		return this.id;
	}
	
	/**
     * Získá aktuální pozici.
     * @return Coordinate aktuální pozice
     */
	@JsonIgnore
	public Coordinate getCurrentPosition()
	{
		return this.currentPosition;
	}
	
	/**
     * Získá aktuální ulici.
     * @return Street aktuální ulice
     */
	@JsonIgnore
	public Street getCurrentStreet()
	{
		return this.currentStreet;
	}
	
	/**
     * Získá objekt reprezentující grafickou reprezentaci vozidla.
     * @return VehicleView grafická reprezentace vozidla
     */
	@JsonIgnore
	public VehicleView getVehicle()
	{
		return this.vehicleView;
	}
	
	/**
     * Přidá aktuální vozidlo v lince do jejího seznamu.
     */
	public void addVehicleToLine()
	{
		this.line.addVehicle(this);
	}
}
