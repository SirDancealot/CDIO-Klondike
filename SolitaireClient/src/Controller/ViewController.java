package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.dto.Card;
import model.dto.GameState;
import view.MainApp;

import java.util.concurrent.atomic.AtomicBoolean;

public class ViewController {
    AtomicBoolean nextMove = new AtomicBoolean(false);

    private static volatile ViewController INSTANCE = null;

    public ViewController() throws Exception {
        if (INSTANCE == null)
            INSTANCE = this;
        else
            throw new Exception("Cannot have two controllers");
    }

    public static ViewController getINSTANCE() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new ViewController();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    @FXML
    private TableView<Card> finishStack4;
    @FXML
    private TableView<Card> finishStack3;
    @FXML
    private TableView<Card> finishStack2;
    @FXML
    private TableView<Card> finishStack1;
    @FXML
    private TableView<Card> turnedStock;
    @FXML
    private TableView<Card> gameStack1;
    @FXML
    private TableView<Card> gameStack2;
    @FXML
    private TableView<Card> gameStack3;
    @FXML
    private TableView<Card> gameStack4;
    @FXML
    private TableView<Card> gameStack5;
    @FXML
    private TableView<Card> gameStack6;
    @FXML
    private TableView<Card> gameStack7;
    private TableView<Card>[] stacks;
    @FXML
    private TableColumn<Card, Card.Suit> turnedStockSuit;
    @FXML
    private TableColumn<Card, String> turnedStockNumber;
    @FXML
    private TableColumn<Card, Card.Suit> finishStack4Suit;
    @FXML
    private TableColumn<Card, String> finishStack4Number;
    @FXML
    private TableColumn<Card, Card.Suit> finishStack3Suit;
    @FXML
    private TableColumn<Card, String> finishStack3Number;
    @FXML
    private TableColumn<Card, Card.Suit> finishStack2Suit;
    @FXML
    private TableColumn<Card, String> finishStack2Number;
    @FXML
    private TableColumn<Card, Card.Suit> finishStack1Suit;
    @FXML
    private TableColumn<Card, String> finishStack1Number;
    @FXML
    private TableColumn<Card, Card.Suit> gameStack1Suit;
    @FXML
    private TableColumn<Card, String> gameStack1Number;
    @FXML
    private TableColumn<Card, Card.Suit> gameStack2Suit;
    @FXML
    private TableColumn<Card, String> gameStack2Number;
    @FXML
    private TableColumn<Card, Card.Suit> gameStack3Suit;
    @FXML
    private TableColumn<Card, String> gameStack3Number;
    @FXML
    private TableColumn<Card, Card.Suit> gameStack4Suit;
    @FXML
    private TableColumn<Card, String> gameStack4Number;
    @FXML
    private TableColumn<Card, Card.Suit> gameStack5Suit;
    @FXML
    private TableColumn<Card, String> gameStack5Number;
    @FXML
    private TableColumn<Card, Card.Suit> gameStack6Suit;
    @FXML
    private TableColumn<Card, String> gameStack6Number;
    @FXML
    private TableColumn<Card, Card.Suit> gameStack7Suit;
    @FXML
    private TableColumn<Card, String> gameStack7Number;
    private TableColumn[] stackSuits;
    private TableColumn[] stackNumbers;
    @FXML
    private TextArea moveString;

    private MainApp mainApp;

    @FXML
    private void initialize() {
        stacks = new TableView[] {gameStack1, gameStack2, gameStack3, gameStack4, gameStack5, gameStack6, gameStack7, finishStack1, finishStack2, finishStack3, finishStack4, turnedStock};
        stackSuits = new TableColumn[]{gameStack1Suit, gameStack2Suit, gameStack3Suit, gameStack4Suit, gameStack5Suit, gameStack6Suit, gameStack7Suit, finishStack1Suit, finishStack2Suit, finishStack3Suit, finishStack4Suit, turnedStockSuit};
        stackNumbers = new TableColumn[] {gameStack1Number, gameStack2Number, gameStack3Number, gameStack4Number, gameStack5Number, gameStack6Number, gameStack7Number, finishStack1Number, finishStack2Number, finishStack3Number, finishStack4Number, turnedStockNumber};

        for (TableColumn<Card, Card.Suit> t : stackSuits) {
            t.setCellValueFactory(cellData -> cellData.getValue().getSuit());
        }

        for (TableColumn<Card, String> t : stackNumbers) {
            t.setCellValueFactory(cellData -> cellData.getValue().getStringValue());
        }

        for (TableView<Card> t : stacks) {
            t.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    clearSelections(t);
                }
            });
        }
    }

    private void clearSelections(TableView<Card> tableView) {
    	for (TableView<Card> t : stacks) {
            if(tableView != t) {
                t.getSelectionModel().clearSelection();
            }
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void editButton() {
        Card specificCard = null;

        for (TableView<Card> t : stacks) {
            if(specificCard == null) {
                specificCard = t.getSelectionModel().getSelectedItem();
            }
        }

        mainApp.dialogBoxForEdit(specificCard);
    }

    @FXML
    private void nextMoveButton() {
        nextMove.set(true);
        LogicController logicController = LogicController.getInstance();
        String move = logicController.makeMoveTest();
        if(move != null) {
            moveString.setText(move);
        } else {
            moveString.setText("You lost!");
        }
        updateView(logicController.getGameState());
    }

    public void setText(String text) {
        moveString.setText(text);
    }

    public void updateView(GameState gameState) {
        ObservableList<Card> cards = FXCollections.observableArrayList();
        cards.addAll(gameState.getTurnedStock().stack);
        turnedStock.setItems(cards);

        for (int i = 0; i < 7; i++) {
            cards = FXCollections.observableArrayList();
            cards.addAll(gameState.getGameStacks()[i].stack);
            stacks[i].setItems(cards);
        }
        for (int i = 0; i < 4; i++) {
            cards = FXCollections.observableArrayList();
            cards.addAll(gameState.getFinishStacks()[i].stack);
            stacks[i+7].setItems(cards);
        }
    }
}
