package dto;

import com.sun.tools.javac.comp.Todo;

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

    private boolean turnHiddenCard(){
        //TODO Add revealed card to gameState.
        Card card;
        for (GameStack gameStack : gameStacks) {
            if(gameStack.stack.peek().isHidden()){
                return true;
            }
        }
        return false;
    }
    private boolean aceToFinishStack(){
        Card card;
        for (GameStack gameStack : gameStacks) {
            if(gameStack.stack.peek().getCardValue() == 1){
                card = gameStack.stack.pop();
                for (FinishStack finishStack : finishStacks) {
                    if(finishStack.isEmpty()){
                        finishStack.stack.add(card);
                        break;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean fullVisibleStackToStack(){
        for (GameStack gameStackTake : gameStacks) {
            for (int i = 0; i < gameStackTake.size(); i++) {
                if(!gameStackTake.stack.elementAt(i).isHidden()){
                    for (GameStack gameStackReceive : gameStacks) {
                        if(gameStackReceive.stack.peek().getColor() != gameStackTake.stack.elementAt(i).getColor()){
                            if(gameStackReceive.stack.peek().getCardValue() == gameStackTake.stack.elementAt(i).getCardValue()+1){
                                gameStackReceive.addCards(gameStackTake.takeCards(i));
                                return true;
                            }
                        }
                    }
                    break;
                }
            }
        }
        return false;
    }

    private boolean lastVisibleCardToFinishStack(){
        for (GameStack gameStack : gameStacks) {
            for (int i = 0; i < gameStack.size(); i++) {
                if(!gameStack.stack.elementAt(i).isHidden()){
                   if(gameStack.size() == i+1){
                       for (FinishStack finishStack : finishStacks) {
                           if(finishStack.stack.peek().getCardValue() == gameStack.stack.elementAt(i).getCardValue()-1){
                               if(finishStack.stack.peek().getSuit() == gameStack.stack.elementAt(i).getSuit()){
                                   finishStack.addCards(gameStack.takeCards(i));
                                   return true;
                               }
                           }
                       }
                   }
                   break;
                }
            }
        }
        return false;
    }

    private boolean kingToEmptyStack(){
        for (GameStack gameStackEmpty : gameStacks) {
            if(gameStackEmpty.isEmpty()){
                for (GameStack gameStack : gameStacks) {
                    for (int i = 0; i < gameStack.size(); i++) {
                        if(!gameStack.stack.elementAt(i).isHidden()){
                            if(gameStack.stack.elementAt(i).getCardValue() == 13){
                                gameStackEmpty.addCards(gameStack.takeCards(i));
                                return true;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean playCardFromTurnedStock(){
        for (GameStack gameStack : gameStacks) {
            if(turnedStock.stack.peek().getColor() != gameStack.stack.peek().getColor()){
                if(turnedStock.stack.peek().getCardValue() == gameStack.stack.peek().getCardValue()+1){
                    gameStack.addCard(turnedStock.stack.pop());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean turnNewCardFromStock(){

        if(stock.size() > 0){
            //TODO Add revealed card to gameState.
            return true;
        }else if(turnedStock.size() != 0){
            stock.addCardsReversed(turnedStock.takeCards(turnedStock.stack.size()));
            return true;
        }else return false;
    }

    private boolean cardToFinishStack(){
        for (GameStack gameStack : gameStacks) {
            for (FinishStack finishStack : finishStacks) {
                if(gameStack.stack.peek().getSuit() == finishStack.stack.peek().getSuit()){
                    if(gameStack.stack.peek().getCardValue() == finishStack.stack.peek().getCardValue()+1){
                        finishStack.addCards(gameStack.takeCards(gameStack.size()-1));
                        return true;
                    }
                }
            }
        }
        return false;
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
}
