package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import map.Coordinate;
import map.InputData;
import map.Line;
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
		/*FileChooser fileChooser = new FileChooser(); // otevře prohledávání souborů TODO: zpracovat vstupní soubor
        File file = fileChooser.showOpenDialog(null);*/
        /*
        if (file != null) {
        	 System.out.println("You clicked me!");
        }
        this.stage.close();*/
		
		
		
		List<Stop> stop_list = Arrays.asList(
				new Stop("stop1", Coordinate.create(5, 7)),
				new Stop("stop2", Coordinate.create(55, 71))
			);
		
		List<Street> street_list = Arrays.asList(
				new Street("street1", Coordinate.create(10, 20), Coordinate.create(15, 25), Arrays.asList(stop_list.get(0), stop_list.get(1))),
				new Street("street2", Coordinate.create(10, 20), Coordinate.create(15, 25), Arrays.asList(stop_list.get(0), stop_list.get(1)))
			);
		
		Line test = new Line("line1", street_list);
		
		List<Vehicle> vehicle_list = Arrays.asList(
				new Vehicle(new Timetable(Arrays.asList(new TimetableEntry(stop_list.get(0), "00:10:00"), new TimetableEntry(stop_list.get(1), "00:12:00"))), test),
				new Vehicle(new Timetable(Arrays.asList(new TimetableEntry(stop_list.get(0), "00:50:00"), new TimetableEntry(stop_list.get(1), "00:09:00"))), test)
			);
		
		List<Line> line_list = Arrays.asList(
				test,
				test
			);
		
		InputData data = new InputData(stop_list, street_list, vehicle_list, line_list);
		
		//ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		YAMLFactory factory = new  YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        factory.enable(YAMLGenerator.Feature.INDENT_ARRAYS);
        ObjectMapper mapper = new ObjectMapper(factory);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
		
		mapper.writeValue(new File("D:\\Install\\OneDrive\\School\\ŠKOLA\\FIT VUT - Brno\\2. ročník\\2. semestr\\IJA\\PROJEKTY\\PROJEKT\\orderOutput.yaml"), data);
		//String yaml = mapper.writeValueAsString(map);
		//System.out.println(yaml);
		/////////////////////////////////////
		
		
		/*ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		//File file = new File(classLoader.getResource("orderOutput.yaml").getFile());


		// Mapping the employee from the YAML file to the Employee class
		StreetMap map = mapper.readValue(file, StreetMap.class);
		
		System.out.println("test: ");
		System.out.println(map.getStops().get(0).getId());
		System.out.println(map.getStreets().get(0).getId());*/
		
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
