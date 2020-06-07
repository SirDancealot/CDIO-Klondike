package dto;

public class GameState {
    private GameStack stock = new GameStack();
    private GameStack turnedStock = new GameStack();
    private GameStack[] gameStacks = new GameStack[7];
    private GameStack[] finishStacks = new GameStack[4];

    public GameState() {
        for (int i = 0; i < 7; i++) {
            gameStacks[i] = new GameStack();
        }
        for (int i = 0; i < 4; i++) {
            finishStacks[i] = new GameStack();
        }
    }

    public GameStack getStock() {
        return stock;
    }

    public GameStack getTurnedStock() {
        return turnedStock;
    }

    public GameStack[] getGameStacks() {
        return gameStacks;
    }

    public GameStack[] getFinishStacks() {
        return finishStacks;
    }
}
