import dto.Card;
import dto.GameStack;
import dto.GameState;
import parser.CardDeck;

public class main {
    public static void main(String[] args) {
        CardDeck deck = CardDeck.getINSTANCE();
        GameState game = new GameState();
        GameStack[] gameStacks = game.getGameStacks();

        for (int i = 0; i < 5; i++) {
            Card c = deck.draw();
            if (i == 4) c.setHidden(false);
            gameStacks[1].addCard(c);
        }

        System.out.println(game.getGameStacks()[1]);
    }
}
