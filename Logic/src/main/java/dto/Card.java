package dto;

public class Card {
    private static final String[] numberNames = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};

    public enum Suit {
        HEARTS, SPADES, DIAMONDS, CLUBS
    }

    public enum Color {
        BLACK, RED
    }

    private Color color;
    private Suit suit;
    private int cardValue;
    private boolean hidden = true;
    private boolean isKnown = false;

    public boolean isKnown() {
        return isKnown;
    }

    public void setKnown(boolean known) {
        isKnown = known;
    }

    public Card(Suit suit, int cardValue) {
        this.suit = suit;
        this.cardValue = cardValue;
        if(suit == Suit.SPADES || suit == Suit.CLUBS){
            this.color = Color.BLACK;
        }else this.color = Color.RED;
    }

    public Color getColor(){
        return color;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
        if(suit == Suit.SPADES || suit == Suit.CLUBS){
            this.color = Color.BLACK;
        }else this.color = Color.RED;
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
