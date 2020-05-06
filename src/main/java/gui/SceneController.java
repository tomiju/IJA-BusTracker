package gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
	private ArrayList<BusLine> dataBackup = new ArrayList<BusLine>();
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
	private Label lblVehicleList;
	
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
	 * Funkce se postara o zapnuti/vypnuti edit mode - pozastavi simulaci a prepne mod.
	 * @param event event
	 */
	@FXML
	private void handleEditMode(ActionEvent event)
	{
		if(btnEditMode.isSelected())
		{
			//debug - vypise aktualni pozici prvniho vozidla (po zastaveni animace)
			/*boolean test = true;
			for (Vehicle vehicle : SceneController.data.getVehicles()) 
			{
				if(test)
				{
					System.out.println("Vehicle: " + vehicle.getId() + " X: "+ vehicle.getVehicleView().getCircle().translateXProperty());
					System.out.println("Vehicle: " + vehicle.getId() + " Y: "+ vehicle.getVehicleView().getCircle().translateYProperty());
					
					Circle circle = new Circle();
					
					circle.setRadius(8.0f);
					circle.setFill(Color.PINK);
					circle.setCenterX(vehicle.getVehicleView().getCircle().getCenterX() + vehicle.getVehicleView().getCircle().getTranslateX());
					circle.setCenterY(vehicle.getVehicleView().getCircle().getCenterY() + vehicle.getVehicleView().getCircle().getTranslateY());
					test = false;
					map.getChildren().add(circle);
					
					// aktualizace souradnic
					//vehicle.setCurrentPosition(new Coordinate(vehicle.getVehicleView().getCircle().getCenterX() + vehicle.getVehicleView().getCircle().getTranslateX(), vehicle.getVehicleView().getCircle().getCenterY() + vehicle.getVehicleView().getCircle().getTranslateY()));
				
					System.out.println("Current position: X" + vehicle.getCurrentPosition().getX() + " Y " + vehicle.getCurrentPosition().getY());
				}
			}*/
			
			
			lblEditMode.setVisible(true);
			lblVehicleList.setVisible(false);
			txtVehicleName.setText("New Path:");
			
			for (Vehicle vehicle : SceneController.data.getVehicles()) 
			{
				vehicle.pauseVehicle();

				vehicle.getVehicleView().getCircle().setMouseTransparent(true);
				vehicle.getVehicleView().getText().setMouseTransparent(true);
			}
			
			btnPause.setText("Resume");
			btnPause.setMouseTransparent(true);
			focusReset.setMouseTransparent(true);
			btnSaveTimeSpeed.setMouseTransparent(true);
			txtTimeSpeed.setMouseTransparent(true);
			streetList.setMouseTransparent(false);
					
			for (BusLine line : SceneController.data.getLines()) 
			{
				line.unsetLineFocus();
				
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
		}
		else
		{
			// kontrola, ze pri pokusu o opusteni edit mode jsou nastaveny vsechny editovane cesty
			for (BusLine line : SceneController.data.getLines()) 
			{
				if (line.getStreets().isEmpty())
				{
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setTitle("Edit mode error");
					alert.setHeaderText("Warning\nPath not set for line:  " + line.getId());
					alert.setContentText("You need to create new path if you have closed street that belonged to some bus line.\nPath can be created by clicking on the streets in map.");
					alert.show();
					
					btnEditMode.setSelected(true);
					return;
				}
			}
			
			streetList.setMouseTransparent(true);
			lblEditMode.setVisible(false);
			lblVehicleList.setVisible(true);
			
			for (Vehicle vehicle : SceneController.data.getVehicles()) 
			{
				vehicle.resumeVehicle();
				

				vehicle.getVehicleView().getCircle().setMouseTransparent(false);
				vehicle.getVehicleView().getText().setMouseTransparent(false);
			}
			
			btnPause.setText("Pause");
			btnPause.setMouseTransparent(false);
			focusReset.setMouseTransparent(false);
			btnSaveTimeSpeed.setMouseTransparent(false);
			txtTimeSpeed.setMouseTransparent(false);
			
			for (BusLine line : SceneController.data.getLines()) 
			{
				line.unsetLineFocus();
				
				for (Street street : line.getStreets())
				{
					if (street != null)
					{
						streetList.getItems().remove(street.getId());
					}
				}
				
				if(line.isEdited())
				{
					for (Vehicle vehicle : line.getVehicles())
					{
						vehicle.getVehicleView().getCircle().setVisible(true);
						vehicle.getVehicleView().getText().setVisible(true);
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
					street.getStreetView().getLine().setStroke(Color.GREY);
				}
			}
		}
	}
	
	/**
	 * Pozastavi/spusti simulaci
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
				if (!txtTimeSpeed.getText().isEmpty() && Integer.parseInt(txtTimeSpeed.getText()) >= 1 && Integer.parseInt(txtTimeSpeed.getText()) <= 5)
				{
					//System.out.println("speed: " + txtTimeSpeed.getText()); // debug
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
			
			btnPause.setMouseTransparent(false);
			focusReset.setMouseTransparent(false);
			btnSaveTimeSpeed.setMouseTransparent(false);
			txtTimeSpeed.setMouseTransparent(false);
			
			for (BusLine line: SceneController.data.getLines())
			{

				line.setEdit(false);
				
				for (BusLine backupLine : this.dataBackup)
				{					
					if (backupLine.getId() == line.getId())
					{
						line.resetSimulation(backupLine.getStreets());
					}
				}					
				
			}
			
			for (Vehicle vehicle : SceneController.data.getVehicles()) 
			{	
				vehicle.cancelVehicle();
				
				vehicle.setCurrentPosition(vehicle.getLine().getStreets().get(0).getStart()); // nastaveni pocatecni pozice auta
				vehicle.setCurrentStreet(vehicle.getLine().getStart().getStreet()); // nastaveni pocatecni ulice

			    vehicle.getVehicleView().getCircle().setCenterX(vehicle.getCurrentPosition().getX());
			    vehicle.getVehicleView().getCircle().setCenterY(vehicle.getCurrentPosition().getY());
				
				vehicle.getVehicleView().getText().xProperty().bind(vehicle.getVehicleView().getCircle().centerXProperty().add(7));
				vehicle.getVehicleView().getText().yProperty().bind(vehicle.getVehicleView().getCircle().centerYProperty());
				
				vehicle.getVehicleView().getCircle().setFill(Color.BLUE);

			    vehicle.resetIndex();
			    vehicle.getTimetable().resetIndex();
			    vehicle.resetVehiclePath();
			    
			    vehicle.getVehicleView().getCircle().setVisible(false);
			    vehicle.getVehicleView().getText().setVisible(false);
			    
			    this.localTime = LocalTime.of(23, 59, 0, 0);
				this.txtTimer.setText(localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));	
			}
			
			for (Street street : SceneController.data.getStreets())
			{
				if (!street.getStatus())
				{
					street.setOpen(true);
				}
				street.getStreetView().getLine().setStroke(Color.GREY);
				
				street.getStreetView().getLine().setMouseTransparent(true); // nejde editovat trasa
 				street.getStreetView().getName().setMouseTransparent(true);
			}
					
			txtVehicleName.setText("");
			
			if (!this.vehicleList.getItems().isEmpty())
			{
				vehicleList.getItems().clear();					
			}
			
			if (!this.lineList.getSelectionModel().isEmpty()) // error s indexem -1 kdyz se pokousim resetovat focus prazdneho seznamu!!!
		    {
				lineList.getSelectionModel().clearSelection();
		    }
		    
		    if (!this.streetList.getSelectionModel().isEmpty()) // error s indexem -1 kdyz se pokousim resetovat focus prazdneho seznamu!!!
		    {
		    	streetList.getSelectionModel().clearSelection();
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
        			 line.setLineFocus();
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
			line.unsetLineFocus();
			
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
		
		if (!this.lineList.getSelectionModel().isEmpty()) // error s indexem -1 kdyz se pokousim resetovat focus prazdneho seznamu!!!
	    {
			this.lineList.getSelectionModel().clearSelection();
	    }
		
		if (!this.vehicleList.getItems().isEmpty()) // error s indexem -1 kdyz se pokousim resetovat focus prazdneho seznamu!!!
	    {
			vehicleList.getItems().clear();	    
	    }
		
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
            ow.getStylesheets().add("/style.css");
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        InitWindowController iwController = (InitWindowController) owLoader.getController();

        owStage.setTitle("Bus Tracker");
        owStage.setScene(ow);
        owStage.setResizable(false);
        iwController.setStage(owStage);
        owStage.showAndWait();
        
        SceneController.data = iwController.getData(); // vstupni data
        
        try
        {
        	SceneController.data.getLines();
        	SceneController.data.getStops();
        	SceneController.data.getStreets();
        	SceneController.data.getVehicles();
        }
        catch(Exception e)
        {
	   		e.printStackTrace();
	   		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Application setup error");
			alert.setHeaderText("Error\nSomething is was wrong with input data");
			alert.setContentText("It is possible that you didnt choose any input file\nor the file you have chosen has incorrect format.");
			alert.showAndWait();
            Platform.exit();
            System.exit(0);
        }
        
        // vytvori hlubokou kopii stavu linek pred editaci (nutne pro restart editace)
        //this.dataBackup = new ArrayList<BusLine>();
        for (BusLine line : SceneController.data.getLines())
        {
        	try 
        	{
				this.dataBackup.add((BusLine)line.clone());
			} 
        	catch (CloneNotSupportedException e) 
        	{
				e.printStackTrace();
			}
        }
        
        this.lineList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() { // naplneni seznamu jmeny ulic
            @Override
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
            	
            	if(!btnEditMode.isSelected())
            	{
            		if(!streetList.getItems().isEmpty())
            		{
            			streetList.getItems().clear();            			
            		}
            		for (BusLine line : gui.SceneController.data.getLines()) 
            		{
                		if (line != null)
            			{
                			if(line.getId() == oldValue)
                			{
                				line.unsetLineFocus();
                				
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
                    			 line.setLineFocus();
                    			 
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
            		if(!streetList.getItems().isEmpty())
            		{
            			streetList.getItems().clear();
            		}
            		
            		if(!vehicleList.getItems().isEmpty())
            		{
            			vehicleList.getItems().clear();            			
            		}
            		
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
                				line.unsetLineFocus();
                				
                				for (Street street : gui.SceneController.data.getStreets()) 
                				{
                					if (street.isOpen())
                					{
                						street.getStreetView().getLine().setStroke(Color.GREY);
                					}
                				}
                			}
                			
                			if(line.getId() == newValue)
                    		{
                    			 line.setLineFocus();
                    			 
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

            	if(btnEditMode.isSelected())
            	{
            		for (Street street : gui.SceneController.data.getStreets())
            		{
            			if (street.getId() == newValue)
            			{
            				Drawable.setStreetStatus(street, street.getStreetView().getLine(), btnEditMode.isSelected(), gui.SceneController.data.getLines());
            			}
            			
            			if(oldValue == newValue) // opetovne kliknuti ulici otevre
            			{
            				Drawable.setStreetStatus(street, street.getStreetView().getLine(), btnEditMode.isSelected(), gui.SceneController.data.getLines());
            			}
            		}
            	}
            	else
            	{
            		newValue = "";
            		oldValue = "";
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
			
			street.getStreetView().getName().setOnMouseClicked(e -> Drawable.setNewPath(street, street.getStreetView().getLine(), btnEditMode.isSelected(), vehicleList, this.lineList.getSelectionModel().getSelectedItem(), SceneController.data.getLines()));
	        street.getStreetView().getLine().setOnMouseClicked(e -> Drawable.setNewPath(street, street.getStreetView().getLine(), btnEditMode.isSelected(), vehicleList, this.lineList.getSelectionModel().getSelectedItem(), SceneController.data.getLines()));
	        
	        street.getStreetView().getLine().setMouseTransparent(true); // nejde editovat mimo editacni mod
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
			
			vehicle.getVehicleView().getCircle().setOnMouseClicked(e -> showVehicleInfo(vehicle));
			vehicle.getVehicleView().getText().setOnMouseClicked(e -> showVehicleInfo(vehicle));
		}
	}
	
	/**
	 * Zobrazi informace o nakliknutem vozidle
	 * @param vehicle aktivni vozidlo
	 */
	public void showVehicleInfo(Vehicle vehicle)
	{
		for (Vehicle vehicle1 : SceneController.data.getVehicles()) // smaze se focus z predchoziho vozidla
		{
			if(vehicle1.getId().equals(txtVehicleName.getText()) && !vehicle1.getId().equals(vehicle.getId()))
			{
				ArrayList<BusLine> pom = new ArrayList<BusLine>();
				pom.add(vehicle1.getLine());
				Drawable.resetColors(pom);
			}
		}
		
		if(!vehicleList.getItems().isEmpty())
		{
			vehicleList.getItems().clear();			
		}
		
		ArrayList<BusLine> pom = new ArrayList<BusLine>();
		pom.add(vehicle.getLine());
		Drawable.resetColors(pom);
		
		vehicle.getLine().setVehicleInfoFocus();
		vehicle.getVehicleView().getCircle().setFill(Color.ORANGE);
		
		txtVehicleName.setText(vehicle.getId());
		
		for(TimetableEntry entry : vehicle.getTimetable().getEntries())
		{
			if(vehicle.getLine().isEdited())
			{
				boolean isItInList = false;
				
				for(TimetableEntry pom1 : vehicle.getEditedPathStops())
				{
					if(pom1.getStop().getId() == entry.getStop().getId())
					{
						isItInList = true;
						break;
					}
				}
				
				if(isItInList)
				{
					vehicleList.getItems().add(entry.getStop().getId().concat("\n" + entry.getTime()+"\n(+" + vehicle.getDelay() + " min.)"));
				}
				else
				{
					vehicleList.getItems().add(entry.getStop().getId().concat("\n" + entry.getTime()+"\n(SKIPPED)"));
				}
			}
			else
			{
				vehicleList.getItems().add(entry.getStop().getId().concat("\n" + entry.getTime()+"\n(+" + vehicle.getDelay() + " min.)"));				
			}
		}
		
		int pomIndex = 0;
		for	(String item : vehicleList.getItems())
		{
			if(!vehicle.isFinished())
			{
				if(item.equals(vehicle.getCurrentStopId()))
				{
					vehicleList.getSelectionModel().select(pomIndex);
				}
				pomIndex++;				
			}
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
				Platform.runLater(() -> {
					if(btnPause.getText().equals("Pause") && !btnEditMode.isSelected())
					{				
						localTime = localTime.plusMinutes(1);
						
							txtTimer.setText(localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
								
						for (Vehicle vehicle : SceneController.data.getVehicles()) 
						{
							if(vehicle != null)
							{
								vehicle.drive(txtTimer.getText(), timeSpeed);
								
								// automaticka aktualizace nasledujici zastavky (pro nakliknute vozidlo)
									if (getCurrentVehicleId() == vehicle.getId())
									{
										showVehicleInfo(vehicle);
									}						
							}
						}
					}
				});	
			}
		}, 0, (int)(1000 / timeSpeed));   
	}

}
