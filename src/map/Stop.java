package map;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Reprezentuje zastávku. Zastávka má svůj unikátní identifikátor a dále souřadnice umístění a zná ulici, na které je umístěna.
 * Zastávka je jedinečná svým identifikátorem. Reprezentace zastávky může existovat, ale nemusí mít
 * přiřazeno umístění (tj. je bez souřadnic a bez znalosti ulice). Pro shodu objektů platí, že dvě zastávky jsou shodné, pokud
 * mají stejný identifikátor.
 */

//@//JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
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
   * Vrátí identifikátor zastávky.
   * @return Identifikátor zastávky.
   */
	public String getId()
	{
		return this.id;
	}

  /**
   * Vrátí pozici zastávky.
   * @return Pozice zastávky. Pokud zastávka existuje, ale dosud nemá umístění, vrací null.
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
		
  /**
 	* Nastaví ulici, na které je zastávka umístěna.
	* @param s Ulice, na které je zastávka umístěna.
	*/
	public void setStreet(Street s)
	{
		this.street = s;
	}
	
	@Override
    public boolean equals(Object obj) 
	{
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

       Stop stop = (Stop) obj;
       return this.getId() == stop.getId();
    }
}
