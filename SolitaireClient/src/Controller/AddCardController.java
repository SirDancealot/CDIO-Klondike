package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCardController {
    @FXML
    private TextField row;
    @FXML
    private TextField suit;
    @FXML
    private TextField cardValue;
    @FXML
    private ChoiceBox table;

    private Stage editStage;

    @FXML
    private void initialize() {

    }

    public void setStage(Stage editStage) {
        this.editStage = editStage;
    }

    //@FXML
    //private void

    @FXML
    private void cancel() {
        editStage.close();
    }

}
