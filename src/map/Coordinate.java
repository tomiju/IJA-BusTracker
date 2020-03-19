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
	private double x, y;
	
	public Coordinate(double X, double Y)
	{
		this.x = X;
		this.y = Y;
	}
	
	public Coordinate() {}
	
  /**
   * Vrací hodnotu souřadnice x.
   * @return double Souřadnice x.
   */
	public double getX()
	{
		return this.x;
	}
	
  /**
   * Vrací hodnotu souřadnice y.
   * @return double Souřadnice y.
   */
	public double getY()
	{
		return this.y;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
}
