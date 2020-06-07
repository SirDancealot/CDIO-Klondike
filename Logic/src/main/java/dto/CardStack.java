package dto;

import java.util.ArrayList;
import java.util.Stack;

public class CardStack {
	private Stack<Card> stack = new Stack<Card>();
	private ArrayList<Card> tmp = new ArrayList<Card>();
	private int movable = 0;

	public void addCard(Card c) {
		stack.add(c);
		calculateMovable();
	}

	public void addStack(CardStack cStack) {

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

	public Card cardAt(int index) {
		return stack.elementAt(index);
	}

	@Override
	public String toString() {
		String s = "CardStack{";
		for (Card c : stack) {
			s = s.concat(("\nCard = " + c.toString()));
		}
		return  s.concat("\n}");
	}
}
