/**
 * @author Anders Eisenhardt s185146
 * @author Martin Wassmann s185033
 */

package controller.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.dto.Card;

public class CardEditController {
    @FXML
    private TextField cardValue;
    @FXML
    private TextField suit;

    private Stage editStage;
    private Card card;

    @FXML
    private void initialize() {}

    public void setStage(Stage editStage) {
        this.editStage = editStage;
    }

    public void setCard(Card card) {
        this.card = card;
        suit.setText(card.getSuit().getValue().toString());
        cardValue.setText(card.getCardValue().getValue().toString());
    }

    public boolean validInput(boolean changeSuit, boolean changeValue) {
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

        if((changeSuit ^ changeValue) && (card.getCardValue().get() == 0 || card.getSuit().getValue() == Card.Suit.HIDDEN)) {
            message = "When changing a hidden card, please change both values";
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

    @FXML
    public void handleOK() {
        Card.Suit lastSuit = card.getSuit().getValue();
        int lastValue = card.getCardValue().get();
        boolean changeSuit = !lastSuit.toString().equals(suit.getText().toUpperCase());
        boolean changeValue = lastValue != Integer.parseInt(cardValue.getText());

        if(validInput(changeSuit,changeValue)) {
            if(changeSuit) {
                card.setSuit(Card.Suit.valueOf(suit.getText().toUpperCase()));
            }
            if(changeValue) {
                card.setCardValue(Integer.parseInt(cardValue.getText()));
            }
            editStage.close();
        }
    }

    @FXML
    private void cancel() {
        editStage.close();
    }
}
