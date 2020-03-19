package gui;


/**
 * 
 * Vytvoření okna a scény
 * @author Tomáš Julina (xjulin08)
 * @author Tomáš Kantor (xkanto14)
 *
 */

import gui.SceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
 
public class Main extends Application 
{
	public static void main(String[] args) 
	{
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception 
	{
		FXMLLoader loader = new FXMLLoader();
        loader.setController(new SceneController());
        loader.setLocation(getClass().getResource("/scene.fxml"));
		Parent root = loader.load();
		
	    Scene scene = new Scene(root, 1000, 700);
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("Bus tracker");
	    
	    primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	        @Override
	        public void handle(WindowEvent t) {
	            Platform.exit();
	            System.exit(0);
	        }
	    });

		/*root.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
		  @Override
		  public void handle(MouseEvent event) {
		    System.out.println(event.getScreenX());
		    System.out.println(event.getSource().toString());
		  }
		});*/
	    
	    primaryStage.show();

	}
}
