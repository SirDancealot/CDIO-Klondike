package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
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
    private ChoiceBox table;

    private Stage editStage;
    private Card card;

    @FXML
    private void initialize() {

    }

    public void setStage(Stage editStage) {
        this.editStage = editStage;
    }

    public void setCard(Card card) {
        this.card = card;
        suit.setText(card.getSuit().getValue().toString());
        cardValue.setText(card.getCardValue().getValue().toString());
    }

    @FXML
    public void handleSave() {
        editStage.close();
    }

    @FXML
    private void cancel() {
        editStage.close();
    }

}
