package drawable;

import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class VehicleView 
{
	private Circle circle;
	private Text text;
	
	public VehicleView(Circle circle, Text text)
	{
		this.circle = circle;
		this.text = text;
	}
	
	public void setCircle(Circle circle)
	{
		this.circle = circle;
	}
	
	public void setText(Text text)
	{
		this.text = text;
	}
	
	public Circle getCircle()
	{
		return this.circle;
	}
	
	public Text getText()
	{
		return this.text;
	}
}
