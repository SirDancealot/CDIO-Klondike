package model.dto;

public class GameStack extends CardStack {
	private int movable = 0;

	public void addCard(Card c) {
		stack.add(c);
		calculateMovable();
	}

	public void addStack(GameStack cStack) {
		calculateMovable();
	}

	private void calculateMovable() {
		boolean lastRed = false;
		boolean oppositeLast = true;
		boolean lastHidden = false;
		int movable = 0;

		for (int i = 0; i < stack.size(); i++) {
			if (i == 0) {
				movable++;
			} else {

			}

		}

		this.movable = movable;
	}

	public int getMovable() {
		return this.movable;
	}


}
