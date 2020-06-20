package model.Controller;

import model.dto.*;

import java.util.Scanner;

public class TestController {
    /*public static void main(String[] args){

        TestController testController = new TestController();
        for (int i = 0; i < 1000; i++) {
            testController.setupGame();
            testController.runGame();
        }
        System.out.println(testController.getGames());
        System.exit(0);
    }*/

    int wonGames = 0;
    int lostGames = 0;
    int[] randomOrder = {2,4,7,9,8,11,5,1,13,6,12,3,10,10,3,12,6,13,1,5,11,8,9,7,4,2,1,13,2,12,3,11,4,10,5,9,6,8,7,7,6,8,5,9,4,10,3,11,2,12,13,1};
    LogicController logicController;

    private String getGames(){
        return "Won: " + wonGames + " Lost: " + lostGames;
    }



    public TestController(){
        logicController = LogicController.getInstance();
    }

    public void runGame(){
        boolean running = true;
        boolean moveSinceLastStockTurn = true;
        while(running){
            if(logicController.gameWon()){
                System.out.println("YOU WON!");
                wonGames++;
                break;
            }else {
                String moveString = logicController.makeMoveTest();

                if (moveString == null) {
                    running = false;
                    System.out.println("GAME OVER!");
                    lostGames++;
                } else {
                    if (moveString.equals("Turn the stock over, then turn new card from the stock") && !moveSinceLastStockTurn) {
                        System.out.println("GAME OVER!");
                        lostGames++;
                        break;
                    } else if (!moveString.equals("Turn the stock over, then turn new card from the stock") && !moveString.equals("Turn new card from the stock")) {
                        moveSinceLastStockTurn = true;
                    } else if (moveString.equals("Turn the stock over, then turn new card from the stock")) {
                        moveSinceLastStockTurn = false;
                    }

                    //System.out.println(moveString);
                    //System.out.println(gameState.countCardsInGame());

                    Scanner scanner = new Scanner(System.in);
                    scanner.nextLine();
                }
            }
        }
    }

    public void setupGame(){
        logicController.gameState = new GameState();
        CardStack stock = makeStock();
        stock.shuffle();
        makeGameStateFromStock(stock);
    }

    private CardStack makeStock(){
        CardStack stock = new CardStack();
        int counter = 1;
        for (int number : randomOrder) {
            Card card;
            if(counter > 39){
                card = new Card(Card.Suit.HEARTS,number);
                card.setHidden(true);
                stock.addCard(card);
            }else if(counter > 26){
                card = new Card(Card.Suit.CLUBS,number);
                card.setHidden(true);
                stock.addCard(card);
            }else if(counter > 13){
                card = new Card(Card.Suit.DIAMONDS,number);
                card.setHidden(true);
                stock.addCard(card);
            }else if(counter > 0){
                card = new Card(Card.Suit.SPADES,number);
                card.setHidden(true);
                stock.addCard(card);
            }

            counter ++;
        }

        return stock;
    }

    private void makeGameStateFromStock(CardStack stock){
        GameStack[] gameStacks = new GameStack[7];
        for (int i = 0; i < 7; i++) {
            gameStacks[i] = new GameStack();
        }
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if(i == j){
                    Card card = stock.stack.pop();
                    card.setHidden(false);
                    card.setKnown(true);
                    gameStacks[j].addCard(card);
                }else if(i < j){
                    Card card = stock.stack.pop();
                    gameStacks[j].addCard(card);
                }
            }
        }
        logicController.gameState.setGameStacks(gameStacks);
        logicController.gameState.setStock(stock);
    }

    public LogicController getLogicController() {
        return logicController;
    }

    public void setLogicController(LogicController logicController) {
        this.logicController = logicController;
    }

}
