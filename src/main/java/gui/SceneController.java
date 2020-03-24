package gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

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
import map.Street;
import map.TimetableEntry;
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
	private int timeSpeed = 1;
	private Timer mainClock;
	
	@FXML
    private Pane map;
	
	@FXML
	private ListView<String> lineList;
	
	@FXML
	private ListView<String> streetList;
	
	@FXML
	private ListView<String> vehicleList;
	
	@FXML
	private Group scroll;
	
	@FXML
	private Text txtTimer;
	
	@FXML
	private Text txtVehicleName;
	
	@FXML
	private Button btnReset;
	
	@FXML
	private TextField txtTimeSpeed;
	
	@FXML
	private Button btnSaveTimeSpeed;
	
	/**
	 * Zmeni rychlost simulace.
	 * @param event event
	 */
	@FXML
	private void handleSimulationSpeedBtn(ActionEvent event)
	{
		if (txtTimeSpeed.getText() != "0")
		{
			if(txtTimeSpeed.getText() == "")
			{
				timeSpeed = 1;
			}
			else
			{	
				if (Integer.parseInt(txtTimeSpeed.getText()) >= 1 && Integer.parseInt(txtTimeSpeed.getText()) <= 5)
				{
					System.out.println("test: " + txtTimeSpeed.getText()) ;
					timeSpeed = Integer.parseInt(txtTimeSpeed.getText());
				}
				else
				{
					timeSpeed = 1;
				}	
			}
		}
		else
		{
			timeSpeed = 1;
		}
	}
	
	/**
	 * Zmeni rychlost simulace.
	 * @param ae event
	 */
	@FXML
	private void onEnter(ActionEvent ae)
	{
		if (txtTimeSpeed.getText() != "0")
		{
			if(txtTimeSpeed.getText() == "")
			{
				timeSpeed = 1;
			}
			else
			{	
				if (Integer.parseInt(txtTimeSpeed.getText()) >= 1 && Integer.parseInt(txtTimeSpeed.getText()) <= 5)
				{
					System.out.println("test: " + txtTimeSpeed.getText()) ;
					timeSpeed = Integer.parseInt(txtTimeSpeed.getText());
				}
				else
				{
					timeSpeed = 1;
				}	
			}
		}
		else
		{
			timeSpeed = 1;
		}
	}
	
	/**
	 * Restartuje simulaci.
	 * @param event event
	 */
	@FXML
	private void handleSimulationReset(ActionEvent event)
	{
		this.mainClock.cancel();
		
		for (Vehicle vehicle : SceneController.data.getVehicles()) 
		{			
			vehicle.setCurrentPosition(vehicle.getLine().getStreets().get(0).getStart()); // nastavení počáteční pozice auta
			vehicle.setCurrentStreet(vehicle.getLine().getStart().getStreet()); // nastavení počáteční ulice

		    vehicle.getVehicleView().getCircle().setCenterX(vehicle.getCurrentPosition().getX());
		    vehicle.getVehicleView().getCircle().setCenterY(vehicle.getCurrentPosition().getY());
			
			vehicle.getVehicleView().getText().xProperty().bind(vehicle.getVehicleView().getCircle().centerXProperty().add(7));
			vehicle.getVehicleView().getText().yProperty().bind(vehicle.getVehicleView().getCircle().centerYProperty());

		    vehicle.resetIndex();
		    vehicle.getTimetable().resetIndex();
		    vehicle.resetVehiclePath();
		    
		    this.localTime = LocalTime.of(23, 59, 0, 0);
			this.txtTimer.setText(localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));	
		}
				
		vehicleList.getItems().clear();	
		txtVehicleName.setText("");
		
		drive();
	}
	
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
	
	/**
	 * Resetuje zvyraznenou linku.
	 * @param arg0 event
	 */
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
			
			for (Street street : line.getStreets())
			{
				if (street != null)
				{
					streetList.getItems().remove(street.getId());
				}
			}
		}
		
		vehicleList.getItems().clear();
		
		txtVehicleName.setText("");
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
        
        timeSpeed = 1;

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
        owStage.setResizable(false);
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
            				
            				for (Street street : line.getStreets())
                			{
                				if (street != null)
                				{
                					streetList.getItems().remove(street.getId());
                				}
                			}
            			}
            			
            			if(line.getId() == newValue)
                		{
                			 line.setLineFocus(map);
                			 
                			 for (Street street : line.getStreets())
                 			{
                 				if (street != null)
                 				{
                 					streetList.getItems().add(street.getId());
                 				}
                 			}
                		}
        			}
        		}
            }
        });
        
        // kontroluje vstup u nastaveni rychlosti simulace - jen cisla
        txtTimeSpeed.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
                String newValue) {
                if (!newValue.matches("\\d*")) {
                	txtTimeSpeed.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
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
			
			vehicle.getVehicleView().getCircle().setOnMouseClicked(e -> showVehicleInfo(vehicle)); // TODO: info o spoji
			vehicle.getVehicleView().getText().setOnMouseClicked(e -> showVehicleInfo(vehicle));
		}
	}
	
	/**
	 * Zobrazí informace o nakliknutem vozidle
	 * @param vehicle aktivni vozidlo
	 */
	public void showVehicleInfo(Vehicle vehicle)
	{
		vehicleList.getItems().clear();
		
		txtVehicleName.setText(vehicle.getId());
		
		for(TimetableEntry entry : vehicle.getTimetable().getEntries())
		{
			vehicleList.getItems().add(entry.getStop().getId().concat("\n" + entry.getTime()));
		}
		
		int pomIndex = 0;
		for	(String item : vehicleList.getItems())
		{
			if(item.equals(vehicle.getCurrentStopId()))
			{
				vehicleList.getSelectionModel().select(pomIndex);
			}
			pomIndex++;
		}

	}
	
	public String getCurrentVehicleId()
	{
		return txtVehicleName.getText();
	}
	
	/**
	 * Spusti animaci pohybu vozidel
	 */
	public void drive()
	{
		Timer timer = new Timer();
		
		this.mainClock = timer;
		
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run()
			{
				localTime = localTime.plusMinutes(1);
				txtTimer.setText(localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
						
				for (Vehicle vehicle : SceneController.data.getVehicles()) 
				{
					vehicle.drive(txtTimer.getText(), timeSpeed);
					
					Platform.runLater(() -> { // automaticka aktualizace nasledujici zastavky (pro nakliknute vozidlo)
						if (getCurrentVehicleId() == vehicle.getId())
						{
							showVehicleInfo(vehicle);
						}
		            });
				}
			}
		}, 0, (int)(1000 / timeSpeed));   
	}

}
