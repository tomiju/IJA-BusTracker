package drawable;

import javafx.scene.shape.Line;
import javafx.scene.text.Text;

/**
 * 
 * Objekt reprezentující ulici v mapě (vykreslená)
 * @author Tomáš Julina (xjulin08)
 * @author Tomáš Kantor (xkanto14)
 *
 */
public class StreetView 
{
	private Line line;
	private Text name;
	
	public StreetView(Line line, Text name)
	{
		this.line = line;
		this.name = name;
	}
	
	public StreetView() {}
	
	
	/**
     * Získá objekt reprezentující ulici.
     * @return Line grafická reprezentace ulice.
     */
	public Line getLine()
	{
		return this.line;
	}
	
	/**
     * Získá název ulice.
     * @return Text název ulice.
     */
	public Text getName()
	{
		return this.name;
	}
}
