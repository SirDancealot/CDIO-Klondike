package dto;

public class GameState {
    private CardStack stock = new CardStack();
    private CardStack turnedStock = new CardStack();
    private GameStack[] gameStacks = new GameStack[7];
    private FinishStack[] finishStacks = new FinishStack[4];

    private static GameState gameState_instance = null;

    public static GameState getInstance()
    {
        if (gameState_instance == null)
            gameState_instance = new GameState();

        return gameState_instance;
    }

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
                gameStack.stack.peek().setKnown(true);
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
        for (GameStack gameStack : gameStacks) {
            index++;
            if(!gameStack.isEmpty()) {
                if (gameStack.stack.peek().getCardValue() == 1 && !gameStack.stack.peek().isHidden()) {
                    card = gameStack.stack.pop();
                    for (FinishStack finishStack : finishStacks) {
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
        for (GameStack gameStackTake : gameStacks) {
            for (int i = 0; i < gameStackTake.size(); i++) {
                if(!gameStackTake.stack.elementAt(i).isHidden()){
                    for (GameStack gameStackReceive : gameStacks) {
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
        for (GameStack gameStack : gameStacks) {
            for (int i = 0; i < gameStack.size(); i++) {
                if(!gameStack.stack.elementAt(i).isHidden()){
                   if(gameStack.size() == i+1){
                       for (FinishStack finishStack : finishStacks) {
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
        for (GameStack gameStackEmpty : gameStacks) {
            index++;
            if(gameStackEmpty.isEmpty()){
                for (GameStack gameStack : gameStacks) {
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
        if(!turnedStock.isEmpty()){
            if(turnedStock.stack.peek().getCardValue() == 13){
                String returnString = null;
                int index = 0;
                for (GameStack gameStack : gameStacks) {
                    index++;
                    if(gameStack.isEmpty()){
                        returnString = "Move " + turnedStock.stack.peek().toString() + " from stock to stack " + index;
                        gameStack.addCard(turnedStock.stack.pop());
                        return returnString;
                    }
                }
            }
        }
        if(!turnedStock.isEmpty()){
            if(turnedStock.stack.peek().getCardValue() == 1){
                String returnString = null;
                for (FinishStack finishStack : finishStacks) {
                    if(finishStack.isEmpty()){
                        returnString = "Move " + turnedStock.stack.peek().toString() + " from stock to empty finish stack";
                        finishStack.addCard(turnedStock.stack.pop());
                        return returnString;
                    }
                }
            }
        }
        for (GameStack gameStack : gameStacks) {
            if(!gameStack.isEmpty() && !turnedStock.isEmpty()) {
                if (turnedStock.stack.peek().getColor() != gameStack.stack.peek().getColor()) {
                    if (turnedStock.stack.peek().getCardValue() == gameStack.stack.peek().getCardValue() - 1) {
                        String returnString = "Move " + turnedStock.stack.peek().toString() +  " to " + gameStack.stack.peek().toString();
                        gameStack.addCard(turnedStock.stack.pop());
                        return returnString;
                    }
                }
            }
        }
        for (FinishStack finishStack : finishStacks) {
            if(!finishStack.isEmpty() && !turnedStock.isEmpty()) {
                if (turnedStock.stack.peek().getSuit() == finishStack.stack.peek().getSuit()) {
                    if (turnedStock.stack.peek().getCardValue() == finishStack.stack.peek().getCardValue() + 1) {
                        String returnString = "Move " + turnedStock.stack.peek().toString() +  " to " + finishStack.stack.peek().toString() + " on finish stack";
                        finishStack.addCard(turnedStock.stack.pop());
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
            turnedStock.stack.peek().setKnown(true);
            return returnString;
        }else if(turnedStock.size() > 0){
            String returnString = "Turn the stock over, then turn new card from the stock";
            stock.addCardsReversed(turnedStock.takeCards(0));
            for (Card card : stock.stack) {
                card.setHidden(true);
            }

            turnedStock.stack.add(stock.stack.pop());
            turnedStock.stack.peek().setHidden(false);
            turnedStock.stack.peek().setKnown(true);
            return returnString;
        }else return null;
    }

    public int countCardsInGame(){
        int cards = 0;
        for (GameStack gameStack : gameStacks) {
            cards += gameStack.size();
        }
        for (FinishStack finishStack : finishStacks) {
            cards += finishStack.size();
        }
        cards += stock.size();
        cards += turnedStock.size();
        return cards;
    }

    private String cardToFinishStack(){
        String returnString;
        for (GameStack gameStack : gameStacks) {
            for (FinishStack finishStack : finishStacks) {
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
        for (GameStack gameStackTake : gameStacks) {
            int nonHiddenCardsBeforeThisInTake = 0;
            for (int i = 0; i < gameStackTake.stack.size(); i++) {
                if(!gameStackTake.stack.elementAt(i).isHidden()){
                    for (GameStack gameStackReceive : gameStacks) {
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
        for (Card card : stock.stack) {
            if(!card.isKnown()){
                return true;
            }
            if(card.getCardValue() == 1){
                return true;
            }
            for (GameStack gameStack : gameStacks) {
                if(!gameStack.isEmpty()) {
                    if ((card.getCardValue() == gameStack.stack.peek().getCardValue() - 1) && (card.getColor() != gameStack.stack.peek().getColor())) {
                        return true;
                    }
                }
                if(gameStack.isEmpty() && card.getCardValue() == 13){
                    return true;
                }
            }
            for (FinishStack finishStack : finishStacks) {
                if(!finishStack.isEmpty()) {
                    if ((card.getCardValue() == finishStack.stack.peek().getCardValue() + 1) && (card.getSuit() == finishStack.stack.peek().getSuit())) {
                        return true;
                    }
                }

            }
        }
        for (Card card : turnedStock.stack) {
            if(!card.isKnown()){
                return true;
            }
            if(card.getCardValue() == 1){
                return true;
            }
            for (GameStack gameStack : gameStacks) {
                if(!gameStack.isEmpty()) {
                    if ((card.getCardValue() == gameStack.stack.peek().getCardValue() - 1) && (card.getColor() != gameStack.stack.peek().getColor())) {
                        return true;
                    }
                }
                if(gameStack.isEmpty() && card.getCardValue() == 13){
                    return true;
                }
            }
            for (FinishStack finishStack : finishStacks) {
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
        for (FinishStack finishStack : finishStacks) {
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
