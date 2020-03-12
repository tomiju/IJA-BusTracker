package map;


/**
 * Reprezentuje zastávku. Zastávka má svůj unikátní identifikátor a dále souřadnice umístění a zná ulici, na které je umístěna.
 * Zastávka je jedinečná svým identifikátorem. Reprezentace zastávky může existovat, ale nemusí mít
 * přiřazeno umístění (tj. je bez souřadnic a bez znalosti ulice). Pro shodu objektů platí, že dvě zastávky jsou shodné, pokud
 * mají stejný identifikátor.
 */
public class Stop
{
	private String stopID;
	private Coordinate stopCoordinate;
	private Street stopStreet;
	
	public Stop(String id, Coordinate coordinate)
	{
		this.stopID = id;
		this.stopCoordinate = coordinate;
		this.stopStreet = null;
	}
	
	public Stop(String id)
	{
		this.stopID = id;
		this.stopStreet = null;
	}
	
  /**
   * Vrátí identifikátor zastávky.
   * @return Identifikátor zastávky.
   */
	public String getId()
	{
		return this.stopID;
	}

  /**
   * Vrátí pozici zastávky.
   * @return Pozice zastávky. Pokud zastávka existuje, ale dosud nemá umístění, vrací null.
   */
	public Coordinate getCoordinate()
	{
		return this.stopCoordinate;
	}

  /**
   * Nastaví ulici, na které je zastávka umístěna.
   * @param s Ulice, na které je zastávka umístěna.
   */
	public void setStreet(Street s)
	{
		this.stopStreet = s;
	}

  /**
   * Vrátí ulici, na které je zastávka umístěna.
   * @return Ulice, na které je zastávka umístěna. Pokud zastávka existuje, ale dosud nemá umístění, vrací null.
   */
	public Street getStreet()
	{
		return this.stopStreet;
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
