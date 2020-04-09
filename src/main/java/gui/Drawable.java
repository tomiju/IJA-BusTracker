package gui;

import drawable.StreetView;
import drawable.VehicleView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import map.Stop;
import map.Street;
import map.Vehicle;

/**
 * 
 * Vykreslovani mapy
 * @author Tomas Julina (xjulin08)
 * @author Tomas Kantor (xkanto14)
 *
 */
public class Drawable 
{
	/**
     * Vykresli ulici do mapy.
     * @param street ulice
     * @param map mapa
     */
	public static void drawStreets(Street street, Pane map)
	{
		Line line = new Line();
		
		line.setStartX(street.getStart().getX()); 
	    line.setStartY(street.getStart().getY()); 
	    line.setEndX(street.getEnd().getX()); 
	    line.setEndY(street.getEnd().getY());
	    line.setStrokeWidth(2.5);
	   
	    Text text = new Text(street.getStart().getX(),street.getStart().getY(), street.getId());
	    
	    text.xProperty().bind(line.startXProperty().add(line.getEndX()).divide(2).add(7));
	    text.yProperty().bind(line.startYProperty().add(line.getEndY()).divide(2).add(-10));
	    
	    //text.setOnMouseClicked(e -> setStreetStatus(street, line));
	    //line.setOnMouseClicked(e -> setStreetStatus(street, line));
	    
	    map.getChildren().add(line);
	    map.getChildren().add(text);
	    
	    street.setStreetView(new StreetView(line, text));
	    
	    drawStops(street, map);
	}
	
	/**
     * Vykresli ulici do mapy.
     * @param street ulice
     * @param map mapa
     */
	public static void drawStops(Street street, Pane map)
	{
		if (street.getStops() != null)
		{
			for (Stop stop : street.getStops()) 
			{
				Circle circle = new Circle();
				Text text = new Text();
				
				circle.setRadius(7.0f);
				circle.setFill(Color.RED);
				circle.setCenterX(stop.getCoordinate().getX());
				circle.setCenterY(stop.getCoordinate().getY());
		
				text.setText(stop.getId());
				text.xProperty().bind(circle.centerXProperty().add(15));
				text.yProperty().bind(circle.centerYProperty().add(15));

				map.getChildren().add(circle);
			    map.getChildren().add(text);	
			}
		}
	}
	
	/**
     * Vykresli vozidlo do mapy.
     * @param vehicle vozidlo
     * @param map mapa
     */
	public static void drawVehicles(Vehicle vehicle, Pane map)
	{
			Circle circle = new Circle();
			Text text = new Text();
			
			circle.setRadius(7.0f);
			circle.setFill(Color.BLUE);
			circle.setCenterX(vehicle.getCurrentPosition().getX());
			circle.setCenterY(vehicle.getCurrentPosition().getY());
	
			text.setText(vehicle.getId());
			text.xProperty().bind(circle.centerXProperty().add(7));
			text.yProperty().bind(circle.centerYProperty());

			map.getChildren().add(circle);
		    map.getChildren().add(text);	
		    
		    vehicle.setVehicle(new VehicleView(circle, text));
	}
	
	/**
	 * Zvyrazni novou trasu (umozni naklikavat v edit mode)
	 * @param street
	 * @param line
	 * @param editMode
	 */
	public static void setNewPath(Street street, Line line, boolean editMode)
	{
		if (editMode)
		{
			if(street.getStatus())
			{ 
				// TODO: menit "path" pro animaci -> podle naklikavani ukladat jmena ulic do nejakeho seznamu, ten potom pouzit pro kazdou linku
				// a zvlast pro kazde vozidlo sestavit novou cestu
				line.setStroke(Color.BLUE);
			}
		}
	}
	
	/**
     * Uzavre / otevre ulici (prujezdna/neprujezdna).
     * @param street ulice
     * @param line graficka reprezentace ulice
     */
	public static void setStreetStatus(Street street, Line line, boolean editMode)
	{
		if (editMode)
		{
			if(street.getStatus())
			{
				street.setOpen(false);
				line.setStroke(Color.RED);
			}
			else
			{
				street.setOpen(true);
				line.setStroke(Color.BLACK);
			}
		}
	}
}
