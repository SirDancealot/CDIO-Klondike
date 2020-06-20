package Controller;

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

		logic.setGameState(comm.requestNewState());
		view.updateView(logic.getGameState());

		while (true) {
			//view.updateView(logic.getGameState());
		}
	}

	public void setViewController(ViewController viewController) {
		view = viewController;
	}
}
