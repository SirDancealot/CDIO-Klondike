package dto;

public class LogicController {
    GameState gameState;

    public LogicController(){
        gameState = GameState.getInstance();
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
                if (gameStack.stack.peek().getCardValue() == 1 && !gameStack.stack.peek().isHidden()) {
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
                                if (gameStackReceive.stack.peek().getCardValue() == gameStackTake.stack.elementAt(i).getCardValue() + 1) {
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
                                if (finishStack.stack.peek().getCardValue() == gameStack.stack.elementAt(i).getCardValue() - 1) {
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
                            if(gameStack.stack.elementAt(i).getCardValue() == 13 && i != 0){
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
            if(gameState.getTurnedStock().stack.peek().getCardValue() == 13){
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
            if(gameState.getTurnedStock().stack.peek().getCardValue() == 1){
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
                    if (gameState.getTurnedStock().stack.peek().getCardValue() == gameStack.stack.peek().getCardValue() - 1) {
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
                    if (gameState.getTurnedStock().stack.peek().getCardValue() == finishStack.stack.peek().getCardValue() + 1) {
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
                        if (gameStack.stack.peek().getCardValue() == finishStack.stack.peek().getCardValue() + 1) {
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
                            if (gameStackTake.stack.elementAt(i).getCardValue() == gameStackReceive.stack.peek().getCardValue() - 1) {
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
            if(card.getCardValue() == 1){
                return true;
            }
            for (GameStack gameStack : gameState.getGameStacks()) {
                if(!gameStack.isEmpty()) {
                    if ((card.getCardValue() == gameStack.stack.peek().getCardValue() - 1) && (card.getColor() != gameStack.stack.peek().getColor())) {
                        return true;
                    }
                }
                if(gameStack.isEmpty() && card.getCardValue() == 13){
                    return true;
                }
            }
            for (FinishStack finishStack : gameState.getFinishStacks()) {
                if(!finishStack.isEmpty()) {
                    if ((card.getCardValue() == finishStack.stack.peek().getCardValue() + 1) && (card.getSuit() == finishStack.stack.peek().getSuit())) {
                        return true;
                    }
                }

            }
        }
        for (Card card : gameState.getTurnedStock().stack) {
            if(!card.isKnown()){
                return true;
            }
            if(card.getCardValue() == 1){
                return true;
            }
            for (GameStack gameStack : gameState.getGameStacks()) {
                if(!gameStack.isEmpty()) {
                    if ((card.getCardValue() == gameStack.stack.peek().getCardValue() - 1) && (card.getColor() != gameStack.stack.peek().getColor())) {
                        return true;
                    }
                }
                if(gameStack.isEmpty() && card.getCardValue() == 13){
                    return true;
                }
            }
            for (FinishStack finishStack : gameState.getFinishStacks()) {
                if(!finishStack.isEmpty()) {
                    if ((card.getCardValue() == finishStack.stack.peek().getCardValue() + 1) && (card.getSuit() == finishStack.stack.peek().getSuit())) {
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
            if(finishStack.stack.peek().getCardValue() != 13){
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
}
