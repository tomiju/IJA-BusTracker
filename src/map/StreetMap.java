package map;

import java.util.List;


/**
 * Reprezentuje jednu mapu, která obsahuje ulice.
 */
public class StreetMap
{
	private List<Stop> stops;
	private List<Street> streets;
	
	public StreetMap(List<Stop> stops, List<Street> streets) 
	{
		this.streets = streets;
		this.stops = stops;
	}
	
	public StreetMap() {}
	
  /**
   * Přidá ulici do mapy.
   * @param s Objekt reprezentující ulici.
   */
	public void addStreet(Street s)
	{
		this.streets.add(s);
	}
  
  /**
 	* Přidá ulici do mapy.
 	* @param s Objekt reprezentující ulici.
 	*/
	public void addStop(Stop s)
	{
		this.stops.add(s);
	}
	
	public List<Street> getStreets()
	{
		return this.streets;
	}
	
	public List<Stop> getStops()
	{
		return this.stops;
	}
	
  /**
   * Vrátí objekt reprezentující ulici se zadaným id.
   * @param id Identifikátor ulice.
   * @return Nalezenou ulici. Pokud ulice s daným identifikátorem není součástí mapy, vrací null.
   */
	public Street getStreet(String id)
	{
		for (Street CurrentStreet : this.streets) 
		{
	        if (CurrentStreet.getId().equals(id)) 
	        {
	            return CurrentStreet;
	        }
		}
		return null;
	}
}
