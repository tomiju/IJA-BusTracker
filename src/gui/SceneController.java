
/**
 * 
 * Ovládání scén
 * @author Tomáš Julina (xjulin08)
 * @author Tomáš Kantor (xkanto14)
 *
 */

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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import map.BusLine;
import map.InputData;
import map.Street;



public class SceneController implements Initializable {
	private static InputData data;
	
	@FXML
    private Pane map;
	
	@FXML
	private ListView<String> lineList;
	
	@FXML
    private void handleButtonAction(ActionEvent event) 
	{
        System.out.println("You clicked me!");
    }
	
	
	// Funkce otevře úvodní okno - z tama vybereme vstup -> podle vstupu se nastaví instance třídy a po zavření okna se vykreslí mapa v hlavním okně
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
        iwController.setSceneController(this);
        owStage.showAndWait();
        SceneController.data = iwController.getData(); // vstupní data
        
        this.lineList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
            	
            	System.out.println(newValue);
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
        
        init();	
	}
	
	public void init()
	{
		for (Street street : SceneController.data.getStreets()) 
		{
			System.out.println(gui.SceneController.data.getLines().get(0).getId());
			Drawable.drawStreets(street, map);
		}
		
		for (BusLine line : SceneController.data.getLines()) 
		{
			if (line != null)
			{
				this.lineList.getItems().add(line.getId());
			}
		}
		
		if(this.lineList.getItems() != null)
		{

		}
		
	}

}
