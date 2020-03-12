package map;

import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje jednu mapu, která obsahuje ulice.
 */
public class StreetMap
{
	private List<Street> streetMap;
	
	public StreetMap() 
	{
		this.streetMap = new ArrayList<>();
	}
	
  /**
   * Přidá ulici do mapy.
   * @param s Objekt reprezentující ulici.
   */
	public void addStreet(Street s)
	{
		this.streetMap.add(s);
	}

  /**
   * Vrátí objekt reprezentující ulici se zadaným id.
   * @param id Identifikátor ulice.
   * @return Nalezenou ulici. Pokud ulice s daným identifikátorem není součástí mapy, vrací null.
   */
	public Street getStreet(String id)
	{
		for (Street CurrentStreet : this.streetMap) 
		{
	        if (CurrentStreet.getId().equals(id)) 
	        {
	            return CurrentStreet;
	        }
		}
		return null;
	}
}
