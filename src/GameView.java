import java.awt.Graphics2D;
import java.util.InputMismatchException;
import java.util.Scanner;

import fr.umlv.zen5.ApplicationContext;

public interface GameView {
  /*public void announcePlayerTurn();
  public void play();*/
  //int getPlayerChoice(ApplicationContext context, SimpleGameData data);
  //public void announcePlayerTurn(SimpleGameData data, Graphics2D graphics);
  //public void draw(ApplicationContext context, SimpleGameData data);
  /**
   * Puts the patch in a certain position chosen by the player
   * 
   * @param scanner     Reads the player's input
   * @param chosenPiece The piece the player wants to put
   * @param mode        The game mode
   */
  
  
  public void play_game(ApplicationContext context, SimpleGameData data);
  
  
}
