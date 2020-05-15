/**
 *
 * @author Tomas Julina (xjulin08)
 * @author Tomas Kantor (xkanto14)
 *
 */

import gui.SceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * Vytvoreni okna a sceny
 *
 */
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

	    Scene scene = new Scene(root, 1280, 800);
	    scene.getStylesheets().add("/style.css");
	    scene.setFill(Color.BLACK);
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("Bus Tracker");
	    primaryStage.setMinWidth(400);
	    primaryStage.setMinHeight(800);

	    primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	        @Override
	        public void handle(WindowEvent t) {
	            Platform.exit();
	            System.exit(0);
	        }
	    });

	    primaryStage.show();
	}
}
