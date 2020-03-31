package map;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 * 
 * Trida reprezentujici zastavku.
 * Zastavka ma sve ID, souradnice umisteni a zna take ulici, na ktere je umistena.
 * @author Tomas Julina (xjulin08)
 * @author Tomas Kantor (xkanto14)
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
 	* Nastavi ulici, na ktere je zastavka umistena.
	* @param s Ulice, na ktere je zastavka umistena
	*/
	public void setStreet(Street s)
	{
		this.street = s;
	}
	
  /**
   * Vrati identifikator zastavky.
   * @return String Identifikator zastavky.
   */
	public String getId()
	{
		return this.id;
	}

  /**
   * Vrati pozici zastavky.
   * @return Pozice zastavky
   */
	public Coordinate getCoordinate()
	{
		return this.coordinate;
	}

  /**
   * Vrati ulici, na ktere je zastavka umistena.
   * @return Ulice, na ktere je zastavka umistena. Pokud zastavka existuje, ale dosud nema umisteni, vraci null.
   */
	@JsonIgnore
	public Street getStreet()
	{
		return this.street;
	}
}
