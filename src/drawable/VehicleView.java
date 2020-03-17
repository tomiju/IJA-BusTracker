package drawable;

import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * 
 * Objekt reprezentující vozdilo v mapě (vykreslené)
 * @author Tomáš Julina (xjulin08)
 * @author Tomáš Kantor (xkanto14)
 *
 */
public class VehicleView 
{
	private Circle circle;
	private Text text;
	
	public VehicleView(Circle circle, Text text)
	{
		this.circle = circle;
		this.text = text;
	}
	
	/**
     * Nastaví grafickou reprezentaci vozidla.
     * @param circle vozidlo
     */
	public void setCircle(Circle circle)
	{
		this.circle = circle;
	}
	
	/**
     * Nastaví název vozidla.
     * @param text
     */
	public void setText(Text text)
	{
		this.text = text;
	}
	
	/**
     * Získá objekt reprezentující vozidlo.
     * @return Circle objekt
     */
	public Circle getCircle()
	{
		return this.circle;
	}
	
	/**
     * Získá název vozidla.,
     * @return Text název vozidla
     */
	public Text getText()
	{
		return this.text;
	}
}
