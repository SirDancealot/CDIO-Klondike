package model.Controller;

import model.dto.*;

import java.util.Scanner;

public class LogicController {
    private static LogicController logicController_instance = null;

    GameState gameState;
    int[] randomOrder = {2,4,7,9,8,11,5,1,13,6,12,3,10,10,3,12,6,13,1,5,11,8,9,7,4,2,1,13,2,12,3,11,4,10,5,9,6,8,7,7,6,8,5,9,4,10,3,11,2,12,13,1};

    private LogicController(){
        gameState = GameState.getInstance();
    }

    public static LogicController getInstance()
    {
        if (logicController_instance == null)
            logicController_instance = new LogicController();

        return logicController_instance;
    }

    public GameState getGameState(){
        return gameState;
    }

    public void setGameState(GameState gameState){
        this.gameState = gameState;
    }


    /*
    private String turnHiddenCard(){
        //TODO Add revealed card to gameState.
        int index = 0;
        for (GameStack gameStack : gameStacks) {
            index++;
            if(gameStack.stack.peek().isHidden()){
                gameStack.stack.peek().setHidden(false);
                gameStack.stack.peek().setKnown(true);
                return "Turn hidden card on stack " + index;
            }
        }
        return null;
    }

     */

    private String turnHiddenCardTest(){
        int index = 0;
        for (GameStack gameStack : gameState.getGameStacks()) {
            index++;
            if(!gameStack.stack.isEmpty()) {
                if (gameStack.stack.peek().isHidden()) {
                    gameStack.stack.peek().setHidden(false);
                    gameStack.stack.peek().setKnown(true);
                    return "Turn hidden card on stack " + index;
                }
            }
        }
        return null;
    }

    private String aceToFinishStack(){
        Card card;
        int index = 0;
        for (GameStack gameStack : gameState.getGameStacks()) {
            index++;
            if(!gameStack.isEmpty()) {
                if (gameStack.stack.peek().getCardValue().getValue() == 1 && !gameStack.stack.peek().isHidden()) {
                    card = gameStack.stack.pop();
                    for (FinishStack finishStack : gameState.getFinishStacks()) {
                        if (finishStack.isEmpty()) {
                            finishStack.stack.add(card);
                            return "Move " + card.toString() + " to an empty finish stack";
                        }
                    }
                }
            }
        }
        return null;
    }

