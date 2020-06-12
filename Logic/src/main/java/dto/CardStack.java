package dto;

import java.util.Collections;
import java.util.Stack;

public class CardStack {

    Stack<Card> stack = new Stack<>();

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

    Stack<Card> takeCards(int index){
        Stack<Card> cards = new Stack<>();

        for (int i = stack.size()-1; i >= index; i--) {
            cards.add(stack.pop());
        }

        return cards;
    }

    void addCards(Stack<Card> cards){
        int cardSize = cards.size();
        for (int i = 0; i < cardSize; i++) {
            stack.add(cards.pop());
        }
    }

    void addCardsReversed(Stack<Card> cards){
        for (int i = cards.size()-1; i >= 0; i--) {
            stack.add(cards.elementAt(i));
        }
    }

    void shuffle(){
        Collections.shuffle(stack);
    }

    @Override
    public String toString() {
        String s = "CardStack{";
        for (Card c : stack) {
            s = s.concat(("\nCard = " + c.toString()));
        }
        return  s.concat("\n}");
    }
}
