/**
 * @author Michael Jeppesen s185123
 * @author Rasmus Traub Nielsen s185101
 */

package controller.runtime;

import controller.view.ViewController;

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
					logic.updateGameState(returnString, comm.requestNewState());
					view.hideLoadingAlert();
				}
				view.setNextMove(false);
				if (logic.gameWon()) {
					view.setText("The game has been won");
					break;
				}
				returnString = logic.makeMove();
				if (returnString == null) {
					playing = false;
					returnString = "You lost";
				}
				if (returnString.contains("hidden"))
					requestImage = true;
				view.setText(returnString);
				view.updateView(logic.getGameState());
			}
		}
		comm.close();
	}

	public void setViewController(ViewController viewController) {
		view = viewController;
	}
}
