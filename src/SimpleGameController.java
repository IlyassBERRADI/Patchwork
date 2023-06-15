
import java.awt.Color;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

import fr.umlv.zen5.Application;
import fr.umlv.zen5.ApplicationContext;

/**
 * A class that implements the game's loop and handles the actions of the player
 * 
 * @author Antoine BENOIT
 * @author Ilyass BERRADI
 * @since 07/05/2023
 *
 */
public class SimpleGameController {
  
  
  
  /**
   * Sets up the game, then launches the game loop.
   * 
   * @param context {@code ApplicationContext} of the game.
   */
  private static void memoryGame(ApplicationContext context) {
      Objects.requireNonNull(context);
      Player player1 = new Player();
      Player player2 = new Player();
      GameView gameView;
      var data = new SimpleGameData(player1, player2);
      if (data.getVersion()==1) {
        gameView= new ConsoleGameView();
        
      }
      else {
        var screenInfo = context.getScreenInfo();
        var width = screenInfo.getWidth();
        var height = screenInfo.getHeight();
        var marginX = 300;
        var marginY = 200;
        gameView = SimpleGameView.initGameGraphics(marginX+50, marginY-50, (int) Math.min(width, height) - 2 * marginY+10, data,
             (int)width, (int)height);
      }
      
      
      
      while (!data.endGame()) {
          gameView.play_game(context, data);
          
      }
      
      if (data.getVersion()==1) {
        ConsoleGameView.winner(data);
      }
  }
  
  
  
  
  
  /**
   * Executable program.
   * 
   * @param args Spurious arguments.
   */
  public static void main(String[] args) {
      
    Application.run(Color.WHITE, (context)->memoryGame(context));
      
      
  }
}
