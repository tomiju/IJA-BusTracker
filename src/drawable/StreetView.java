package drawable;

import javafx.scene.shape.Line;
import javafx.scene.text.Text;

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
	
	public Line getLine()
	{
		return this.line;
	}
	
	public Text getName()
	{
		return this.name;
	}
}
