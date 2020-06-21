package model.dto;

public class GameStack extends CardStack {

	public void addCard(Card c) {
		stack.add(c);
	}

	public int getMovable() {
		int movable = 0;
		for (Card c : stack) {
			if (!c.isHidden())
				movable++;
		}
		return movable;
	}


}
