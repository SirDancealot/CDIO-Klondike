package model.parser;

import model.dto.Card;
import model.dto.GameState;

public class DeckDecoder {
	public static GameState decode(String encoded) {
		GameState state = new GameState();
		char[] chars = encoded.toCharArray();
		int index = 0;
		while (index < chars.length) {
			for (int gStackNum = 0; gStackNum < 7; gStackNum++) {
				int gStackSize = chars[index++];
				for (int i = 0; i < gStackSize; i++, index++) {
					int suit = ((int) chars[index] & 0xF);
					int value = ((int) chars[index] & 0xF0) >> 4;
					Card card = new Card(Card.Suit.values()[suit], value);

					state.getGameStacks()[gStackNum].addCard(card);
				}
			}
			for (int i = 0; i < 4; i++, index++) {
				int suit = ((int) chars[index] & 0xF);
				int value = ((int) chars[index] & 0xF0) >> 4;
				Card card = new Card(Card.Suit.values()[suit], value);
				state.getFinishStacks()[i].addCard(card);
			}
			if (chars[index++] != 0xFF)
				state.getStock().addCard(new Card());
			if (chars[index++] != 0xFF) {
				int suit = ((int) chars[index] & 0xF);
				int value = ((int) chars[index] & 0xF0) >> 4;
				Card card = new Card(Card.Suit.values()[suit], value);
				state.getTurnedStock().addCard(card);
			}

		}

		return state;
	}
}
