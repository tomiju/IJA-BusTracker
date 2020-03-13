package gui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class InitWindowController implements Initializable {
	
	private Stage stage;
	private SceneController sceneController;
	
	
	@FXML
	private void loadFile(ActionEvent event) //TODO
	{
		FileChooser fileChooser = new FileChooser(); // otevře prohledávání souborů TODO: zpracovat vstupní soubor
        File file = fileChooser.showOpenDialog(null);
        
        if (file != null) {
        	 System.out.println("You clicked me!");
        }
        this.stage.close();
	}
	
	/**
     * Konstruktor úvodního okna
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {}
	
    /**
     * Nastaví scénu hlavního okna
     * @param sceneController   Scéna hlavního okna
     */
    public void setSceneController(SceneController sceneController)
    {
        this.sceneController = sceneController;
    }

    /**
     * Nastaví stage
     * @param stage stage
     */
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    
    
}
