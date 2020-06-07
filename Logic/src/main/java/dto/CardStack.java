package dto;

import java.util.Stack;

public class CardStack {

    Stack<Card> stack = new Stack<Card>();

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
        Stack<Card> cards = new Stack<Card>();

        int cardsRemoved = 0;
        for (int i = stack.size()-1; i >= index; i--) {
            cards.add(stack.elementAt(i));
            cardsRemoved++;
        }
        for (int i = 0; i < cardsRemoved; i++) {
            stack.pop();
        }
        return cards;
    }

    void addCards(Stack<Card> cards){
        for (int i = 0; i < cards.size(); i++) {
            stack.add(cards.pop());
        }
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
