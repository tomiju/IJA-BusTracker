package map;

import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje jednu ulici v mapě. Ulice má svůj identifikátor (název) a je definována souřadnicemi. Pro 1. úkol
 * předpokládejte pouze souřadnice začátku a konce ulice.
 * Na ulici se mohou nacházet zastávky.
 */
public class Street
{
	private Coordinate start, end;
	private String name;
	private List<Stop> stopList;
	private List<Coordinate> coordinateList;
	
	public Street(String name, Coordinate start, Coordinate end)
	{
		this.coordinateList = new ArrayList<>();
		this.name = name;
		this.start = start;
		this.end = end;
		this.stopList = new ArrayList<>();
		this.coordinateList.add(this.start);
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
		return this.stopList;
	}

  /**
   * Přidá do seznamu zastávek novou zastávku.
   * @param stop Nově přidávaná zastávka.
   */
	public void addStop(Stop stop)
	{
		this.stopList.add(stop);
		stop.setStreet(this);
	}
}
