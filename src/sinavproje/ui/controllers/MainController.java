package sinavproje.ui.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.*;

import javafx.scene.control.*;
import sinavproje.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML private TabPane mainTab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainTab.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab tab, Tab t1) {
                if(t1.getId().equals("questionsTab"))
                {
                    Main.controllers.getQuestionsController().update();
                }
            }
        });
    }
}
