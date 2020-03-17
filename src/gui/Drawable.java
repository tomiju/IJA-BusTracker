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
 * Vykreslování mapy
 * @author Tomáš Julina (xjulin08)
 * @author Tomáš Kantor (xkanto14)
 *
 */
public class Drawable 
{
	/**
     * Vykreslí ulici do mapy.
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
	    line.setStrokeWidth(2);
	   
	    Text text = new Text(street.getStart().getX(),street.getStart().getY(), street.getId());
	    
	    text.xProperty().bind(line.startXProperty().add(line.getEndX()).divide(2).add(7));
	    text.yProperty().bind(line.startYProperty().add(line.getEndY()).divide(2));
	    
	    text.setOnMouseClicked(e -> setStreetStatus(street, line));
	    
	    map.getChildren().add(line);
	    map.getChildren().add(text);
	    
	    street.setStreetView(new StreetView(line, text));
	    
	    drawStops(street, map);
	}
	
	/**
     * Vykreslí ulici do mapy.
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
				circle.setFill(javafx.scene.paint.Color.RED);
				circle.setCenterX(stop.getCoordinate().getX());
				circle.setCenterY(stop.getCoordinate().getY());
		
				text.setText(stop.getId());
				text.xProperty().bind(circle.centerXProperty().add(7));
				text.yProperty().bind(circle.centerYProperty());

				map.getChildren().add(circle);
			    map.getChildren().add(text);	
			}
		}
	}
	
	/**
     * Vykreslí vozidlo do mapy.
     * @param vehicle vozidlo
     * @param map mapa
     */
	public static void drawVehicles(Vehicle vehicle, Pane map)
	{
			Circle circle = new Circle();
			Text text = new Text();
			
			circle.setRadius(7.0f);
			circle.setFill(javafx.scene.paint.Color.BLUE);
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
     * Uzavře / otevře ulici (průjezdná/neprůjezdná).
     * @param street ulice
     * @param line grafická reprezentace ulice
     */
	public static void setStreetStatus(Street street, Line line)
	{
		if(street.getStatus())
		{
			line.setStroke(Color.RED);
			street.setOpen(false);
		}
		else
		{
			line.setStroke(Color.BLACK);
			street.setOpen(true);
		}
	}
}
