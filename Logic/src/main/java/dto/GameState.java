package dto;

public class GameState {
    private CardStack stock = new CardStack();
    private CardStack turnedStock = new CardStack();
    private GameStack[] gameStacks = new GameStack[7];
    private FinishStack[] finishStacks = new FinishStack[4];

    public GameState() {
        for (int i = 0; i < 7; i++) {
            gameStacks[i] = new GameStack();
        }
        for (int i = 0; i < 4; i++) {
            finishStacks[i] = new FinishStack();
        }
    }

    private String turnHiddenCard(){
        //TODO Add revealed card to gameState.
        int index = 0;
        for (GameStack gameStack : gameStacks) {
            index++;
            if(gameStack.stack.peek().isHidden()){
                gameStack.stack.peek().setHidden(false);
                return "Turn hidden card on stack " + index;
            }
        }
        return null;
    }

    private String turnHiddenCardTest(){
        int index = 0;
        for (GameStack gameStack : gameStacks) {
            index++;
            if(!gameStack.stack.isEmpty()) {
                if (gameStack.stack.peek().isHidden()) {
                    gameStack.stack.peek().setHidden(false);
                    return "Turn hidden card on stack " + index;
                }
            }
        }
        return null;
    }

    private String aceToFinishStack(){
        Card card;
        int index = 0;
        for (GameStack gameStack : gameStacks) {
            index++;
            if(!gameStack.isEmpty()) {
                if (gameStack.stack.peek().getCardValue() == 1 && !gameStack.stack.peek().isHidden()) {
                    card = gameStack.stack.pop();
                    for (FinishStack finishStack : finishStacks) {
                        if (finishStack.isEmpty()) {
                            finishStack.stack.add(card);
                            break;
                        }
                    }
                    return "Move " + card.getCardValue() + " of " + card.getSuit() + " to an empty finish stack";
                }
            }
        }
        return null;
    }

