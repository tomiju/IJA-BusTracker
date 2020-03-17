package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import map.BusLine;
import map.InputData;
import map.Street;
import map.Vehicle;

/**
 * 
 * Ovládání scén.
 * @author Tomáš Julina (xjulin08)
 * @author Tomáš Kantor (xkanto14)
 *
 */
public class SceneController implements Initializable {
	private static InputData data;
	
	@FXML
    private Pane map;
	
	@FXML
	private ListView<String> lineList;
	
	@FXML
	private Group scroll;
	
	/**
     * Ovládá zoom, mění scale "mapy".
     * @param e event
     */
	@FXML
	private void onStackPaneScroll(ScrollEvent e)
	{
		if(e.isControlDown())
		{
			e.consume();
			
			double zoom = e.getDeltaY() > 0 ? 1.1 : 1 / 1.1;
			
			this.map.setScaleX(zoom * this.map.getScaleX());
			this.map.setScaleY(zoom * this.map.getScaleY());
			
			this.scroll.layout();
		}
	}
	
	/**
     * Resetuje barvu ulice (zruší focus).
     * @param event event
     */
	@FXML
	private void handleFocusReset(ActionEvent event)
	{
		for (BusLine line : SceneController.data.getLines()) 
		{
			line.unsetLineFocus(map, line.getStreets());
		}
	}
	
	/**
     * Konstruktor hlavního okna.
     * @param location
     * @param resources
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		Stage owStage = new Stage();
        FXMLLoader owLoader = new FXMLLoader();
        Parent opWindow = null;
        Scene ow = null;

        try 
        {
        	opWindow = owLoader.load(getClass().getResource("initWindow.fxml").openStream());
            ow = new Scene(opWindow);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        InitWindowController iwController = (InitWindowController) owLoader.getController();

        owStage.setTitle("Bus tracker");
        owStage.setScene(ow);
        iwController.setStage(owStage);
        owStage.showAndWait();
        SceneController.data = iwController.getData(); // vstupní data
        
        this.lineList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
            	
            	for (BusLine line : gui.SceneController.data.getLines()) 
        		{
            		if (line != null)
        			{
            			if(line.getId() == newValue)
                		{
                			 line.setLineFocus(map, line.getStreets());
                		}
            			
            			if(line.getId() == oldValue)
            			{
            				line.unsetLineFocus(map, line.getStreets());
            			}
        			}	
        		}
            }
        });
        
        initMap();	
	}
	
	/**
     * Vykreslí mapu a její prvky.
     * @param location
     * @param resources
     */
	public void initMap()
	{
		for (Street street : SceneController.data.getStreets()) 
		{
			Drawable.drawStreets(street, map);
		}
		
		for (BusLine line : SceneController.data.getLines()) 
		{
			if (line != null)
			{
				this.lineList.getItems().add(line.getId());
			}
		}
		
		for (Vehicle vehicle : SceneController.data.getVehicles()) 
		{
			vehicle.setCurrentPosition(vehicle.getLine().getStreets().get(0).getStart()); // nastavení počáteční pozice auta
			vehicle.setCurrentStreet(vehicle.getLine().getStart().getStreet()); // nastavení počáteční ulice
			
			Drawable.drawVehicles(vehicle, map);
		}
	}

}
