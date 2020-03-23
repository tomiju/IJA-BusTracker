package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import map.BusLine;
import map.InputData;
import map.Stop;
import map.Street;
import map.Vehicle;

/**
 * 
 * Ovladani scen.
 * @author Tomas Julina (xjulin08)
 * @author Tomas Kantor (xkanto14)
 *
 */
public class SceneController implements Initializable {
	private static InputData data;
	private LocalTime localTime = LocalTime.of(23, 59, 0, 0);
	
	@FXML
    private Pane map;
	
	@FXML
	private ListView<String> lineList;
	
	@FXML
	private Group scroll;
	
	@FXML
	private Text txtTimer;
	
	/**
     * Ovlada zoom, meni scale "mapy".
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
	
	// opetovne resetuje focus na click
	@FXML 
	public void handleMouseClick(MouseEvent arg0) 
	{
	    for (BusLine line : gui.SceneController.data.getLines()) 
		{
    		if (line != null)
			{
    			if(line.getId() == this.lineList.getSelectionModel().getSelectedItem())
        		{
        			 line.setLineFocus(map);
        		}
			}	
		}
	}
	
	/**
     * Resetuje barvu ulice (zrusi focus).
     * @param event event
     */
	@FXML
	private void handleFocusReset(ActionEvent event)
	{
		for (BusLine line : SceneController.data.getLines()) 
		{
			line.unsetLineFocus(map);
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
        owLoader.setController(new InitWindowController());
        owLoader.setLocation(getClass().getResource("/initWindow.fxml")); // TODO: Pri odevzdani jen "/initWindow.fxml" + jina struktura souboru
        Parent opWindow = null;
        Scene ow = null;

        try 
        {
        	opWindow = owLoader.load();
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
        SceneController.data = iwController.getData(); // vstupni data
        
        this.lineList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
            	
            	for (BusLine line : gui.SceneController.data.getLines()) 
        		{
            		if (line != null)
        			{
            			if(line.getId() == oldValue)
            			{
            				line.unsetLineFocus(map);
            			}
            			if(line.getId() == newValue)
                		{
                			 line.setLineFocus(map);
                		}
        			}	
        		}
            }
        });
        
        /*this.lineList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + this.lineList.getSelectionModel().getSelectedItem());
            }
        });*/
        
        initMap();
        drive();
	}
	
	/**
     * Vykresli mapu a jeji prvky.
     */
	public void initMap()
	{
		for (Street street : SceneController.data.getStreets()) 
		{
			Drawable.drawStreets(street, map);
			street.setStreetParameterToStop();
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
			vehicle.addVehicleToLine();
			
			Drawable.drawVehicles(vehicle, map);
		}
	}
	
	public void drive() // TODO: rychlost simulace + reset simulace
	{
		Timer timer = new Timer();
		
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run()
			{
				localTime = localTime.plusMinutes(1);
				txtTimer.setText(localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
				
				for (Vehicle vehicle : SceneController.data.getVehicles()) 
				{
					vehicle.drive(txtTimer.getText());
				}
			}
		}, 0, (int)1000);   
	}

}
