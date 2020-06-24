package controller.runtime;

import model.dto.*;

import java.io.IOException;

public class LogicController {
    private static LogicController logicController_instance = null;

    GameState gameState;
    
    private LogicController(){
        gameState = GameState.getPrimary();
    }

    public static LogicController getInstance() {
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
                    return "Turn hidden card on GameStack" + index;
                }
            }
        }
        return null;
    }

    private String aceToFinishStack(){
        Card card;
        int gameStackNumber = 0;
        for (GameStack gameStack : gameState.getGameStacks()) {
            gameStackNumber++;
            if(!gameStack.isEmpty()) {
                if (gameStack.stack.peek().getCardValue().getValue() == 1 && !gameStack.stack.peek().isHidden()) {
                    card = gameStack.stack.peek();
                    int finishStackNumber = 0;
                    for (FinishStack finishStack : gameState.getFinishStacks()) {
                        finishStackNumber++;
                        if (finishStack.isEmpty()) {
                            finishStack.stack.add(card);
                            gameStack.stack.pop();
                            return "Move " + card.toString() + " from GameStack" + gameStackNumber + " to an empty FinishStack" + finishStackNumber;
                        }
                    }
                }
            }
        }
        return null;
    }

    private String fullVisibleStackToStack(){
        int gameStackTakeNumber = 0;
        for (GameStack gameStackTake : gameState.getGameStacks()) {
            gameStackTakeNumber++;
            for (int i = 0; i < gameStackTake.size(); i++) {
                if(!gameStackTake.stack.elementAt(i).isHidden()){
                    int gameStackReceiveNumber = 0;
                    for (GameStack gameStackReceive : gameState.getGameStacks()) {
                        gameStackReceiveNumber++;
                        if(!gameStackReceive.isEmpty()) {
                            if (gameStackTake.stack.elementAt(i).canMoveToOnGameStack(gameStackReceive.stack.peek())) {
                                String returnString = "Move " + gameStackTake.stack.elementAt(i).toString() + " from GameStack" + gameStackTakeNumber + " to " + gameStackReceive.stack.peek().toString() + " on GameStack" + gameStackReceiveNumber;
                                gameStackReceive.addCards(gameStackTake.takeCards(i));
                                return returnString;

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
        int gameStackNumber = 0;
        for (GameStack gameStack : gameState.getGameStacks()) {
            gameStackNumber++;
            for (int i = 0; i < gameStack.size(); i++) {
                if(!gameStack.stack.elementAt(i).isHidden() && gameStack.size() == i+2){
                    int finishStackNumber = 0;
                    for (FinishStack finishStack : gameState.getFinishStacks()) {
                        finishStackNumber++;
                        if(!finishStack.isEmpty() && gameStack.stack.elementAt(i+1).canMoveToOnFinishStack(finishStack.stack.peek())) {
                            String returnString = "Move " + gameStack.stack.elementAt(i+1).toString() + "from GameStack " + gameStackNumber + " to " + finishStack.stack.peek().toString() + " on FinishStack" + finishStackNumber;
                            finishStack.addCards(gameStack.takeCards(i+1));
                            return returnString;
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
                                String returnString = "Move " + gameStack.stack.elementAt(i).toString() + " to empty GameStack " + index;
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
                String returnString;
                int index = 0;
                for (GameStack gameStack : gameState.getGameStacks()) {
                    index++;
                    if(gameStack.isEmpty()){
                        returnString = "Move " + gameState.getTurnedStock().stack.peek().toString() + " from stock to GameStack" + index;
                        gameStack.addCard(gameState.getTurnedStock().stack.pop());
                        return returnString;
                    }
                }
            }
        }
        if(!gameState.getTurnedStock().isEmpty()){
            if(gameState.getTurnedStock().stack.peek().getCardValue().getValue() == 1){
                String returnString;
                int finishStackNumber = 0;
                for (FinishStack finishStack : gameState.getFinishStacks()) {
                    finishStackNumber++;
                    if(finishStack.isEmpty()){
                        returnString = "Move " + gameState.getTurnedStock().stack.peek().toString() + " from stock to empty FinishStack" + finishStackNumber;
                        finishStack.addCard(gameState.getTurnedStock().stack.pop());
                        return returnString;
                    }
                }
            }
        }
        int gameStackNumber = 0;
        for (GameStack gameStack : gameState.getGameStacks()) {
            gameStackNumber++;
            if(!gameStack.isEmpty() && !gameState.getTurnedStock().isEmpty()) {
                if (gameState.getTurnedStock().stack.peek().canMoveToOnGameStack(gameStack.stack.peek())){
                    String returnString = "Move " + gameState.getTurnedStock().stack.peek().toString() +  " from stock to " + gameStack.stack.peek().toString() + " on GameStack" + gameStackNumber;
                    gameStack.addCard(gameState.getTurnedStock().stack.pop());
                    return returnString;
                }
            }
        }
        int finishStackNumber = 0;
        for (FinishStack finishStack : gameState.getFinishStacks()) {
            finishStackNumber++;
            if(!finishStack.isEmpty() && !gameState.getTurnedStock().isEmpty()) {
                if (gameState.getTurnedStock().stack.peek().canMoveToOnFinishStack(finishStack.stack.peek())) {
                    String returnString = "Move " + gameState.getTurnedStock().stack.peek().toString() +  " from stock to " + finishStack.stack.peek().toString() + " on FinishStack" + finishStackNumber;
                    finishStack.addCard(gameState.getTurnedStock().stack.pop());
                    return returnString;
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
            String returnString = "Turn new " + (gameState.getStock().stack.peek().isKnown() ? "" : "hidden ") + "card from the stock";
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
        int gameStackNumber = 0;
        for (GameStack gameStack : gameState.getGameStacks()) {
            gameStackNumber++;
            int finishStackNumber = 0;
            for (FinishStack finishStack : gameState.getFinishStacks()) {
                finishStackNumber++;
                if (!gameStack.isEmpty() && !finishStack.isEmpty()) {
                    if (gameStack.stack.peek().canMoveToOnFinishStack(finishStack.stack.peek())) {
                        returnString = "Move " + gameStack.stack.peek().toString() + " from GameStack" + gameStackNumber + " to " + finishStack.stack.peek().toString() + " on FinishStack" + finishStackNumber;
                        finishStack.stack.add(gameStack.stack.pop());
                        return returnString;
                    }
                }
            }
        }
        return null;
    }

    private String splitStackToMakeLargerStack(){
        int gameStackTakeNumber = 0;
        int gameStackReceiveNumber = 0;
        for (GameStack gameStackTake : gameState.getGameStacks()) {
            gameStackTakeNumber++;
            int nonHiddenCardsBeforeThisInTake = 0;
            for (int i = 0; i < gameStackTake.stack.size(); i++) {
                if(!gameStackTake.stack.elementAt(i).isHidden()){
                    for (GameStack gameStackReceive : gameState.getGameStacks()) {
                        gameStackReceiveNumber++;
                        int nonHiddenCardsBeforeThisInReceive = -1;
                        for (int j = 0; j < gameStackReceive.stack.size(); j++) {
                            if(!gameStackReceive.stack.elementAt(j).isHidden()){
                                nonHiddenCardsBeforeThisInReceive++;
                            }
                        }
                        if(!gameStackReceive.isEmpty()) {
                            if (gameStackTake.stack.elementAt(i).canMoveToOnGameStack(gameStackReceive.stack.peek())) {
                                if (nonHiddenCardsBeforeThisInTake < nonHiddenCardsBeforeThisInReceive) {
                                    String returnString = "Move " + gameStackTake.stack.elementAt(i).toString() + " from GameStack " + gameStackTakeNumber + "to " + gameStackReceive.stack.peek().toString() + " on GameStack" + gameStackReceiveNumber;
                                    gameStackReceive.addCards(gameStackTake.takeCards(i));
                                    return returnString;
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
                    if (card.canMoveToOnGameStack(gameStack.stack.peek())) {
                        return true;
                    }
                }
                if(gameStack.isEmpty() && card.getCardValue().getValue() == 13){
                    return true;
                }
            }
            for (FinishStack finishStack : gameState.getFinishStacks()) {
                if(!finishStack.isEmpty()) {
                    if ((card.canMoveToOnFinishStack(finishStack.stack.peek()))) {
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
                    if (card.canMoveToOnGameStack(gameStack.stack.peek())) {
                        return true;
                    }
                }
                if(gameStack.isEmpty() && card.getCardValue().getValue() == 13){
                    return true;
                }
            }
            for (FinishStack finishStack : gameState.getFinishStacks()) {
                if(!finishStack.isEmpty()) {
                    if ((card.canMoveToOnFinishStack(finishStack.stack.peek()))) {
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

        //TODO if all cards less then top cards are used, move cards to finish stack

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

    public void setupGame(GameState initialRead){
        gameState.getTurnedStock().addCard(initialRead.getTurnedStock().getTopCard());
        for (int i = 0; i < 7; i++) {
            gameState.getGameStacks()[i].addCard(initialRead.getGameStacks()[i].getTopCard());
        }
    }

    public void updateGameState(String returnString, GameState imgState) throws IOException {
        boolean tryAgain = true;
        while (tryAgain) {
            tryAgain = false;
            try {
                if (returnString.equals("Turn new hidden card from the stock")) {
                    gameState.getTurnedStock().getTopCard().setSuit(imgState.getTurnedStock().getTopCard().getSuit().getValue());
                    gameState.getTurnedStock().getTopCard().setCardValue(imgState.getTurnedStock().getTopCard().getCardValue().get());

                }
                else {
                    System.out.println(returnString);
                    int i = Character.getNumericValue(returnString.charAt(returnString.length()-1))-1;
                    System.out.println(i);
                    GameStack[] gStacks = gameState.getGameStacks();
                    int gStackLen[] = new int[7];
                    int imgStackLen[] = new int[7];
                    for (int j = 0; j < 7; j++) {
                        gStackLen[j] = gStacks[j].getMovable();
                        imgStackLen[j] = imgState.getGameStacks()[j].getMovable();
                    }
                    gStackLen[i]++;

                    int a = 0, b = 0;
                    while (a < i) {
                        if ((gStackLen[a] != 0 && imgStackLen[b] != 0) || (gStackLen[a] == 0 && imgStackLen[b] == 0)) {
                            b++;
                        }
                        a++;
                    }
                    gameState.getGameStacks()[a].getTopCard().setCardValue(imgState.getGameStacks()[b].getTopCard().getCardValue().getValue());
                    gameState.getGameStacks()[a].getTopCard().setSuit(imgState.getGameStacks()[b].getTopCard().getSuit().get());
                }
            } catch (Exception e) {
                tryAgain = true;
                System.out.println("Error in image, trying again");
                imgState = CommController.getINSTANCE().requestNewState();
            }

        }    }
}
