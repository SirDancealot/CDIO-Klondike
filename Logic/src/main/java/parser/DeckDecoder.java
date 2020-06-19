package parser;

import dto.Card;
import dto.GameState;

public class DeckDecoder {
	public static GameState decode(byte[] bytes) {
		GameState state = new GameState();
		int index = 0;
		while (index < bytes.length) {
			for (int gStackNum = 0; gStackNum < 7; gStackNum++) {
				int gStackSize = bytes[index++];
				for (int i = 0; i < gStackSize; i++, index++) {
					int suit = ((int) bytes[index] & 0xF);
					int value = ((int) bytes[index] & 0xF0) >> 4;
					Card card = new Card(Card.Suit.values()[suit], value);

					state.getGameStacks()[gStackNum].addCard(card);
				}
			}
			for (int i = 0; i < 4; i++, index++) {
				int suit = ((int) bytes[index] & 0xF);
				int value = ((int) bytes[index] & 0xF0) >> 4;
				Card card = new Card(Card.Suit.values()[suit], value);
				state.getFinishStacks()[i].addCard(card);
			}
			if (bytes[index++] != 0)
				state.getStock().addCard(new Card(Card.Suit.Hidden, 0));
			if (bytes[index++] != 0) {
				int suit = ((int) bytes[index] & 0xF);
				int value = ((int) bytes[index] & 0xF0) >> 4;
				Card card = new Card(Card.Suit.values()[suit], value);
				state.getTurnedStock().addCard(card);
			}

		}

		return state;
	}
}
