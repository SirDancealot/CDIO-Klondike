package model.Controller;

import java.util.Scanner;

public class Controller {

    public static void main(String[] args){
        Controller controller = new Controller();
    }

    public Controller(){
    }

    /*private void runGame(){
        boolean running = true;
        boolean moveSinceLastStockTurn = true;
        while(running){
            if(logicController.gameWon()){
                System.out.println("YOU WON!");
                break;
            }else {
                String moveString = logicController.makeMoveTest();

                if (moveString == null) {
                    running = false;
                    System.out.println("GAME OVER!");
                } else {
                    if (moveString.equals("Turn the stock over, then turn new card from the stock") && !moveSinceLastStockTurn) {
                        System.out.println("GAME OVER!");
                        break;
                    } else if (!moveString.equals("Turn the stock over, then turn new card from the stock") && !moveString.equals("Turn new card from the stock")) {
                        moveSinceLastStockTurn = true;
                    } else if (moveString.equals("Turn the stock over, then turn new card from the stock")) {
                        moveSinceLastStockTurn = false;
                    }

                    System.out.println(moveString);
                    Scanner scanner = new Scanner(System.in);
                    scanner.nextLine();
                }
            }
        }
    }*/

    private boolean setupGame(){
        return true;
    }
}