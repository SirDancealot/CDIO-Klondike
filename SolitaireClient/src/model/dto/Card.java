package model.dto;

import javafx.beans.property.*;

public class Card {
    private static final String[] numberNames = {"Hidden","Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};

    public enum Suit {
        HIDDEN, HEARTS, SPADES, DIAMONDS, CLUBS
    }

    public enum Color {
        BLACK, RED
    }

    private Color color;
    private ObjectProperty<Suit> suit = null;
    private IntegerProperty cardValue = null;
    private StringProperty stringValue = null;
    private boolean hidden = true;
    private boolean isKnown = false;

    public Card(Suit suit, int cardValue) {
        this.hidden = false;
        this.suit = new SimpleObjectProperty<>(suit);
        this.cardValue = new SimpleIntegerProperty(cardValue);
        this.stringValue = new SimpleStringProperty(numberNames[cardValue]);
        if(suit == Suit.HIDDEN){
            hidden = true;
        } else if(suit == Suit.SPADES || suit == Suit.CLUBS){
            this.color = Color.BLACK;
        }else this.color = Color.RED;
    }

    public void setSuit(Suit suit) {
        this.suit.setValue(suit);
        if(suit == Suit.HIDDEN){
            hidden = true;
            if(cardValue.get() != 0) {
                setCardValue(0);
            }
        } else if(suit == Suit.SPADES || suit == Suit.CLUBS){
            this.color = Color.BLACK;
        } else this.color = Color.RED;
    }

    public IntegerProperty getCardValue() {
        return cardValue;
    }
    public void setCardValue(int cardValue) {
        this.cardValue.set(cardValue);
        this.stringValue.set(numberNames[cardValue]);
        if(cardValue == 0) {
            hidden = true;
            if(suit.getValue() != Suit.HIDDEN) {
                setSuit(Suit.HIDDEN);
            }
        }
    }

    public boolean canMoveToOnGameStack(Card card){
        return(color != card.getColor() && cardValue.getValue() == card.getCardValue().getValue() - 1);
    }

    public boolean canMoveToOnFinishStack(Card card){
        return(suit.getValue() == card.getSuit().getValue() && cardValue.getValue() == card.getCardValue().getValue() + 1);
    }

    public void setKnown(boolean known) {
        isKnown = known;
    }
    public boolean isKnown() {
        return isKnown;
    }

    public Color getColor(){
        return color;
    }

    public StringProperty getStringValue() {
        return stringValue;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
    public boolean isHidden() {
        return hidden;
    }

    public ObjectProperty<Suit> getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        if (hidden)
            return "Hidden";
        return numberNames[cardValue.getValue()] + " of " + suit.getValue().toString();
    }
}
