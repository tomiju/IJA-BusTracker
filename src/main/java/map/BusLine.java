package map;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * 
 * Reprezentace jedne autobusove linky.
 * Linka ma sve ID, ma pocatecni a konecnou zastavku a obsahuje seznam vozidel a ulic, ze kterych se sklada.
 * @author Tomas Julina (xjulin08)
 * @author Tomas Kantor (xkanto14)
 *
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class BusLine 
{
	private String id;
	private Stop start, end;
	private List<Street> streets;
	private List<Vehicle> vehicles;
	
	public BusLine(String id, List<Street> streets)
	{
		this.streets = streets;
		this.id = id;
		this.vehicles = new ArrayList<>();
	}
	
	public BusLine() {
		this.vehicles = new ArrayList<>();
	}
	
	/**
     * Nastavi nazev linky.
     * @param name nazev
     */
	public void setId(String name)
	{
		this.id = name; 
	}
	
	/**
     * Nastavi zacatek linky (pocatecni zastavku).
     * @param start start
     */
	public void setStart(Stop start)
	{
		this.start = start;
	}
	
	/**
     * Nastavi konecnou stanici.
     * @param stop konecna zastavka
     */
	public void setEnd(Stop stop)
	{
		this.end = stop;
	}
	
	/**
     * Prida ulici do linky.
     * @param street ulice
     */
	public void addStreet(Street street)
	{
		this.streets.add(street);
	}
	
	/**
     * Prida vozidlo patrici dane lince.
     * @param vehicle vozidlo
     */
	public void addVehicle(Vehicle vehicle)
	{
		this.vehicles.add(vehicle);
	}
	
	/**
     * Ziska nazev linky.
     * @return String nazev linky
     */
	public String getId()
	{
		return this.id;
	}
	
	/**
     * Ziska pocatecni zastavku
     * @return Stop pocatecni zastavka
     */
	public Stop getStart()
	{
		return this.start;
	}
	
	/**
     * Ziska konecnou zastavku.
     * @return Stop konecna zastavka
     */
	public Stop getEnd()
	{
		return this.end;
	}
	
	/**
     * Ziska seznam ulic.
     * @return List seznam ulic
     */
	public List<Street> getStreets()
	{
		return this.streets;
	}
	
	/**
     * Ziska seznam vozidel.
     * @return List seznam vozidel
     */
	@JsonIgnore
	public List<Vehicle> getVehicles()
	{
		return this.vehicles;
	}
	
	/**
     * Nastavi "focus" (zvyrazni) na celou linku.
     * @param map mapa
     */
	@JsonIgnore
	public void setLineFocus(Pane map)
	{
		for (Street street : this.streets) 
		{
			if (street.isOpen())
			{
				Line line = street.getStreetView().getLine();
				line.setStroke(Color.GREEN);
			}
		}
		
		for (Vehicle vehicle : this.vehicles)
		{
			Circle circle = vehicle.getVehicleView().getCircle();
			circle.setFill(Color.GREEN);
		}
	}
	
	/**
     * Zrusi "focus" lince.
     * @param map mapa
     */
	@JsonIgnore
	public void unsetLineFocus(Pane map)
	{
		for (Street street : this.streets) 
		{
			if (street.isOpen())
			{
				Line line = street.getStreetView().getLine();
				line.setStroke(Color.BLACK);
			}
		}
		
		for (Vehicle vehicle : this.vehicles)
		{
			Circle circle = vehicle.getVehicleView().getCircle();
			circle.setFill(Color.BLUE);
		}
	}
	
}
