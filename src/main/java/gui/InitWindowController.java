package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import map.InputData;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

/**
 *
 * Ovladani uvodni sceny
 * @author Tomas Julina (xjulin08)
 * @author Tomas Kantor (xkanto14)
 *
 */
public class InitWindowController implements Initializable {

	private Stage stage;
	private InputData data;

	@FXML
	private Button btnLoadMap;

	@FXML
	private void loadFile(ActionEvent event) throws JsonGenerationException, JsonMappingException, IOException
	{
	    btnLoadMap.setDisable(true);
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

		     	 this.data = mapper.readValue(file, InputData.class);
        	 }
        	 catch (Exception e)
        	 {
        		 //e.printStackTrace(); // debug
     	   		 Alert alert = new Alert(Alert.AlertType.ERROR);
    			 alert.setTitle("Application setup error");
    			 alert.setHeaderText("Error\nSomething is was wrong with input data");
    			 alert.setContentText("It is possible that you didnt choose any input file\nor the file you have chosen has incorrect format.");
    			 alert.showAndWait();
 	             Platform.exit();
 	             System.exit(0);
        	 }
        }
        this.stage.close();
	}

	/**
     * Konstruktor uvodniho okna.
     * @param location location
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    /**
     * Nastavi stage.
     * @param stage stage
     */
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    /**
     * Nacte data ze souboru.
     * @return InputData zpracovane vstupni data
     */
    public InputData getData()
    {
    	return this.data;
    }
}
