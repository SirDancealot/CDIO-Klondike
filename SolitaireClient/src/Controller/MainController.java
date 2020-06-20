package Controller;

import model.dto.Card;
import model.dto.GameStack;
import model.dto.GameState;

import java.io.IOException;

public class MainController {
	private static volatile MainController INSTANCE = null;

	private CommController comm;
	private LogicController logic;
	private volatile ViewController view = null;

	private MainController() {
		INSTANCE = this;
		logic = LogicController.getInstance();
		comm = CommController.getINSTANCE();
	}

	public static MainController getINSTANCE() {
		if (INSTANCE == null)
			return new MainController();
		return INSTANCE;
	}

	public void run() throws IOException {
		while (view == null) {}

		//logic.setGameState(comm.requestNewState());
		logic.setupGame(comm.requestNewState());
		view.updateView(logic.getGameState());
		boolean requestImage = false;
		String returnString = null;

		while (true) {
			if (view.requestNextMove()) {
				if (requestImage) {
					requestImage = false;
					updateGameState(returnString, comm.requestNewState());
				}
				view.setNextMove(false);
				returnString = logic.makeMoveTest();
				if (returnString.contains("hidden"))
					requestImage = true;
				view.setText(returnString.isEmpty() ? "You lost" : returnString);
				view.updateView(logic.getGameState());
			}
		}
	}

	private void updateGameState(String returnString, GameState imgState) {
		if (returnString.equals("Turn new hidden card from the stock")) {
			logic.getGameState().getTurnedStock().getTopCard().setSuit(imgState.getTurnedStock().getTopCard().getSuit().getValue());
			logic.getGameState().getTurnedStock().getTopCard().setCardValue(imgState.getTurnedStock().getTopCard().getCardValue().get());

		} else {
			int i = Integer.parseInt(returnString.split(" ")[5]) - 1;
			int expected[] = new int[7], sizes[] = new int[7];
			for (int j = 0; j < 7; j++) {
				expected[i] = logic.getGameState().getGameStacks()[i].getMovable();
				sizes[i] = imgState.getGameStacks()[i].getMovable();
			}


		}
	}

	public void setViewController(ViewController viewController) {
		view = viewController;
	}
}
