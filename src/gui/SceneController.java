
/**
 * 
 * Ovládání scén
 * @author Tomáš Julina (xjulin08)
 * @author Tomáš Kantor (xkanto14)
 *
 */

package gui;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;



public class SceneController implements Initializable {
	
	@FXML
    private Pane testPane;
	
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

        init();
		
	}
	
	public void init()
	{
		Line line = new Line(); 
        
	    //Setting the properties to a line 
	    line.setStartX(100.0); 
	    line.setStartY(150.0); 
	    line.setEndX(500.0); 
	    line.setEndY(150.0); 
		testPane.getChildren().add(line);
	}

}
