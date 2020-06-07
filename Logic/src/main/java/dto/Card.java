package dto;

public class Card {
    private static final String[] numberNames = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};

    public enum Suit {
        HEARTS, SPADES, DIAMONDS, CLUBS
    }

    private Suit suit;
    private int cardValue;
    private boolean hidden = true;

    public Card(Suit suit, int cardValue) {
        this.suit = suit;
        this.cardValue = cardValue;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public int getCardValue() {
        return cardValue;
    }

    public void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }

    @Override
    public String toString() {
        if (hidden)
            return "Hidden";
        return numberNames[cardValue-1] + " of " + suit.toString();
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isRed() {
        if (hidden) return false;
        return suit == Suit.DIAMONDS || suit == Suit.HEARTS;

    }

    public boolean isBlack() {
        if (hidden) return false;
        return suit == Suit.CLUBS || suit == Suit.SPADES;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
