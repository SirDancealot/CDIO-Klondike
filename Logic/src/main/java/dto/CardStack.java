package dto;

import java.util.Collections;
import java.util.Stack;

public class CardStack {

    public Stack<Card> stack = new Stack<>();

    public void addCard(Card c) {
        stack.add(c);
    }

    public Card getTopCard(){
        if(stack.empty()) return null;
        return stack.peek();
    }

    public int size(){
        return stack.size();
    }

    public boolean isEmpty(){
        return stack.isEmpty();
    }

    public Card getCardAt(int index) {
        return stack.elementAt(index);
    }

    public Stack<Card> takeCards(int index){
        Stack<Card> cards = new Stack<>();

        for (int i = stack.size()-1; i >= index; i--) {
            cards.add(stack.pop());
        }

        return cards;
    }

    public void addCards(Stack<Card> cards){
        int cardSize = cards.size();
        for (int i = 0; i < cardSize; i++) {
            stack.add(cards.pop());
        }
    }

    public void addCardsReversed(Stack<Card> cards){
        for (int i = cards.size()-1; i >= 0; i--) {
            stack.add(cards.elementAt(i));
        }
    }

    public void shuffle(){
        Collections.shuffle(stack);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CardStack{");
        for (Card c : stack) {
            sb.append("\nCard = ").append(c.toString());
        }
        return  sb.append("\n}").toString();
    }
}
