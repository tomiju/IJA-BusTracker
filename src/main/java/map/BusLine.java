package map;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
public class BusLine implements Cloneable
{
	private String id;
	private Stop start, end;
	private ArrayList<Street> streets;
	private ArrayList<Vehicle> vehicles;
	
	private boolean edited = false;
	
	public BusLine(String id, ArrayList<Street> streets)
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
	 * Nastavi aktualni linku jako editovanou
	 * @param isEdited status linky
	 */
	public void setEdit(boolean isEdited)
	{
		this.edited = isEdited;
	}
	
	/**
	 * Vymaze seznam ulic v pripade, ze se musi linka editovat
	 */
	public void resetStreetsForEditing()
	{
		this.streets = new ArrayList<Street>();
	}
	
	/**
	 * Nahraje puvodni stav ulic
	 * @param backupStreets puvodni ulice
	 */
	public void resetSimulation(ArrayList<Street> backupStreets)
	{
		this.streets = new ArrayList<Street>();
		this.streets = backupStreets;
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
     * @return ArrayList seznam ulic
     */
	public ArrayList<Street> getStreets()
	{
		return this.streets;
	}
	
	/**
     * Ziska seznam vozidel.
     * @return ArrayList seznam vozidel
     */
	@JsonIgnore
	public ArrayList<Vehicle> getVehicles()
	{
		return this.vehicles;
	}
	
	/**
	 * Zjisti, zda aktualni linka byla editovana - kvuli uzavirce cesty
	 * @return boolean stav linky
	 */
	@JsonIgnore
	public boolean isEdited()
	{
		return this.edited;
	}
	
	/**
     * Nastavi "focus" (zvyrazni) na celou linku.
     */
	@JsonIgnore
	public void setLineFocus()
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
     */
	@JsonIgnore
	public void unsetLineFocus()
	{
		for (Street street : this.streets) 
		{
			if (street.isOpen())
			{
				Line line = street.getStreetView().getLine();
				line.setStroke(Color.GREY);
			}
		}
		
		for (Vehicle vehicle : this.vehicles)
		{
			Circle circle = vehicle.getVehicleView().getCircle();
			circle.setFill(Color.BLUE);
		}
	}
	
	/**
	 * Nastavi barvu trase vybraného vozidla
	 */
	public void setVehicleInfoFocus()
	{
		for (Street street : this.streets) 
		{
			if (street.isOpen())
			{
				Line line = street.getStreetView().getLine();
				line.setStroke(Color.ORANGE);
			}
		}
	}
	
	/**
	 * Funkce pro hlubokou kopii stavu linek nutna pro restart simulace (navrat do puvodniho stavu pred editacemi).
	 */
	@Override
	public Object clone() throws CloneNotSupportedException 
	{

	    return super.clone();
	}
	
}
