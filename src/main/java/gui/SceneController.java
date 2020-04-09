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
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
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
	
	@FXML
	private Button btnPause;
	
	@FXML
	private ToggleButton btnEditMode;
	
	@FXML
	private Label lblEditMode;
	
	@FXML
	private Button focusReset;
	
	/**
	 * Funkce se postara o zapnuti edit mode - pozastavi simulaci a prepne mod
	 * @param event event
	 */
	@FXML
	private void handleEditMode(ActionEvent event)
	{
		// TODO: pred editem si nekde zalohovat puvodni trasu kvuli restartu!!!!!!!!!!!!!!!!!!!!!!!
		if(btnEditMode.isSelected())
		{
			lblEditMode.setVisible(true);
			
			for (Vehicle vehicle : SceneController.data.getVehicles()) 
			{
				vehicle.pauseVehicle();
			}
			
			btnPause.setText("Resume");
			btnPause.setMouseTransparent(true);
			focusReset.setMouseTransparent(true);
			btnSaveTimeSpeed.setMouseTransparent(true);
			txtTimeSpeed.setMouseTransparent(true);
			
			
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
			
		    this.lineList.getSelectionModel().clearSelection();
		    this.streetList.getSelectionModel().clearSelection();
			
			vehicleList.getItems().clear();
			
			txtVehicleName.setText("");
		}
		else
		{
			lblEditMode.setVisible(false);
			
			for (Vehicle vehicle : SceneController.data.getVehicles()) 
			{
				vehicle.resumeVehicle();
			}
			
			btnPause.setText("Pause");
			btnPause.setMouseTransparent(false);
			focusReset.setMouseTransparent(false);
			btnSaveTimeSpeed.setMouseTransparent(false);
			txtTimeSpeed.setMouseTransparent(false);
			
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
			
		    this.lineList.getSelectionModel().clearSelection();
			
			vehicleList.getItems().clear();
			
			txtVehicleName.setText("");
			
			for (Street street : SceneController.data.getStreets()) // zmeni barvu otevrenych ulic na cernou - zavrene zustanou cervene
			{
				if (street.getStatus())
				{
					street.getStreetView().getLine().setStroke(Color.BLACK);
				}
			}
		}
	}
	
	/**
	 * Pozastaví/spustí simulaci
	 * @param event event
	 */
	@FXML
	private void pauseSimulation(ActionEvent event)
	{
		if (btnPause.getText().equals("Pause"))
		{
			for (Vehicle vehicle : SceneController.data.getVehicles()) 
			{
				vehicle.pauseVehicle();
			}
			btnPause.setText("Resume");
		}
		else
		{
			for (Vehicle vehicle : SceneController.data.getVehicles()) 
			{
				vehicle.resumeVehicle();
			}
			btnPause.setText("Pause");
		}
	}
	
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
					System.out.println("speed: " + txtTimeSpeed.getText()) ;
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
					System.out.println("speed: " + txtTimeSpeed.getText()) ;
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
		if (((Integer.parseInt(this.txtTimer.getText().substring(3,5)) + (Integer.parseInt(this.txtTimer.getText().substring(0,2)) * 60))) >= 1)
		{
			this.mainClock.cancel();
			
			btnPause.setText("Pause");
			btnEditMode.setSelected(false);
			lblEditMode.setVisible(false);
			
			for (Vehicle vehicle : SceneController.data.getVehicles()) 
			{	
				vehicle.cancelVehicle();
				
				vehicle.setCurrentPosition(vehicle.getLine().getStreets().get(0).getStart()); // nastaveni pocatecni pozice auta
				vehicle.setCurrentStreet(vehicle.getLine().getStart().getStreet()); // nastaveni pocatecni ulice

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
			
			for (Street street : SceneController.data.getStreets())
			{
				if (!street.getStatus())
				{
					street.setOpen(true);
				}
				street.getStreetView().getLine().setStroke(Color.BLACK);
				
				street.getStreetView().getLine().setMouseTransparent(true); // nejde editovat trasa
 				street.getStreetView().getName().setMouseTransparent(true);
			}
					
			vehicleList.getItems().clear();	
			txtVehicleName.setText("");
			
		    this.lineList.getSelectionModel().clearSelection();
		    
		    if (!this.streetList.getSelectionModel().isEmpty()) // error s indexem -1 kdyz se pokousim resetovat focus prazdneho seznamu!!!
		    {
		    	this.streetList.getSelectionModel().clearSelection();
		    }
			
			drive();
		}
		
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
					street.getStreetView().getLine().setMouseTransparent(true); // nejde editovat trasa
 					street.getStreetView().getName().setMouseTransparent(true);
					streetList.getItems().remove(street.getId());
				}
			}
		}
		
	    this.lineList.getSelectionModel().clearSelection();
		
		vehicleList.getItems().clear();
		
		txtVehicleName.setText("");
	}
	
	/**
     * Konstruktor hlavniho okna.
     * @param location location
     * @param resources resources
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
        
        this.lineList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() { // naplneni seznamu jmeny ulic
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
            	
            	if(!btnEditMode.isSelected())
            	{
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
            	else
            	{
            		System.out.println("TODO: edit mode #DEBUG (line 449, SceneController)"); // # DEBUG
            		
            		for (Street street : gui.SceneController.data.getStreets())
            		{
    					street.getStreetView().getLine().setMouseTransparent(false); // jde editovat trasa
     					street.getStreetView().getName().setMouseTransparent(false);
            		}
            		
            		for (BusLine line : gui.SceneController.data.getLines()) 
            		{
                		if (line != null)
            			{
                			if(line.getId() == oldValue)
                			{
                				line.unsetLineFocus(map);
                				
                				for (Street street : gui.SceneController.data.getStreets()) 
                				{
                					if (street.isOpen())
                					{
                						street.getStreetView().getLine().setStroke(Color.BLACK);
                					}
                				}
                				
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
            	
            }
        });
        
        this.streetList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() { // uzavirani ulic
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
            	// TODO: pouze v edit mode!
            	// když kliknu na nějakou ulici, nastaví se červená a status "uzavřeno", když na ni kliknu znova, tak se otevře
            	if(btnEditMode.isSelected())
            	{
            		for (Street street : gui.SceneController.data.getStreets())
            		{
            			if (street.getId() == newValue)
            			{
            				Drawable.setStreetStatus(street, street.getStreetView().getLine(), btnEditMode.isSelected());
            			}
            			
            			if(oldValue == newValue) // opetovne kliknuti ulici otevre
            			{
            				Drawable.setStreetStatus(street, street.getStreetView().getLine(), btnEditMode.isSelected());
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
			
			// TODO: toto bude pro naklikávání trasy! uzavření ulice přes "list ulic"
			street.getStreetView().getName().setOnMouseClicked(e -> Drawable.setNewPath(street, street.getStreetView().getLine(), btnEditMode.isSelected()));
	        street.getStreetView().getLine().setOnMouseClicked(e -> Drawable.setNewPath(street, street.getStreetView().getLine(), btnEditMode.isSelected()));
	        
	        street.getStreetView().getLine().setMouseTransparent(true);
	        street.getStreetView().getName().setMouseTransparent(true);
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
			vehicle.setCurrentPosition(vehicle.getLine().getStreets().get(0).getStart()); // nastaveni pocatecni pozice auta
			vehicle.setCurrentStreet(vehicle.getLine().getStart().getStreet()); // nastaveni pocatecni ulice
			vehicle.addVehicleToLine();
			Drawable.drawVehicles(vehicle, map);
			
			vehicle.getVehicleView().getCircle().setOnMouseClicked(e -> showVehicleInfo(vehicle)); // TODO: info o spoji
			vehicle.getVehicleView().getText().setOnMouseClicked(e -> showVehicleInfo(vehicle));
		}
	}
	
	/**
	 * Zobrazi informace o nakliknutem vozidle
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
	
	/**
	 * Vrati jmeno aktualne nakliknuteho vozidla
	 * @return String jmeno aktualne nakliknuteho vozidla
	 */
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
				if(btnPause.getText().equals("Pause") && !btnEditMode.isSelected())
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
			}
		}, 0, (int)(1000 / timeSpeed));   
	}

}
