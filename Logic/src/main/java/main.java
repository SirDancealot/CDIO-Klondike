import dto.Card;
import dto.GameStack;
import dto.GameState;
import parser.CardDeck;
import parser.DeckDecoder;

public class main {
    public static void main(String[] args) {
        CardDeck deck = CardDeck.getINSTANCE();
        GameState game = new GameState();
        GameStack[] gameStacks = game.getGameStacks();

        DeckDecoder.decode("\u0002ÑÄ\u0001£\u0001\"\u0001T\u0001Q\u0001¢\u0001\u0081\u0013\u0014\u0012\u0011ÿS");

        for (int i = 0; i < 5; i++) {
            Card c = deck.draw();
            if (i == 4) c.setHidden(false);
            gameStacks[1].addCard(c);
        }

        System.out.println(game.getGameStacks()[1]);
    }
}
