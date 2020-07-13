package controller;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class sampleController implements Initializable {
    static String nameOFTaskFormHaveToDo = "";
    static String nameOFTaskFormCompleted = "";
    static String currentNameOFTask = "";
    static String path = "C:/Users/Dan/MyDocument"; //путь к папке сохранения документов
    static File directory = new File("C:/Users/Dan/MyDocument");
    static File currentDirectory;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button openButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ListView<String> haveToDoList;

    @FXML
    private Button doneButton;

    @FXML
    private ListView<String> completedList;

    @FXML
    private Button addNewButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //создание списков задач
        ArrayList<String> nameOfTask = new ArrayList<>();
        try{
            File directory1 = new File(path + "/haveToDo");
            directory1.mkdir();
            File directory2 = new File(path + "/completed");
            directory2.mkdir();
        }
        catch (Exception e){
        }
        directory = new File(path + "/haveToDo");
        for(File item : Objects.requireNonNull(directory.listFiles()))
            nameOfTask.add(item.getName().replaceAll(".json",""));

        ObservableList<String> langs1 = FXCollections.observableArrayList(nameOfTask);
        haveToDoList.setItems(langs1);
        nameOfTask.clear();

        MultipleSelectionModel<String> langsSelectionModel1 = haveToDoList.getSelectionModel();
        langsSelectionModel1.selectedItemProperty().addListener(new ChangeListener<String>(){
            public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue){
                //обработчик для списка HaveToDo
                nameOFTaskFormHaveToDo = newValue;
                nameOFTaskFormCompleted = "";
            }
        });

        directory = new File(path + "/completed");
        for(File item : Objects.requireNonNull(directory.listFiles()))
            nameOfTask.add(item.getName().replaceAll(".json",""));
        ObservableList<String> langs2 = FXCollections.observableArrayList(nameOfTask);
        completedList.setItems(langs2);

        MultipleSelectionModel<String> langsSelectionModel2 = completedList.getSelectionModel();
        langsSelectionModel2.selectedItemProperty().addListener(new ChangeListener<String>(){
            //обработчик для списка Completed
            public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue){
                nameOFTaskFormCompleted = newValue;
                nameOFTaskFormHaveToDo = "";
            }
        });
    }

    @FXML
    private void doneEvent(ActionEvent event) throws IOException, ParseException {
        //перенос задачи в поле Completed
        FileReader reader;
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = null;
        if(!nameOFTaskFormHaveToDo.equals("")) {
            reader = new FileReader(path + "/haveToDo/" + nameOFTaskFormHaveToDo + ".json");
            jsonObject = (JSONObject) jsonParser.parse(reader);
            reader.close();
        }

        FileWriter writer= new FileWriter(path + "/completed/" + nameOFTaskFormHaveToDo + ".json");
        writer.write(jsonObject.toJSONString());
        writer.flush();
        writer.close();

        File file = null;
        if(!nameOFTaskFormHaveToDo.equals("")){
            file = new File(path + "/haveToDo/" + nameOFTaskFormHaveToDo + ".json");
            if(file.delete()){
                System.out.println(file.getName() + " is deleted!");
            }else{
                System.out.println("Delete operation is failed.");
            }
        }

        Stage stage = (Stage) doneButton.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../sample/sample.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
    private void openEvent(ActionEvent event) throws IOException {
        //открытие выделенной задачи для просмотра
        if (!nameOFTaskFormHaveToDo.equals("")){
            currentDirectory = new File(path + "/haveToDo");
            currentNameOFTask = nameOFTaskFormHaveToDo;
            openNewStage("../sample/taskPage.fxml");
        }
        else{
            if (!nameOFTaskFormCompleted.equals("")){
                currentDirectory = new File(path + "/completed");
                currentNameOFTask = nameOFTaskFormCompleted;
                openNewStage("../sample/taskPage.fxml");
            }
        }
    }

    @FXML
    private void deleteEvent(ActionEvent event) throws IOException{
        //удаление выдеденной задачи
        File file;
        if (!nameOFTaskFormHaveToDo.equals("")){
            file = new File(path + "/haveToDo/" + nameOFTaskFormHaveToDo + ".json");
            if(file.delete()){
                System.out.println(file.getName() + " is deleted!");
            }else{
                System.out.println("Delete operation is failed.");
            }
        }
        else {
            if (!nameOFTaskFormCompleted.equals("")){
                file = new File(path + "/completed/" + nameOFTaskFormCompleted + ".json");
                if(file.delete()){
                    System.out.println(file.getName() + " is deleted!");
                }else{
                    System.out.println("Delete operation is failed.");
                }
            }
        }

        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../sample/sample.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void addNewEvent(ActionEvent event) throws IOException {
        //добавление новой задачи
        openNewStage("../sample/CreatePage.fxml");
    }

    public void openNewStage(String name) throws IOException {
        // переход на другое окно
        Stage stage = (Stage) openButton.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(name));
        Parent root = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.show();
    }
}