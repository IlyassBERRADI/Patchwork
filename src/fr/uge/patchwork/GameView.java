package fr.uge.patchwork;
import java.awt.Graphics2D;
import java.util.InputMismatchException;
import java.util.Scanner;

import fr.umlv.zen5.ApplicationContext;


/**
 * An interface representing the game's interface
 * 
 * @author Antoine BENOIT
 * @author Ilyass BERRADI
 * @since 07/05/2023
 *
 */
public interface GameView {
  
  
  /**
   * Goes once in the game loop, which consists in retrieving user actions,
   * transmissing it to the GameView and GameData, and dealing with time events.
   * 
   * @param context {@code ApplicationContext} of the game.
   * @param data    GameData of the game.
   */
  public void play_game(ApplicationContext context, SimpleGameData data);
  
  
}
