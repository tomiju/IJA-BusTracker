package gui;

import java.util.List;

import drawable.StreetView;
import drawable.VehicleView;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import map.BusLine;
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
	public static void setNewPath(Street street, Line line, boolean editMode, ListView<String> streetList, String editedBusLine, List<BusLine> lines)
	{
		if (editMode)
		{
			for (BusLine busLine : lines)
			{
				if (busLine.getId() == editedBusLine)
				{
					if (busLine.isEdited())
					{
						if(street.getStatus())
						{ 
							// TODO: menit "path" pro animaci -> podle naklikavani ukladat jmena ulic do nejakeho seznamu, ten potom pouzit pro kazdou linku
							// a zvlast pro kazde vozidlo sestavit novou cestu
							// kdyz pridavam do seznamu - pridavat i do noveho seznamu ulic pro danou linku (nejspis predat funkci jmeno nakliknute linky)
							boolean inList = false;							
							
							if (streetList.getItems().isEmpty())
							{
								streetList.getItems().add(street.getId());
								line.setStroke(Color.BLUE);
								
								busLine.addStreet(street);
								//DEBUG
								/*System.out.println("Debug edited streets:");
								for (Street street1 : busLine.getStreets())
								{
									System.out.println(street1.getId());
								}*/
							}
							
							for (String item : streetList.getItems()) // lze pridat pouze ulice, ktere jiz v seznamu nejsou!
							{
								if (street.getId() == item)
								{
									inList = true;
									break;
								}
							}
							
							if (!inList) // lze pridat pouze ulice, ktere jiz v seznamu nejsou!
							{
								streetList.getItems().add(street.getId());
								line.setStroke(Color.BLUE);
								
								if (busLine.getId() == editedBusLine)
								{
									busLine.addStreet(street);
									//DEBUG
									/*System.out.println("Debug edited streets:");
									for (Street street1 : busLine.getStreets())
									{
										System.out.println(street1.getId());
									}*/
								}
							}
						}
					}
				}
			}
		}
	}

	
	/**
     * Uzavre / otevre ulici (prujezdna/neprujezdna).
     * @param street ulice
     * @param line graficka reprezentace ulice
     */
	public static void setStreetStatus(Street street, Line line, boolean editMode, List<BusLine> busLines)
	{
		if (editMode)
		{
			if(street.getStatus())
			{
				street.setOpen(false);
				line.setStroke(Color.RED);
				
				for	(BusLine busLine : busLines)
				{
					for	(Street street_busLines : busLine.getStreets())
					{
						if (street_busLines.getId() == street.getId())
						{
							//System.out.println(busLine.getId()); // debug
							Alert alert = new Alert(Alert.AlertType.INFORMATION);
							alert.setTitle("Street closing information");
							alert.setHeaderText("Warning\nYou have closed street:  " + street.getId());
							alert.setContentText("The path has been affected by street closing, you will now need to select new path for line:  " + busLine.getId());
							alert.show();
							
							busLine.setEdit(true);
							busLine.resetStreetsForEditing();
						}
					}
				}
			}
			else
			{
				street.setOpen(true);
				line.setStroke(Color.BLACK);
				
				boolean stillEdited = false;
				BusLine pom = new BusLine();
							
				// pokud po otevreni ulice je v lince jeste nejaka uzavrena ulice, tak je stale v edit modu, jinak se edit mode pro ni vypne
				for	(BusLine busLine : busLines)
				{
					for	(Street street_busLines : busLine.getStreets())
					{
						if (street_busLines.getId() == street.getId())
						{
							for	(Street street_busLines2 : busLine.getStreets())
							{
								if (!street_busLines2.isOpen())
								{
									stillEdited = true;
									break;
								}
							}
							
							pom = busLine;
							break;
						}
					}
				}
				
				if (!stillEdited)
				{
					pom.setEdit(false);
				}
			}
			
		}
	}
}
