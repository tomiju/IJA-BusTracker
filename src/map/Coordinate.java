package map;

/**
 * 
 * Reprezentuje pozici (souřadnice) v mapě.
 * Souřadnice je dvojice (x,y).
 * @author Tomáš Julina (xjulin08)
 * @author Tomáš Kantor (xkanto14)
 *
 */
public class Coordinate
{
	private float x, y;
	
	public Coordinate(float X, float Y)
	{
		this.x = X;
		this.y = Y;
	}
	
	public Coordinate() {}
	
  /**
   * Vrací hodnotu souřadnice x.
   * @return float Souřadnice x.
   */
	public float getX()
	{
		return this.x;
	}
	
  /**
   * Vrací hodnotu souřadnice y.
   * @return float Souřadnice y.
   */
	public float getY()
	{
		return this.y;
	}
}
