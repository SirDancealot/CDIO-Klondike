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

        String decodeString = "\u0001\u0011\u0001\u0094\u0001b\u0001\u0091\u0001c\u0003AÂr\u0005\u0084£ÁC!aQ2\"3";

        System.out.println("Printing Bytes");
        for (byte b : decodeString.getBytes()) {
            System.out.println(Integer.toBinaryString(b));
        }

        System.out.println("Decoding String");
        DeckDecoder.decode(decodeString);



        for (int i = 0; i < 5; i++) {
            Card c = deck.draw();
            if (i == 4) c.setHidden(false);
            gameStacks[1].addCard(c);
        }

        System.out.println(game.getGameStacks()[1]);
    }
}
