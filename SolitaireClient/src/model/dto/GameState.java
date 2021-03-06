/**
 * @author Rasmus Traub Nielsen s185101
 * @author Michael Jeppesen s185123
 */

package model.dto;

public class GameState {
    private CardStack stock = new CardStack();
    private CardStack turnedStock = new CardStack();
    private GameStack[] gameStacks = new GameStack[7];
    private FinishStack[] finishStacks = new FinishStack[4];

    private static GameState primaryGameState = null;

    public static GameState getPrimary() {
        if (primaryGameState == null) {
            primaryGameState = new GameState();
            for (int i = 0; i < 23; i++) {
                primaryGameState.getStock().addCard(new Card(Card.Suit.HIDDEN, 0));
            }
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j <= i; j++) {
                    primaryGameState.getGameStacks()[i].addCard(new Card(Card.Suit.HIDDEN, 0));
                }
            }
            primaryGameState.turnedStock.addCard(new Card(Card.Suit.HIDDEN, 0));
        }
        return primaryGameState;
    }

    public static void setPrimary(GameState state) {
        primaryGameState = state;
    }

    public GameState() {
        for (int i = 0; i < 7; i++) {
            gameStacks[i] = new GameStack();
        }
        for (int i = 0; i < 4; i++) {
            finishStacks[i] = new FinishStack();
        }
    }
    public CardStack getStock() {
        return stock;
    }

    public CardStack getTurnedStock() {
        return turnedStock;
    }

    public GameStack[] getGameStacks() {
        return gameStacks;
    }

    public FinishStack[] getFinishStacks() {
        return finishStacks;
    }

    public void setStock(CardStack stock) {
        this.stock = stock;
    }

    public void setTurnedStock(CardStack turnedStock) {
        this.turnedStock = turnedStock;
    }

    public void setGameStacks(GameStack[] gameStacks) {
        this.gameStacks = gameStacks;
    }

    public void setFinishStacks(FinishStack[] finishStacks) {
        this.finishStacks = finishStacks;
    }
}