    private String fullVisibleStackToStack(){
        for (GameStack gameStackTake : gameStacks) {
            for (int i = 0; i < gameStackTake.size(); i++) {
                if(!gameStackTake.stack.elementAt(i).isHidden()){
                    for (GameStack gameStackReceive : gameStacks) {
                        if(!gameStackReceive.isEmpty()) {
                            if (gameStackReceive.stack.peek().getColor() != gameStackTake.stack.elementAt(i).getColor()) {
                                if (gameStackReceive.stack.peek().getCardValue() == gameStackTake.stack.elementAt(i).getCardValue() + 1) {
                                    String returnString = "Move " + gameStackTake.stack.elementAt(i).getCardValue() + " of " + gameStackTake.stack.elementAt(i).getSuit() + " to " + gameStackReceive.stack.peek().getCardValue() + " of " + gameStackReceive.stack.peek().getSuit();
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
        for (GameStack gameStack : gameStacks) {
            for (int i = 0; i < gameStack.size(); i++) {
                if(!gameStack.stack.elementAt(i).isHidden()){
                   if(gameStack.size() == i+1){
                       for (FinishStack finishStack : finishStacks) {
                           if(!finishStack.isEmpty()) {
                               if (finishStack.stack.peek().getCardValue() == gameStack.stack.elementAt(i).getCardValue() - 1) {
                                   if (finishStack.stack.peek().getSuit() == gameStack.stack.elementAt(i).getSuit()) {
                                       String returnString = "Move " + gameStack.stack.elementAt(i).getCardValue() + " of " + gameStack.stack.elementAt(i).getSuit() + " to " + finishStack.stack.peek().getCardValue() + " of " + finishStack.stack.peek().getSuit() + " on finish stack";
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
        for (GameStack gameStackEmpty : gameStacks) {
            index++;
            if(gameStackEmpty.isEmpty()){
                for (GameStack gameStack : gameStacks) {
                    for (int i = 0; i < gameStack.size(); i++) {
                        if(!gameStack.stack.elementAt(i).isHidden()){
                            if(gameStack.stack.elementAt(i).getCardValue() == 13 && i != 0){
                                String returnString = "Move 13 of " + gameStack.stack.elementAt(i).getSuit() + " to empty stack " + index;
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
        if(!turnedStock.isEmpty()){
            if(turnedStock.stack.peek().getCardValue() == 1){
                String returnString = null;
                for (FinishStack finishStack : finishStacks) {
                    if(finishStack.isEmpty()){
                        returnString = "Move 1 of " + turnedStock.stack.peek().getSuit() + " from stock to empty finish stack";
                        finishStack.addCard(turnedStock.stack.pop());
                    }
                }
                return returnString;

            }
        }
        for (GameStack gameStack : gameStacks) {
            if(!gameStack.isEmpty() && !turnedStock.isEmpty()) {
                if (turnedStock.stack.peek().getColor() != gameStack.stack.peek().getColor()) {
                    if (turnedStock.stack.peek().getCardValue() == gameStack.stack.peek().getCardValue() - 1) {
                        String returnString = "Move " + turnedStock.stack.peek().getCardValue() + " of " + turnedStock.stack.peek().getSuit() + " to " + gameStack.stack.peek().getCardValue() + " of " + gameStack.stack.peek().getSuit();
                        gameStack.addCard(turnedStock.stack.pop());
                        return returnString;
                    }
                }
            }
        }
        return null;
    }

    private String turnNewCardFromStock(){
        String returnString = "Turn new card from the stock";
        if(stock.size() > 0){
            //TODO Add revealed card to gameState.
            return returnString;
        }else if(turnedStock.size() != 0){
            stock.addCardsReversed(turnedStock.takeCards(turnedStock.stack.size()));
            return returnString;
        }else return null;
    }

    private String turnNewCardFromStockTest(){

        if(stock.size() > 0){
            String returnString = "Turn new card from the stock";
            turnedStock.stack.add(stock.stack.pop());
            turnedStock.stack.peek().setHidden(false);
            return returnString;
        }else if(turnedStock.size() > 0){
            String returnString = "Turn the stock over, then turn new card from the stock";
            stock.addCardsReversed(turnedStock.takeCards(0));
            for (Card card : stock.stack) {
                card.setHidden(true);
            }

            turnedStock.stack.add(stock.stack.pop());
            turnedStock.stack.peek().setHidden(false);
            return returnString;
        }else return null;
    }

    private String cardToFinishStack(){
        String returnString;
        for (GameStack gameStack : gameStacks) {
            for (FinishStack finishStack : finishStacks) {
                if(!gameStack.isEmpty() && !finishStack.isEmpty()) {
                    if (gameStack.stack.peek().getSuit() == finishStack.stack.peek().getSuit()) {
                        if (gameStack.stack.peek().getCardValue() == finishStack.stack.peek().getCardValue() + 1) {
                            returnString = "Move " + gameStack.stack.peek().getCardValue() + " of " + gameStack.stack.peek().getSuit() + " to " + finishStack.stack.peek().getCardValue() + " of " + finishStack.stack.peek().getSuit();
                            finishStack.stack.add(gameStack.stack.pop());
                            return returnString;
                        }
                    }
                }
            }
        }
        return null;
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
        if(returnString != null){
            return returnString;
        }returnString = playCardFromTurnedStock();
        if(returnString != null){
            return returnString;
        }
        returnString = turnNewCardFromStockTest();
        if(returnString != null){
            return returnString;
        }
        returnString = lastVisibleCardToFinishStack();
        if(returnString != null){
            return returnString;
        }
        returnString = cardToFinishStack();
        return returnString;



    }

    public CardStack getStock() {
        return stock;
    }

    public CardStack getTurnedStock() {
        return turnedStock;
    }

    public GameStack[] getGameStacks() {
        return gameStacks;
    }

    public FinishStack[] getFinishStacks() {
        return finishStacks;
    }

    public void setStock(CardStack stock) {
        this.stock = stock;
    }

    public void setTurnedStock(CardStack turnedStock) {
        this.turnedStock = turnedStock;
    }

    public void setGameStacks(GameStack[] gameStacks) {
        this.gameStacks = gameStacks;
    }

    public void setFinishStacks(FinishStack[] finishStacks) {
        this.finishStacks = finishStacks;
    }
}
