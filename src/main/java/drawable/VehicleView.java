package drawable;

import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * 
 * Objekt reprezentujici vozdilo v mape (vykreslene)
 * @author Tomas Julina (xjulin08)
 * @author Tomas Kantor (xkanto14)
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
     * Nastavi grafickou reprezentaci vozidla.
     * @param circle vozidlo
     */
	public void setCircle(Circle circle)
	{
		this.circle = circle;
	}
	
	/**
     * Nastavi nazev vozidla.
     * @param text
     */
	public void setText(Text text)
	{
		this.text = text;
	}
	
	/**
     * Ziska objekt reprezentujici vozidlo.
     * @return Circle objekt
     */
	public Circle getCircle()
	{
		return this.circle;
	}
	
	/**
     * Ziska nazev vozidla.,
     * @return Text nazev vozidla
     */
	public Text getText()
	{
		return this.text;
	}
}
