/**
 *
 * @author Tomas Julina (xjulin08)
 * @author Tomas Kantor (xkanto14)
 *
 */

package drawable;

import javafx.scene.shape.Line;
import javafx.scene.text.Text;

/**
 *
 * Objekt reprezentujici ulici v mape (vykreslena)
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
     * Ziska objekt reprezentujici ulici.
     * @return Line graficka reprezentace ulice.
     */
	public Line getLine()
	{
		return this.line;
	}

	/**
     * Ziska nazev ulice.
     * @return Text nazev ulice.
     */
	public Text getName()
	{
		return this.name;
	}
}
