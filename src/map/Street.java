package map;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import drawable.StreetView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Reprezentuje jednu ulici v mapě. Ulice má svůj identifikátor (název) a je definována souřadnicemi. Pro 1. úkol
 * předpokládejte pouze souřadnice začátku a konce ulice.
 * Na ulici se mohou nacházet zastávky.
 */

// TODO: výpočet, zda X,Y leží na přímce (na ulici)

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
   * @return Identifikátor ulice.
   */
	public String getId()
	{
		return this.id;
	}

  /**
   * Vrátí seznam souřadnic definujících ulici. První v seznamu je vždy počátek a poslední v seznamu konec ulice.
   * @return Seznam souřadnic ulice.
   */

	public Coordinate getStart()
	{
		
		return this.start;
	}
	
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
	
	@JsonIgnore
	public boolean getStatus()
	{
		return this.open;
	}
	
	public void setStreetParameterToStop()
	{
		for (Stop stop : this.stops) 
		{
	        stop.setStreet(this);
		}
	}
	
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
	
	@JsonIgnore
	public void setStreetView(StreetView view)
	{
		this.streetView = view;
	}
	
	@JsonIgnore
	public StreetView getStreetView()
	{
		return this.streetView;
	}
	
	@JsonIgnore
	public void setOpen(boolean status)
	{
		this.open = status;
	}
}
