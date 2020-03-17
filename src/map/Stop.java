package map;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 * 
 * Třída reprezentující zastávku.
 * Zastávka má své ID, souřadnice umíštění a zná také ulici, na které je umístěna.
 * @author Tomáš Julina (xjulin08)
 * @author Tomáš Kantor (xkanto14)
 *
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Stop
{
	private String id;
	private Coordinate coordinate;
	private Street street;
	
	public Stop(String id, Coordinate coordinate)
	{
		this.id = id;
		this.coordinate = coordinate;
	}
	
	public Stop() {}
	
  /**
 	* Nastaví ulici, na které je zastávka umístěna.
	* @param s Ulice, na které je zastávka umístěna
	*/
	public void setStreet(Street s)
	{
		this.street = s;
	}
	
  /**
   * Vrátí identifikátor zastávky.
   * @return String Identifikátor zastávky.
   */
	public String getId()
	{
		return this.id;
	}

  /**
   * Vrátí pozici zastávky.
   * @return Pozice zastávky
   */
	public Coordinate getCoordinate()
	{
		return this.coordinate;
	}

  /**
   * Vrátí ulici, na které je zastávka umístěna.
   * @return Ulice, na které je zastávka umístěna. Pokud zastávka existuje, ale dosud nemá umístění, vrací null.
   */
	@JsonIgnore
	public Street getStreet()
	{
		return this.street;
	}
}
