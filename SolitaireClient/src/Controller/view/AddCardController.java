package Controller.view;

import Controller.runtime.LogicController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.dto.Card;

public class AddCardController {
    @FXML
    private TextField row;
    @FXML
    private TextField suit;
    @FXML
    private TextField cardValue;
    @FXML
    private ChoiceBox<String> table;

    private Stage editStage;
    private ObservableList<String> allStacks;
    ViewController viewController;
    LogicController logicController;
    TableView<Card> tableToAdd;

    @FXML
    private void initialize() {
        viewController = ViewController.getINSTANCE();
        logicController = LogicController.getInstance();
    }

    @FXML
    public void handleSave() {
        tableToAdd = viewController.getSpecificStack(table.getSelectionModel().getSelectedIndex());
        if(validInput()) {
            Card newCard = new Card(Card.Suit.valueOf(suit.getText().toUpperCase()), Integer.parseInt(cardValue.getText()));
            for (int i = 0; i < viewController.getStacks().length; i++) {
                if(tableToAdd.getId().equals(viewController.getSpecificStack(i).getId())) {
                    if(i <= 6) {
                        logicController.getGameState().getGameStacks()[i].stack.insertElementAt(newCard, Integer.parseInt(row.getText()) - 1);
                        tableToAdd.getItems().add(Integer.parseInt(row.getText())-1,newCard);
                        break;
                    } else if(i<=9) {
                        logicController.getGameState().getFinishStacks()[i-7].stack.insertElementAt(newCard, Integer.parseInt(row.getText()) - 1);
                        tableToAdd.getItems().add(Integer.parseInt(row.getText())-1,newCard);
                        break;
                    } else {
                        logicController.getGameState().getTurnedStock().stack.insertElementAt(newCard,Integer.parseInt(row.getText())-1);
                        tableToAdd.getItems().add(Integer.parseInt(row.getText())-1,newCard);
                    }
                }
            }
            editStage.close();
        }
    }

    public void populateChoiceBox() {
        allStacks = FXCollections.observableArrayList();
        allStacks.addAll(getStacks());
        table.setItems(allStacks);
    }

    public void setStage(Stage editStage) {
        this.editStage = editStage;
    }

    @FXML
    private void cancel() {
        editStage.close();
    }

    private String[] getStacks() {
        return new String[]{"GameStack (1)", "GameStack (2)","GameStack (3)","GameStack (4)","GameStack (5)","GameStack (6)",
                "GameStack (7)","FinishStack (1)","FinishStack (2)","FinishStack (3)","FinishStack (4)", "TurnedStock"};
    }

    private boolean validInput() {
        String message = "";

        boolean validSuit = false;
        for (Card.Suit s : Card.Suit.values()) {
            if(s.toString().equals(suit.getText().toUpperCase())) {
                validSuit = true;
            }
        }
        if(!validSuit) {
            message ="Choose a valid Suit";
        }

        int s = Integer.parseInt(cardValue.getText());
        if(s < 0 || s > 13) {
            message = "Choose a valid cardValue between 0 and 13";
        }

        s = Integer.parseInt(row.getText());
        if(s < 0 || s >= tableToAdd.getItems().size()+2) {
            message = "Choose a valid row between 0 and " + (tableToAdd.getItems().size()+1);
        }

        if(message.isEmpty()) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(editStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(message);

            alert.showAndWait();
            return false;
        }
    }
}
