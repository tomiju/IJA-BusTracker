package map;

import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje jednu ulici v mapě. Ulice má svůj identifikátor (název) a je definována souřadnicemi. Pro 1. úkol
 * předpokládejte pouze souřadnice začátku a konce ulice.
 * Na ulici se mohou nacházet zastávky.
 */

// TODO: výpočet, zda X,Y leží na přímce (na ulici)
public class Street
{
	private Coordinate start, end;
	private String name;
	private List<Stop> stops;
	private List<Coordinate> coordinateList;
	private int busyness; // vytížení
	private boolean opened; // otevřená / uzavřená ulice
	
	public Street(String name, Coordinate start, Coordinate end, List<Stop> stops)
	{
		this.name = name;
		this.coordinateList = new ArrayList<>();
		this.coordinateList.add(start);
		this.stops = stops;
		this.start = start;
		this.end = end;
		this.busyness = 0;
		this.opened = true;
		
		this.setStreetParameterToStop();
	}
	
  /**
   * Vrátí identifikátor ulice.
   * @return Identifikátor ulice.
   */
	public String getId()
	{
		return this.name;
	}

  /**
   * Vrátí seznam souřadnic definujících ulici. První v seznamu je vždy počátek a poslední v seznamu konec ulice.
   * @return Seznam souřadnic ulice.
   */

	public List<Coordinate> getCoordinates()
	{
		this.coordinateList.add(this.end);
		return this.coordinateList;
	}

  /**
   * Vrátí seznam zastávek na ulici.
   * @return Seznam zastávek na ulici. Pokud ulize nemá žádnou zastávku, je seznam prázdný.
   */
	public List<Stop> getStops()
	{
		return this.stops;
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
		return this.opened;
	}

  /**
   * Přidá do seznamu zastávek novou zastávku.
   * @param stop Nově přidávaná zastávka.
   */
	public void addStop(Stop stop)
	{
		this.stops.add(stop);
		
		this.coordinateList.add(stop.getCoordinate());
		
		stop.setStreet(this);
	}
}
