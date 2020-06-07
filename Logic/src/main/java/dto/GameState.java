package dto;

public class GameState {
    private CardStack stock = new CardStack();
    private CardStack turnedStock = new CardStack();
    private CardStack[] gameStacks = new CardStack[7];
    private CardStack[] finishStacks = new CardStack[4];

    public GameState() {
        for (int i = 0; i < 7; i++) {
            gameStacks[i] = new CardStack();
        }
        for (int i = 0; i < 4; i++) {
            finishStacks[i] = new CardStack();
        }
    }

    public CardStack getStock() {
        return stock;
    }

    public CardStack getTurnedStock() {
        return turnedStock;
    }

    public CardStack[] getGameStacks() {
        return gameStacks;
    }

    public CardStack[] getFinishStacks() {
        return finishStacks;
    }
}