    private String fullVisibleStackToStack(){
        for (GameStack gameStackTake : gameState.getGameStacks()) {
            for (int i = 0; i < gameStackTake.size(); i++) {
                if(!gameStackTake.stack.elementAt(i).isHidden()){
                    for (GameStack gameStackReceive : gameState.getGameStacks()) {
                        if(!gameStackReceive.isEmpty()) {
                            if (gameStackReceive.stack.peek().getColor() != gameStackTake.stack.elementAt(i).getColor()) {
                                if (gameStackReceive.stack.peek().getCardValue().getValue() == gameStackTake.stack.elementAt(i).getCardValue().getValue() + 1) {
                                    String returnString = "Move " + gameStackTake.stack.elementAt(i).toString() + " to " + gameStackReceive.stack.peek().toString();
                                    gameStackReceive.addCards(gameStackTake.takeCards(i));
                                    return returnString;
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
        return null;
    }

    private String lastVisibleCardToFinishStack(){
        for (GameStack gameStack : gameState.getGameStacks()) {
            for (int i = 0; i < gameStack.size(); i++) {
                if(!gameStack.stack.elementAt(i).isHidden()){
                    if(gameStack.size() == i+1){
                        for (FinishStack finishStack : gameState.getFinishStacks()) {
                            if(!finishStack.isEmpty()) {
                                if (finishStack.stack.peek().getCardValue().getValue() == gameStack.stack.elementAt(i).getCardValue().getValue() - 1) {
                                    if (finishStack.stack.peek().getSuit() == gameStack.stack.elementAt(i).getSuit()) {
                                        String returnString = "Move " + gameStack.stack.elementAt(i).toString() + " to " + finishStack.stack.peek().toString() + " on finish stack";
                                        finishStack.addCards(gameStack.takeCards(i));
                                        return returnString;
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
        return null;
    }

    private String kingToEmptyStack(){
        int index = 0;
        for (GameStack gameStackEmpty : gameState.getGameStacks()) {
            index++;
            if(gameStackEmpty.isEmpty()){
                for (GameStack gameStack : gameState.getGameStacks()) {
                    for (int i = 0; i < gameStack.size(); i++) {
                        if(!gameStack.stack.elementAt(i).isHidden()){
                            if(gameStack.stack.elementAt(i).getCardValue().getValue() == 13 && i != 0){
                                String returnString = "Move " + gameStack.stack.elementAt(i).toString() + " to empty stack " + index;
                                gameStackEmpty.addCards(gameStack.takeCards(i));
                                return returnString;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return null;
    }

    private String playCardFromTurnedStock(){
        if(!gameState.getTurnedStock().isEmpty()){
            if(gameState.getTurnedStock().stack.peek().getCardValue().getValue() == 13){
                String returnString = null;
                int index = 0;
                for (GameStack gameStack : gameState.getGameStacks()) {
                    index++;
                    if(gameStack.isEmpty()){
                        returnString = "Move " + gameState.getTurnedStock().stack.peek().toString() + " from stock to stack " + index;
                        gameStack.addCard(gameState.getTurnedStock().stack.pop());
                        return returnString;
                    }
                }
            }
        }
        if(!gameState.getTurnedStock().isEmpty()){
            if(gameState.getTurnedStock().stack.peek().getCardValue().getValue() == 1){
                String returnString = null;
                for (FinishStack finishStack : gameState.getFinishStacks()) {
                    if(finishStack.isEmpty()){
                        returnString = "Move " + gameState.getTurnedStock().stack.peek().toString() + " from stock to empty finish stack";
                        finishStack.addCard(gameState.getTurnedStock().stack.pop());
                        return returnString;
                    }
                }
            }
        }
        for (GameStack gameStack : gameState.getGameStacks()) {
            if(!gameStack.isEmpty() && !gameState.getTurnedStock().isEmpty()) {
                if (gameState.getTurnedStock().stack.peek().getColor() != gameStack.stack.peek().getColor()) {
                    if (gameState.getTurnedStock().stack.peek().getCardValue().getValue() == gameStack.stack.peek().getCardValue().getValue() - 1) {
                        String returnString = "Move " + gameState.getTurnedStock().stack.peek().toString() +  " to " + gameStack.stack.peek().toString();
                        gameStack.addCard(gameState.getTurnedStock().stack.pop());
                        return returnString;
                    }
                }
            }
        }
        for (FinishStack finishStack : gameState.getFinishStacks()) {
            if(!finishStack.isEmpty() && !gameState.getTurnedStock().isEmpty()) {
                if (gameState.getTurnedStock().stack.peek().getSuit() == finishStack.stack.peek().getSuit()) {
                    if (gameState.getTurnedStock().stack.peek().getCardValue().getValue() == finishStack.stack.peek().getCardValue().getValue() + 1) {
                        String returnString = "Move " + gameState.getTurnedStock().stack.peek().toString() +  " to " + finishStack.stack.peek().toString() + " on finish stack";
                        finishStack.addCard(gameState.getTurnedStock().stack.pop());
                        return returnString;
                    }
                }
            }
        }
        return null;
    }

    private String turnNewCardFromStock(){
        String returnString = "Turn new card from the stock";
        if(gameState.getStock().size() > 0){
            //TODO Add revealed card to gameState.
            return returnString;
        }else if(gameState.getTurnedStock().size() != 0){
            gameState.getStock().addCardsReversed(gameState.getTurnedStock().takeCards(gameState.getTurnedStock().stack.size()));
            return returnString;
        }else return null;
    }



    private String turnNewCardFromStockTest(){

        if(gameState.getStock().size() > 0){
            String returnString = "Turn new card from the stock";
            gameState.getTurnedStock().stack.add(gameState.getStock().stack.pop());
            gameState.getTurnedStock().stack.peek().setHidden(false);
            gameState.getTurnedStock().stack.peek().setKnown(true);
            return returnString;
        }else if(gameState.getTurnedStock().size() > 0){
            String returnString = "Turn the stock over, then turn new card from the stock";
            gameState.getStock().addCardsReversed(gameState.getTurnedStock().takeCards(0));
            for (Card card : gameState.getStock().stack) {
                card.setHidden(true);
            }

            gameState.getTurnedStock().stack.add(gameState.getStock().stack.pop());
            gameState.getTurnedStock().stack.peek().setHidden(false);
            gameState.getTurnedStock().stack.peek().setKnown(true);
            return returnString;
        }else return null;
    }

    public int countCardsInGame(){
        int cards = 0;
        for (GameStack gameStack : gameState.getGameStacks()) {
            cards += gameStack.size();
        }
        for (FinishStack finishStack : gameState.getFinishStacks()) {
            cards += finishStack.size();
        }
        cards += gameState.getStock().size();
        cards += gameState.getTurnedStock().size();
        return cards;
    }

    private String cardToFinishStack(){
        String returnString;
        for (GameStack gameStack : gameState.getGameStacks()) {
            for (FinishStack finishStack : gameState.getFinishStacks()) {
                if(!gameStack.isEmpty() && !finishStack.isEmpty()) {
                    if (gameStack.stack.peek().getSuit() == finishStack.stack.peek().getSuit()) {
                        if (gameStack.stack.peek().getCardValue().getValue() == finishStack.stack.peek().getCardValue().getValue() + 1) {
                            returnString = "Move " + gameStack.stack.peek().toString() + " to " + finishStack.stack.peek().toString();
                            finishStack.stack.add(gameStack.stack.pop());
                            return returnString;
                        }
                    }
                }
            }
        }
        return null;
    }

    private String splitStackToMakeLargerStack(){
        for (GameStack gameStackTake : gameState.getGameStacks()) {
            int nonHiddenCardsBeforeThisInTake = 0;
            for (int i = 0; i < gameStackTake.stack.size(); i++) {
                if(!gameStackTake.stack.elementAt(i).isHidden()){
                    for (GameStack gameStackReceive : gameState.getGameStacks()) {
                        int nonHiddenCardsBeforeThisInReceive = -1;
                        for (int j = 0; j < gameStackReceive.stack.size(); j++) {
                            if(!gameStackReceive.stack.elementAt(j).isHidden()){
                                nonHiddenCardsBeforeThisInReceive++;
                            }
                        }
                        if(!gameStackReceive.isEmpty()) {
                            if (gameStackTake.stack.elementAt(i).getCardValue().getValue() == gameStackReceive.stack.peek().getCardValue().getValue() - 1) {
                                if (gameStackTake.stack.elementAt(i).getColor() != gameStackReceive.stack.peek().getColor()) {
                                    if (nonHiddenCardsBeforeThisInTake < nonHiddenCardsBeforeThisInReceive) {
                                        String returnString = "Move " + gameStackTake.stack.elementAt(i).toString() + " to " + gameStackReceive.stack.peek().toString();
                                        gameStackReceive.addCards(gameStackTake.takeCards(i));
                                        return returnString;
                                    }
                                }
                            }
                        }
                    }
                    nonHiddenCardsBeforeThisInTake++;
                }
            }
        }
        return null;
    }

    private boolean possibleMoveInStock(){
        for (Card card : gameState.getStock().stack) {
            if(!card.isKnown()){
                return true;
            }
            if(card.getCardValue().getValue() == 1){
                return true;
            }
            for (GameStack gameStack : gameState.getGameStacks()) {
                if(!gameStack.isEmpty()) {
                    if ((card.getCardValue().getValue() == gameStack.stack.peek().getCardValue().getValue() - 1) && (card.getColor() != gameStack.stack.peek().getColor())) {
                        return true;
                    }
                }
                if(gameStack.isEmpty() && card.getCardValue().getValue() == 13){
                    return true;
                }
            }
            for (FinishStack finishStack : gameState.getFinishStacks()) {
                if(!finishStack.isEmpty()) {
                    if ((card.getCardValue().getValue() == finishStack.stack.peek().getCardValue().getValue() + 1) && (card.getSuit() == finishStack.stack.peek().getSuit())) {
                        return true;
                    }
                }

            }
        }
        for (Card card : gameState.getTurnedStock().stack) {
            if(!card.isKnown()){
                return true;
            }
            if(card.getCardValue().getValue() == 1){
                return true;
            }
            for (GameStack gameStack : gameState.getGameStacks()) {
                if(!gameStack.isEmpty()) {
                    if ((card.getCardValue().getValue() == gameStack.stack.peek().getCardValue().getValue() - 1) && (card.getColor() != gameStack.stack.peek().getColor())) {
                        return true;
                    }
                }
                if(gameStack.isEmpty() && card.getCardValue().getValue() == 13){
                    return true;
                }
            }
            for (FinishStack finishStack : gameState.getFinishStacks()) {
                if(!finishStack.isEmpty()) {
                    if ((card.getCardValue().getValue() == finishStack.stack.peek().getCardValue().getValue() + 1) && (card.getSuit() == finishStack.stack.peek().getSuit())) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    public boolean gameWon(){
        boolean won = true;
        for (FinishStack finishStack : gameState.getFinishStacks()) {
            if(finishStack.isEmpty()){
                return false;
            }
            if(finishStack.stack.peek().getCardValue().getValue() != 13){
                won = false;
            }
        }
        return won;
    }

    public String makeMoveTest(){
        String returnString;

        returnString = turnHiddenCardTest();
        if(returnString != null){
            return returnString;
        }
        returnString = aceToFinishStack();
        if(returnString != null){
            return returnString;
        }
        returnString = fullVisibleStackToStack();
        if(returnString != null){
            return returnString;
        }
        returnString = kingToEmptyStack();
        if(returnString != null) {
            return returnString;
        }
        returnString = splitStackToMakeLargerStack();
        if(returnString != null){
            return returnString;
        }
        returnString = playCardFromTurnedStock();
        if(returnString != null){
            return returnString;
        }
        returnString = lastVisibleCardToFinishStack();
        if(returnString != null) {
            return returnString;
        }
        if(possibleMoveInStock()) {
            returnString = turnNewCardFromStockTest();
            if (returnString != null) {
                return returnString;
            }
        }
        returnString = cardToFinishStack();
        return returnString;
    }

    public void runGame(){
        boolean running = true;
        boolean moveSinceLastStockTurn = true;
        while(running){
            if(gameWon()){
                System.out.println("YOU WON!");

                break;
            }else {
                String moveString = makeMoveTest();

                if (moveString == null) {
                    running = false;
                    System.out.println("GAME OVER!");
                } else {
                    if (moveString.equals("Turn the stock over, then turn new card from the stock") && !moveSinceLastStockTurn) {
                        System.out.println("GAME OVER!");
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
        gameState = new GameState();
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
        gameState.setGameStacks(gameStacks);
        gameState.setStock(stock);
    }
}
