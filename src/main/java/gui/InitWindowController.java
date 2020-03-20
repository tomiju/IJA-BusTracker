package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import map.InputData;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

/**
 * 
 * Ovládání úvodní scény
 * @author Tomáš Julina (xjulin08)
 * @author Tomáš Kantor (xkanto14)
 *
 */
public class InitWindowController implements Initializable {
	
	private Stage stage;
	private InputData data;
	
	
	@FXML
	private void loadFile(ActionEvent event) throws JsonGenerationException, JsonMappingException, IOException //TODO
	{
		FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        
        
        if (file != null) 
        {
        	 try 
        	 {
        		 YAMLFactory factory = new  YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
		         factory.enable(YAMLGenerator.Feature.INDENT_ARRAYS);
		         ObjectMapper mapper = new ObjectMapper(factory);
		         mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		         mapper.registerModule(new JavaTimeModule());
		         mapper.enable(SerializationFeature.INDENT_OUTPUT);
		             
		     	this.data = mapper.readValue(file, InputData.class); // TODO: dá to do try - ošetřit špatný formát vstupu     		
        	 } 
        	 catch (InvalidDefinitionException e)
        	 {
        		 e.printStackTrace();
        		 System.out.println("Wrong input file.");
        	 }     		
        }
        this.stage.close();	
	}
	
	/**
     * Konstruktor úvodního okna.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    /**
     * Nastaví stage.
     * @param stage stage
     */
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }
    
    /**
     * Načte data ze souboru.
     */
    public InputData getData()
    {
    	return this.data;
    }
    
}
