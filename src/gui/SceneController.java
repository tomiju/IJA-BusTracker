package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController implements Initializable{

	// Funkce otevře úvodní okno - z tama vybereme vstup -> podle vstupu se nastaví instance třídy a po zavření okna se vykreslí mapa v hlavním okně
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		Stage owStage = new Stage();
        FXMLLoader owLoader = new FXMLLoader();
        Parent opWindow = null;
        Scene ow = null;

        try {
            opWindow = owLoader.load(getClass().getResource("openingWindow.fxml").openStream());
            ow = new Scene(opWindow);
        } catch (IOException e) {
            e.printStackTrace();
        }


        owStage.setTitle("Bus tracker");
        owStage.setScene(ow);
        owStage.showAndWait();

        init();
		
	}
	
	public void init()
	{
		
	}

}
