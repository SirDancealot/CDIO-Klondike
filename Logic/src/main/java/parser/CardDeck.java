package parser;

import dto.Card;

import java.util.ArrayList;

public class CardDeck implements CardParserI {
    private ArrayList<Card> deck = new ArrayList<Card>();
    private static final CardDeck INSTANCE = new CardDeck();

    private CardDeck() {
        reset();
    }

    public void reset() {
        deck.clear();
        for (Card.Suit s: Card.Suit.values() ) {
            for (int i = 1; i <= 13; i++) {
                deck.add(new Card(s, i));
            }
        }
    }

    public Card draw() {
        return deck.remove((int)(Math.random() * deck.size()));
    }

    public static CardDeck getINSTANCE() {
        return INSTANCE;
    }
}
