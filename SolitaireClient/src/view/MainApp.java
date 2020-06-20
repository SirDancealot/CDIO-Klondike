package view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import model.Controller.*;
import model.dto.Card;

public class MainApp extends Application {
    private Stage primaryStage;
    private AnchorPane rootLayout;
    private LogicController logicController = LogicController.getInstance();
    private ViewController viewController = new ViewController();
    boolean isPushed = false;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("SolitaireClient");

        initRootLayout();
    }

    public ObservableList<Card> getCards() {
        ObservableList<Card> cards = FXCollections.observableArrayList();
        cards.add(new Card(Card.Suit.SPADES,1));
        cards.add(new Card(Card.Suit.SPADES,2));
        cards.add(new Card(Card.Suit.SPADES,3));
        cards.add(new Card(Card.Suit.SPADES,4));

        cards.add(new Card(Card.Suit.HEARTS,1));
        cards.add(new Card(Card.Suit.HEARTS,2));
        cards.add(new Card(Card.Suit.HEARTS,3));
        cards.add(new Card(Card.Suit.HEARTS,4));

        return cards;
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("ListOfCards.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

            viewController = loader.getController();

            logicController.setupGame();
            viewController.updateView(logicController.getGameState());

            viewController.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dialogBoxForEdit(Card card) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("CardEditDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Card");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            CardEditController controller = loader.getController();
            controller.setStage(dialogStage);
            controller.setCard(card);


            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public boolean isPushed() {
        return isPushed;
    }

    public void setPushed(boolean pushed) {
        isPushed = pushed;
    }

    public static void main(String[] args) {
        launch(args);
    }
}