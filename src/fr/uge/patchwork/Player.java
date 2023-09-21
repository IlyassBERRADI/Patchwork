package fr.uge.patchwork;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A class representing a player of the game
 * 
 * @author Ilyass BERRADI
 * @author Antoine BENOIT
 * @since 07/05/2023
 *
 */
public class Player {

  /**
   * The number of buttons that the player has
   */
  private int buttons;

  /**
   * The quilt board of the player
   */
  private QuiltBoard board;

  /**
   * The patches owned by the player
   */
  private final ArrayList<Patch> patchsOwned = new ArrayList<>();

  /**
   * The position of the time token of the player on the time board
   */
  private int pos;
  


  /**
   * Constructs a player by initializing its attributes
   */
  public Player() {
    buttons = 5;
    pos = 0;
    board = new QuiltBoard();
  }

  /**
   * Returns the position of the player
   * 
   * @return Player's position
   */
  public int getPosition() {
    return pos;
  }

  /**
   * Returns the patches owned by the player
   * 
   * @return List of the patches owned
   */
  public ArrayList<Patch> getPatchesOwned() {
    return patchsOwned;
  }

  /**
   * returns the number of buttons owned by the player
   * 
   * @return The number of buttons owned by the player
   */
  public int getButtons() {
    return buttons;
  }

  
  
  
  
  /**
   * Returns the quilt board of the player
   * 
   * @return Player's quilt board
   */
  public QuiltBoard getBoard() {
    return board;
  }

  /**
   * Sets the position of the player
   * 
   * @param pos The new player's position
   */
  public void setPosition(int pos) {
    this.pos = pos;
  }

  /**
   * Sets the number of buttons of the player
   * 
   * @param buttons The new player's score of buttons
   */
  public void setButtons(int buttons) {
    this.buttons = buttons;
  }

  /**
   * Adds a new patch to the player's list of patches owned
   * 
   * @param patch Patch to add
   */
  public void addPatchOwned(Patch patch) {
    Objects.requireNonNull(patch);
    patchsOwned.add(patch);
  }

  @Override
  public String toString() {

    return "Position : " + pos + "\n\nButtons : " + buttons + "\n\nBoard : \n" + board;
  }
}
