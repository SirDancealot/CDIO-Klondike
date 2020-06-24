package Controller.runtime;

import Controller.view.ViewController;
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
		view.showLoadingAlert();
		logic.setupGame(comm.requestNewState());
		view.hideLoadingAlert();
		view.updateView(logic.getGameState());
		boolean requestImage = false;
		String returnString = null;
		boolean playing = true;

		while (playing) {
			if (view.requestNextMove()) {
				if (requestImage) {
					requestImage = false;
					view.showLoadingAlert();
					updateGameState(returnString, comm.requestNewState());
					view.hideLoadingAlert();
				}
				view.setNextMove(false);
				if (logic.gameWon()) {
					view.setText("The game has been won");
					break;
				}
				returnString = logic.makeMoveTest();
				if (returnString.contains("hidden"))
					requestImage = true;
				if (returnString.isEmpty()) {
					playing = false;
					returnString = "You lost";
				}
				view.setText(returnString);
				view.updateView(logic.getGameState());
			}
		}
		comm.close();
	}

	public void updateGameState(String returnString, GameState imgState) throws IOException {
		boolean tryAgain = true;
		while (tryAgain) {
			tryAgain = false;
			try {
				if (returnString.equals("Turn new hidden card from the stock")) {
					logic.getGameState().getTurnedStock().getTopCard().setSuit(imgState.getTurnedStock().getTopCard().getSuit().getValue());
					logic.getGameState().getTurnedStock().getTopCard().setCardValue(imgState.getTurnedStock().getTopCard().getCardValue().get());

				}
				else {
					System.out.println(returnString);
					int i = Character.getNumericValue(returnString.charAt(returnString.length()-1))-1;
					System.out.println(i);
					GameStack[] gStacks = logic.getGameState().getGameStacks();
					int gStackLen[] = new int[7];
					int imgStackLen[] = new int[7];
					for (int j = 0; j < 7; j++) {
						gStackLen[j] = gStacks[j].getMovable();
						imgStackLen[j] = imgState.getGameStacks()[j].getMovable();
					}
					gStackLen[i]++;

					int a = 0, b = 0;
					while (a < i) {
						if ((gStackLen[a] != 0 && imgStackLen[b] != 0) || (gStackLen[a] == 0 && imgStackLen[b] == 0)) {
							b++;
						}
						a++;
					}
					logic.getGameState().getGameStacks()[a].getTopCard().setCardValue(imgState.getGameStacks()[b].getTopCard().getCardValue().getValue());
					logic.getGameState().getGameStacks()[a].getTopCard().setSuit(imgState.getGameStacks()[b].getTopCard().getSuit().get());
				}
			} catch (Exception e) {
				tryAgain = true;
				System.out.println("Error in image, trying again");
				imgState = CommController.getINSTANCE().requestNewState();
			}

		}
	}

	public void setViewController(ViewController viewController) {
		view = viewController;
	}
}
