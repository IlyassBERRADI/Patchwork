
import java.awt.Color;
import java.util.InputMismatchException;
import java.util.Scanner;

import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;

public class SimpleGameController {
  
  /*private static boolean gameLoop(ApplicationContext context, SimpleGameData data, SimpleGameView view) {
    
  }*/
  
  
  /**
   * Sets up the game, then launches the game loop.
   * 
   * @param context {@code ApplicationContext} of the game.
   */
  private static void memoryGame(ApplicationContext context) {
      var screenInfo = context.getScreenInfo();
      var width = screenInfo.getWidth();
      var height = screenInfo.getHeight();
      var marginX = 300;
      var marginY = 200;
      //System.out.println("width= "+width+" height= "+height);
      var images = new ImageLoader("data", "patch1.png", "patch2.png", "patch3.png", "patch4.png");
      Player player1 = new Player();
      Player player2 = new Player();
      var data = new SimpleGameData(player1, player2);
      var view = SimpleGameView.initGameGraphics(marginX+50, marginY-50, (int) Math.min(width, height) - 2 * marginY+10, data,
              images, (int)width, (int)height);
      
      
      while (true) {
          /*if (!gameLoop(context, data, view)) {
              System.out.println("Thank you for quitting!");
              context.exit(0);
              return;
          }*/
        SimpleGameView.draw(context, data, view);
        boolean madeChoice = false;
        
        int posOtherPlayer = data.getTurnPlayer().equals(data.getPlayer1()) ? data.getPlayer2().getPosition() : data.getPlayer1().getPosition();
        Patch chosenPiece = null;
        while (!madeChoice) {
          int choice = view.getPlayerChoice(context, data);
          if (choice == 1 || choice == 2 || choice == 3) {
            chosenPiece = view.buyPatch(choice, data, context, view);
            SimpleGameView.draw(context, data, view);
            if (chosenPiece != null) {
              madeChoice = true;
            }
          } else {
            view.moveAfterSkipping(context, data, view);
            SimpleGameView.draw(context, data, view);
            continue;
          }
        }
        view.playAfterBuy(context, chosenPiece, posOtherPlayer, data);
        SimpleGameView.draw(context, data, view);
        //System.out.println("dddd\n");
      }
  }
  
  
  
  /**
   * Allows the player to play his turn
   */
  /*public static void play(ApplicationContext context, SimpleGameData data, GameView view) {
    Scanner scanner = new Scanner(System.in);
    //view.announcePlayerTurn(data, );  we're gonna figure it out later
    boolean madeChoice = false;
    int posOtherPlayer = data.getTurnPlayer().equals(data.getPlayer1()) ? data.getPlayer2().getPosition() : data.getPlayer1().getPosition();
    Patch chosenPiece = null;
    while (!madeChoice) {
      int choice = view.getPlayerChoice(context, data);
      if (choice == 1 || choice == 2 || choice == 3) {
        chosenPiece = data.buyPatch(choice);
        if (chosenPiece != null) {
          madeChoice = true;
        }
      } else {
        data.moveAfterSkipping(scanner);
        return;
      }
    }
    data.playAfterBuy(scanner, chosenPiece, posOtherPlayer);
  }*/
  
  /**
   * Executable program.
   * 
   * @param args Spurious arguments.
   */
  public static void main(String[] args) {
      System.out.println("Welcome to PatchWork!!!\n\n");
      
      //if (data.getVersion()==2) {
      Application.run(Color.WHITE, (context)->memoryGame(context));
      /*}
      else if (data.getVersion()==1) {
        while (!data.endGame()) {
          System.out.println(data);
          data.announcePlayerTurn();
          data.play();
          data.tieWinner();
        }
        data.winner();
      }*/
      
  }
}