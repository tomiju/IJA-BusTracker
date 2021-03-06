/**
 *
 * @author Tomas Julina (xjulin08)
 * @author Tomas Kantor (xkanto14)
 *
 */

package gui;

import java.util.ArrayList;

import drawable.StreetView;
import drawable.VehicleView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.control.ButtonBar;
import map.BusLine;
import map.Stop;
import map.Street;
import map.Vehicle;

/**
 *
 * Vykreslovani mapy
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
	    line.setStrokeWidth(3.0);
	    line.setStroke(Color.GREY);

	    Text text = new Text(street.getStart().getX(),street.getStart().getY(), street.getId());

	    text.xProperty().bind(line.startXProperty().add(line.getEndX()).divide(2).add(7));
	    text.yProperty().bind(line.startYProperty().add(line.getEndY()).divide(2).add(-10));
		text.setFill(Color.GREY);

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

				circle.setRadius(8.0f);
				circle.setFill(Color.RED);
				circle.setStroke(Color.GREY);
				circle.setStrokeWidth(3.0);
				circle.setCenterX(stop.getCoordinate().getX());
				circle.setCenterY(stop.getCoordinate().getY());

				text.setText(stop.getId());
				text.setFill(Color.GREY);
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

			circle.setRadius(8.0f);
			circle.setFill(Color.BLUE);
			circle.setStroke(Color.GREY);
			circle.setStrokeWidth(3.0);
			circle.setOpacity(0.8);
			circle.setCenterX(vehicle.getCurrentPosition().getX());
			circle.setCenterY(vehicle.getCurrentPosition().getY());

			text.setText(vehicle.getId());
			text.xProperty().bind(circle.centerXProperty().add(10));
			text.yProperty().bind(circle.centerYProperty());
			text.setFill(Color.GAINSBORO);

		    text.setVisible(false);
		    circle.setVisible(false);

			map.getChildren().add(circle);
		    map.getChildren().add(text);

		    vehicle.setVehicle(new VehicleView(circle, text));
	}

	/**
	 * Zvyrazni novou trasu (umozni naklikavat v edit mode)
	 * @param street ulice
	 * @param line graficka reprezentace ulice
	 * @param editMode stav editacniho modu
	 * @param streetList seznam nove cesty
	 * @param editedBusLine nazev editovane linky
	 * @param lines seznam linek
	 */
	public static void setNewPath(Street street, Line line, boolean editMode, ListView<String> streetList, String editedBusLine, ArrayList<BusLine> lines)
	{
		if (editMode)
		{
			if(!street.getStatus())
			{
				setStreetStatus(street, line, editMode, lines);
			}
			else
			{
				for (BusLine busLine : lines)
				{
					if (busLine.getId() == editedBusLine)
					{
						if (busLine.isEdited())
						{
							if(street.getStatus())
							{
								boolean inList = false;
								boolean firstItem = false;

								if (streetList.getItems().isEmpty())
								{
									streetList.getItems().add(street.getId()); // pridani nakliknute ulice do seznamu
									line.setStroke(Color.CYAN);
									firstItem = true;

									busLine.addStreet(street);
								}

								if(!firstItem)
								{
									for (String item : streetList.getItems()) // lze pridat pouze ulice, ktere jiz v seznamu nejsou!
									{
										if (street.getId() == item)
										{
											inList = true;
											if (!streetList.getItems().isEmpty())
											{
												if(street.getId() == streetList.getItems().get(streetList.getItems().size() - 1))
												{
													streetList.getItems().remove(streetList.getItems().size() - 1);
													busLine.getStreets().remove(busLine.getStreets().size() - 1);
													line.setStroke(Color.GREY);
												}
											}
											break;
										}
									}

									if (!inList) // lze pridat pouze ulice, ktere jiz v seznamu nejsou!
									{
										streetList.getItems().add(street.getId());
										line.setStroke(Color.CYAN);

										if (busLine.getId() == editedBusLine)
										{
											busLine.addStreet(street);
										}
									}
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
     * @param editMode stav editacniho modu
     * @param busLines seznam vsech autobusovych linek
     */
	public static void setStreetStatus(Street street, Line line, boolean editMode, ArrayList<BusLine> busLines)
	{
		if (editMode)
		{
			if(street.getStatus())
			{
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

				DialogPane dialogPane = alert.getDialogPane();
				dialogPane.getStylesheets().add("/style.css");

				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.setTitle("Street closing information");
				alert.setHeaderText("You are about to close street: " + street.getId());
				alert.setContentText("Do you want to continue?");
				ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
				ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
				alert.getButtonTypes().setAll(okButton, noButton);
				alert.showAndWait().ifPresent(type -> {
				        if (type.getButtonData().toString() == "YES")
				        {
				    		//Drawable.resetColors(busLines);

							street.setOpen(false); // uzavre ulici
							line.setStroke(Color.RED);
							line.getStrokeDashArray().addAll(3.0,7.0,3.0,7.0);
							
							for	(BusLine busLine : busLines)
							{
								for	(Street street_busLines : busLine.getStreets())
								{
									if (street_busLines.getId() == street.getId()) // upozorneni, kterych ulic se uzavreni tyka
									{
										Alert alert1 = new Alert(Alert.AlertType.WARNING);

										DialogPane dialogPane1 = alert1.getDialogPane();
										dialogPane1.getStylesheets().add("/style.css");

										alert1.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
										alert1.setTitle("Street closing information");
										alert1.setHeaderText("Warning\nYou have closed street:  " + street.getId());
										alert1.setContentText("Line:  " + busLine.getId() + "\nThe path has been edited.");
										alert1.show();

										for (Vehicle vehicle : busLine.getVehicles())
										{
											vehicle.cancelDriving();
										}

										busLine.setEdit(true);
										busLine.resetStreetsForEditing();
									}
								}
							}
				        }
				        else{}
				});
			}
			else
			{
				street.setOpen(true);
				line.setStroke(Color.GREY);

				line.getStrokeDashArray().clear();

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

	public static void resetColors(ArrayList<BusLine> busLines)
	{
		for	(BusLine busLine : busLines)
		{
			busLine.unsetLineFocus();
		}
	}
}
