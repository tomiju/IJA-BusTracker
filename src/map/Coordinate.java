package map;


/**
 * Reprezentuje pozici (souřadnice) v mapě. Souřadnice je dvojice (x,y), počátek mapy je vždy na pozici (0,0).
 * Nelze mít pozici se zápornou souřadnicí.
 */
public class Coordinate
{
	private int coordX, coordY;
	
	public Coordinate(int X, int Y)
	{
		this.coordX = X;
		this.coordY = Y;
	}
	
	public static Coordinate create(int X, int Y)
	{
		if(X >= 0 && Y >= 0)
		{
			return new Coordinate(X,Y);
		}
		else
		{
			return null;
		}
	}
	
  /**
   * Vrací hodnotu souřadnice x.
   * @return Souřadnice x.
   */
	public int getX()
	{
		return this.coordX;
	}
	
  /**
   * Vrací hodnotu souřadnice y.
   * @return Souřadnice y.
   */
	public int getY()
	{
		return this.coordY;
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

       Coordinate coordinate = (Coordinate) obj;
       return this.getX() == coordinate.getX() && this.getY() == coordinate.getY();
    }
}
