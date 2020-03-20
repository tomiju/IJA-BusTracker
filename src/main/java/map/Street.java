package map;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import drawable.StreetView;

// TODO: výpočet, zda X,Y leží na přímce (na ulici)

/**
 * 
 * Třída reprezentující ulici.
 * Ulice má své ID, má proměnnou ukazující, zda je otevřená, či ne, obsahuje seznam zastávek a souřadnice začátku a konce, dále pak také úroveň vytížení.
 * @author Tomáš Julina (xjulin08)
 * @author Tomáš Kantor (xkanto14)
 *
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Street
{

	private String id;
	private boolean open; // otevřená / uzavřená ulice
	private List<Stop> stops;
	private Coordinate start, end;
	private int busyness; // vytížení
	
	@JsonIgnore
	private StreetView streetView;
	
	
	public Street(String name, Coordinate start, Coordinate end, List<Stop> stops)
	{
		this.id = name;
		this.stops = stops;
		this.start = start;
		this.end = end;
		this.busyness = 0;
		this.open = true;
		
		this.streetView = new StreetView();
		
		this.setStreetParameterToStop();
	}
	
	public Street() {}
	
  /**
   * Vrátí identifikátor ulice.
   * @return String Identifikátor ulice
   */
	public String getId()
	{
		return this.id;
	}
	
	/**
	 * Vrátí počáteční souřadnice ulice.
	 * @return Coordinate souřadnice ulice
	 */
	public Coordinate getStart()
	{	
		return this.start;
	}
	
	/**
	 * Vrátí konečné souřadnice ulice.
	 * @return Coordinate souřadnice ulice
	 */
	public Coordinate getEnd()
	{
		return this.end;
	}

  /**
   * Vrátí seznam zastávek na ulici.
   * @return Seznam zastávek na ulici. Pokud ulize nemá žádnou zastávku, je seznam prázdný.
   */
	public List<Stop> getStops()
	{
		return this.stops;
	}
	
	/**
	 * Vrátí status ulice.
	 * @return boolean status ulice (open/closed)
	 */
	@JsonIgnore
	public boolean getStatus()
	{
		return this.open;
	}
	
	/**
	 * Vrátí proměnnou reprezentující "zahlcenost" ulice
	 * @return int stupeň zahlcení ulice
	 */
	@JsonIgnore
	public int getBusyness()
	{
		return this.busyness;
	}
	
	/**
	 * Nastaví zastávkám ulici, na které leží.
	 */
	public void setStreetParameterToStop()
	{
		for (Stop stop : this.stops) 
		{
	        stop.setStreet(this);
		}
	}
	
	/**
	 * Zjistí, status ulice.
	 */
	public boolean isOpen()
	{
		return this.open;
	}

  /**
   * Přidá do seznamu zastávek novou zastávku.
   * @param stop Nově přidávaná zastávka.
   */
	public void addStop(Stop stop)
	{
		this.stops.add(stop);
		
		stop.setStreet(this);
	}
	
	/**
	 * Nastaví proměnnou obsahující grafickou reprezentaci ulice.
	 * @param view Objekt obsahující grafickou reprezentaci ulice.
	 */
	@JsonIgnore
	public void setStreetView(StreetView view)
	{
		this.streetView = view;
	}
	
	/**
	 * Vrátí proměnnou obsahující grafickou reprezentaci ulice.
	 * @return StreetView Objekt obsahující grafickou reprezentaci ulice.
	 */
	@JsonIgnore
	public StreetView getStreetView()
	{
		return this.streetView;
	}
	
	/**
	 * Nastaví status ulice
	 * @param status status
	 */
	@JsonIgnore
	public void setOpen(boolean status)
	{
		this.open = status;
	}
}
