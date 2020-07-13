package controller;

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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreatePageController{
    static File directory = new File("C:/Users/Dan/MyDocument/haveToDo");

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
            FileWriter writer;
            if(!taskNameField.getText().equals(""))
                writer = new FileWriter(directory + "/" + taskNameField.getText() + ".json");
            else
                writer = new FileWriter(directory + "/" + "Task" + ".json");
            writer.write(obj.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        backEvent(event);
    }
}
