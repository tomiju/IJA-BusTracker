package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import map.Coordinate;
import map.InputData;
import map.BusLine;
import map.Stop;
import map.Street;
import map.StreetMap;
import map.Timetable;
import map.TimetableEntry;
import map.Vehicle;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

public class InitWindowController implements Initializable {
	
	private Stage stage;
	private SceneController sceneController;
	private InputData data;
	
	
	@FXML
	private void loadFile(ActionEvent event) throws JsonGenerationException, JsonMappingException, IOException //TODO
	{
		FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        
        
        if (file != null) 
        {
        	 try {
		        	 
		        	 /*List<Stop> stop_list = Arrays.asList(
		     				new Stop("stop1", Coordinate.create(5, 7)),
		     				new Stop("stop2", Coordinate.create(55, 71))
		     			);
		     		
		     		List<Street> street_list = Arrays.asList(
		     				new Street("street1", Coordinate.create(10, 20), Coordinate.create(15, 25), Arrays.asList(stop_list.get(0), stop_list.get(1))),
		     				new Street("street2", Coordinate.create(10, 20), Coordinate.create(15, 25), Arrays.asList(stop_list.get(0), stop_list.get(1)))
		     			);
		     		
		     		BusLine test = new BusLine("line1", street_list);
		     		
		     		List<Vehicle> vehicle_list = Arrays.asList(
		     				new Vehicle(new Timetable(Arrays.asList(new TimetableEntry(stop_list.get(0), "00:10:00"), new TimetableEntry(stop_list.get(1), "00:12:00"))), test),
		     				new Vehicle(new Timetable(Arrays.asList(new TimetableEntry(stop_list.get(0), "00:50:00"), new TimetableEntry(stop_list.get(1), "00:09:00"))), test)
		     			);
		     		
		     		List<BusLine> line_list = Arrays.asList(
		     				test,
		     				test
		     			);
		     		
		     		this.data = new InputData(stop_list, street_list, vehicle_list, line_list);*/
		     		
		     		//ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		     		YAMLFactory factory = new  YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
		             factory.enable(YAMLGenerator.Feature.INDENT_ARRAYS);
		             ObjectMapper mapper = new ObjectMapper(factory);
		             mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		             mapper.registerModule(new JavaTimeModule());
		             mapper.enable(SerializationFeature.INDENT_OUTPUT);
		     		
		     		//mapper.writeValue(new File("D:\\Install\\OneDrive\\School\\ŠKOLA\\FIT VUT - Brno\\2. ročník\\2. semestr\\IJA\\PROJEKTY\\PROJEKT\\orderOutput.yaml"), data);
		     		//String yaml = mapper.writeValueAsString(map);
		     		//System.out.println(yaml);
		     		
		     		
		     		/////////////////////////////////////
		     		
		     		//ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		     		//File file = new File(classLoader.getResource("orderOutput.yaml").getFile());
		
		            // vstup: D:\Install\OneDrive\School\ŠKOLA\FIT VUT - Brno\2. ročník\2. semestr\IJA\PROJEKTY\PROJEKT\IJA\src
		     		this.data = mapper.readValue(file, InputData.class);
		     		
		     		/*System.out.println("test: ");
		     		System.out.println(this.data.getVehicles().get(0).getId());
		     		System.out.println(this.data.getLines().get(0).getStart().getId());
		     		System.out.println(this.data.getVehicles().get(0).getLine().getStart().getCoordinate().getX());*/		     		
        	 } 
        	 catch (InvalidDefinitionException e)
        	 {
        		 System.out.println("Wrong input file.");
        		 e.printStackTrace();       		 
        	 }     		
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
    
    public InputData getData()
    {
    	return this.data;
    }
    
}
