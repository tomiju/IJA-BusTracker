package map;

/**
 * 
 * Reprezentuje pozici (souradnice) v mape.
 * Souradnice je dvojice (x,y).
 * @author Tomas Julina (xjulin08)
 * @author Tomas Kantor (xkanto14)
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
   * Vraci hodnotu souradnice x.
   * @return double Souradnice x.
   */
	public double getX()
	{
		return this.x;
	}
	
  /**
   * Vraci hodnotu souradnice y.
   * @return double Souradnice y.
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
