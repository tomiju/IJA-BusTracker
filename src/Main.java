
/**
 * 
 * Vytvoření okna a scény
 * @author Tomáš Julina (xjulin08)
 * @author Tomáš Kantor (xkanto14)
 *
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
public class Main extends Application 
{
	public static void main(String[] args) 
	{
		launch(args);
	}

	public void start(Stage primaryStage) throws Exception 
	{
		Parent root = FXMLLoader.load(getClass().getResource("gui/scene.fxml"));
		
	    Scene scene = new Scene(root, 1000, 700);
	    //scene.getStylesheets().add("gui/mainScene.css");
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("Bus tracker");

	    primaryStage.show();

	}
}
