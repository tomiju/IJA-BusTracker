package map;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * 
 * Reprezentace jedné autobusové linky.
 * Linka má své ID, má počáteční a konečnou zastávku a obsahuje seznam vozidel a ulic, ze kterých se skládá.
 * @author Tomáš Julina (xjulin08)
 * @author Tomáš Kantor (xkanto14)
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
	
	public BusLine() {}
	
	/**
     * Nastaví název linky.
     * @param name název
     */
	public void setId(String name)
	{
		this.id = name; 
	}
	
	/**
     * Nastaví začátek linky (počáteční zastávku).
     * @param start start
     */
	public void setStart(Stop start)
	{
		this.start = start;
	}
	
	/**
     * Nastaví konečnou stanici.
     * @param stop konečná
     */
	public void setEnd(Stop stop)
	{
		this.end = stop;
	}
	
	/**
     * Přidá ulici do linky.
     * @param street ulice
     */
	public void addStreet(Street street)
	{
		this.streets.add(street);
	}
	
	/**
     * Přidá vozidlo patřící dané lince.
     * @param vehicle vozidlo
     */
	public void addVehicle(Vehicle vehicle)
	{
		this.vehicles.add(vehicle);
	}
	
	/**
     * Získá název linky.
     * @return String název linky
     */
	public String getId()
	{
		return this.id;
	}
	
	/**
     * Získá počáteční zastávku
     * @return Stop počáteční zastávka
     */
	public Stop getStart()
	{
		return this.start;
	}
	
	/**
     * Získá konečnou zastávku.
     * @return Stop konečná zastávka
     */
	public Stop getEnd()
	{
		return this.end;
	}
	
	/**
     * Získá seznam ulic.
     * @return List<Street> seznam ulic
     */
	public List<Street> getStreets()
	{
		return this.streets;
	}
	
	/**
     * Získá seznam vozidel.
     * @return List<Vehicle> seznam vozidel
     */
	@JsonIgnore
	public List<Vehicle> getVehicles()
	{
		return this.vehicles;
	}
	
	/**
     * Nastaví "focus" (zvýrazní) celou linku.
     * @param map mapa
     * @param streets ulice patřící lince
     */
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
	
	/**
     * Zruší "focus" (zvýrazní) celou linku.
     * @param map mapa
     * @param streets ulice patřící lince
     */
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
