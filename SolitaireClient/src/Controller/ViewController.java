package Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
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
    private TableView<Card> finishStack4, finishStack3, finishStack2, finishStack1, turnedStock, gameStack1,
            gameStack2, gameStack3, gameStack4, gameStack5, gameStack6, gameStack7;
    @FXML
    private TableColumn<Card, Card.Suit> turnedStockSuit, finishStack4Suit, finishStack3Suit, finishStack2Suit,
            finishStack1Suit, gameStack1Suit, gameStack2Suit, gameStack3Suit, gameStack4Suit, gameStack5Suit,
            gameStack6Suit, gameStack7Suit;
    @FXML
    private TableColumn<Card, String> turnedStockNumber, finishStack4Number, finishStack3Number,
            finishStack2Number, finishStack1Number, gameStack1Number, gameStack2Number, gameStack3Number,
            gameStack4Number, gameStack5Number, gameStack6Number, gameStack7Number;

    @FXML
    private TextField moveString;

    private TableView<Card>[] stacks;
    private TableColumn[] stackSuits;
    private TableColumn[] stackNumbers;

    private MainApp mainApp;

    @FXML
    private void initialize() {
        stacks = new TableView[] {gameStack1, gameStack2, gameStack3, gameStack4, gameStack5, gameStack6, gameStack7, finishStack1, finishStack2, finishStack3, finishStack4, turnedStock};
        stackSuits = new TableColumn[]{gameStack1Suit, gameStack2Suit, gameStack3Suit, gameStack4Suit, gameStack5Suit, gameStack6Suit, gameStack7Suit, finishStack1Suit, finishStack2Suit, finishStack3Suit, finishStack4Suit, turnedStockSuit};
        stackNumbers = new TableColumn[] {gameStack1Number, gameStack2Number, gameStack3Number, gameStack4Number, gameStack5Number, gameStack6Number, gameStack7Number, finishStack1Number, finishStack2Number, finishStack3Number, finishStack4Number, turnedStockNumber};
        moveString.setEditable(false);


        for (TableColumn<Card, String> t : stackNumbers) {
            t.setCellValueFactory(cellData -> cellData.getValue().getStringValue());
            t.setSortable(false);
        }

        for (TableColumn<Card, Card.Suit> t : stackSuits) {
            t.setCellValueFactory(cellData -> cellData.getValue().getSuit());
            t.setSortable(false);
        }

        for (TableView<Card> t : stacks) {
            t.setBackground(new Background(new BackgroundFill(Color.rgb(201, 222, 195), CornerRadii.EMPTY, Insets.EMPTY)));
        }

        for (TableView<Card> t : stacks) {
            t.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    clearSelections(t);
                }
            });
            t.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    editButton();
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
    private void addCard() {
        mainApp.dialogBoxForAdd();
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
    }

    public boolean requestNextMove() {
        return nextMove.get();
    }

    public void setNextMove(boolean move) {
        nextMove.set(move);
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

    public TableView<Card> getSpecificStack(int item) {
        return stacks[item];
    }

    public TableView<Card>[] getStacks() {
        return stacks;
    }
}
