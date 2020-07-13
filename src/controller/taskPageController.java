package controller;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class taskPageController implements Initializable {
    static File directory = sampleController.currentDirectory;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField taskNameField;

    @FXML
    private Button saveButton;

    @FXML
    private Button backButton;

    @FXML
    private TextArea taskTextArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //чтение выделенного файла
        taskNameField.setText(sampleController.currentNameOFTask);
        FileReader reader;
        try {
            reader = new FileReader(directory + "/" + sampleController.currentNameOFTask + ".json");

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            taskTextArea.setText((String) jsonObject.get("text"));
            reader.close();
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backEvent(ActionEvent event) throws IOException {
        //возврат к sample
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../sample/sample.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void saveEvent(ActionEvent event) throws IOException {
        // сохранение задачи в json формате
        JSONObject obj = new JSONObject();
        obj.put("text",taskTextArea.getText());
        try{
            FileWriter writer = new FileWriter(directory + "/" + taskNameField.getText() + ".json");
            writer.write(obj.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        backEvent(event);
    }
}