
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Objects;

/**
 * A class representing the data of the game of Patchwork
 * This class provides all the necessary informations to make the game work
 * 
 * @author Ilyass BERRADI
 * @author Antoine BENOIT
 * @since 07/05/2023
 */

public class SimpleGameData {
  /**
   * The array that represents the time board content
   */
  private final int timeBoard[] = new int[54];
  /**
   * A boolean that tells if a player has chosen an expensive patch
   */
  private boolean notEnoughBut=false;
  
  /**
   * A boolean that represents if a player has chosen a rotation
   */
  private boolean choosingRotation=false;
  
  /**
   * if the value is null then no patch was chosen, or else it takes the chosen patch as a value
   */
  private Patch chosenPatch=null;
  
  /**
   * The list that represents the circle of patches around the time board
   */
  private final ArrayList<Patch> patches = new ArrayList<>();

  /**
   * The position of the neutral token in the patch circle
   */
  private int NTPos;

  /**
   * The game mode which is either 1 for simple or 2 for normal
   */
  private int mode;

  /**
   * Represents the player who wins the 7x7 square
   */
  private Player bonusSquare7Owner;

  /**
   * The first player of the game
   */
  private Player player1;

  /**
   * The second player of the game
   */
  private Player player2;

  /**
   * The first player that arrives to the final position of the time board We need
   * this attribute to define the winner in case of a tie
   */
  private Player firstFinisher;

  /**
   * Represents the player who has the turn to play, it's 1 for the first player
   * and 2 for the second player
   */
  private int turn = 1;
  
  private int version;

  /**
   * Constructs a Game with the specified player1 and player2 objects
   * 
   * 
   * @param player1 The first player
   * @param player2 The second player
   */
  public SimpleGameData(Player player1, Player player2) {
    Objects.requireNonNull(player1);
    Objects.requireNonNull(player2);
    this.player1 = player1;
    this.player2 = player2;
    bonusSquare7Owner = null;
    version = chooseVersion();
    if (version==1) {
      mode = chooseMode();
    }
    else {
      mode = 2;
    }
    NTPos = 0;
    if (mode == 1) {
      initSimplePatches();
    } else if (mode == 2) {
      initPatches();
    }
    Collections.shuffle(patches);
    int i = 5;
    while (i < 54) {
      timeBoard[i] = 1;
      i += 6;
    }
    initSquares(mode);
  }

  /**
   * Puts squares on time board
   * 
   * @param mode The game mode
   */
  private void initSquares(int mode) {
    if (mode == 2) {
      timeBoard[20] = 2;
      timeBoard[26] = 2;
      timeBoard[32] = 2;
      timeBoard[44] = 2;
      timeBoard[50] = 2;
    }

  }

  /**
   * Initializes all the patches of the game in simple mode
   */
  private void initSimplePatches() {
    for (int i = 0; i < 40; i++) {
      if (i % 2 == 0) {
        patches.add(new Patch(1, 3, 4, 2, 2, new int[][] { { 1, 1 }, { 1, 1 } }));
      } else {
        patches.add(new Patch(0, 2, 2, 2, 2, new int[][] { { 1, 1 }, { 1, 1 } }));
      }
    }
  }

  
  /**
   * Returns the player number 1 of the game
   * @return player number 1
   */
  public Player getPlayer1() {
    return player1;
  }
  
  /**
   * Returns the player number 2 of the game
   * @return player number 2
   */
  public Player getPlayer2() {
    return player2;
  }
  
  /**
   * Returns the mode of the game
   * @return game mode
   */
  public int getMode() {
    return mode;
  }
  
  /**
   * Sets the value of the boolean notEnoughBut
   * @param boolean value
   */
  public void setNotEnoughBut(boolean b) {
    notEnoughBut=b;
  }
  
  /**
   * Returns the value of the boolean notEnoughBut
   * @return boolean value
   */
  public boolean getNotEnoughBut() {
    return notEnoughBut;
  }
  
  
  /**
   * Returns the value of the boolean choosingRotation
   * @return boolean value
   */
  public boolean getChoosingRotation() {
    return choosingRotation;
  }
  
  /**
   * Sets the value of the boolean choosingRotation
   * @param boolean value
   */
  public void setChoosingRotation(boolean b) {
    choosingRotation=b;
  }
  
  
  /**
   * Returns the value of the patch that the player has chosen
   * @return chosen patch
   */
  public Patch getChosenPatch() {
    return chosenPatch;
  }
  
  /**
   * Sets the value of the patch that the player has chosen
   * @param chosen patch
   */
  public void setChosenPatch(Patch p) {
    Objects.requireNonNull(p);
    chosenPatch=p;
  }
  
  /**
   * Returns the value of the player to finish the game
   * @return first finisher
   */
  public Player getFirstFinisher() {
    return firstFinisher;
  }
  
  /**
   * Initializes all the patches of the game in normal mode
   */
  private void initPatches() {
    initPatches0Buttons();
    initPatches1Button();
    initPatches2Buttons();
    initPatches3Buttons();
  }

  /**
   * Initializes all the patches with one button in normal mode
   */
  private void initPatches1Button() {

    patches.add(new Patch(1, 3, 4, 4, 2, new int[][] { { 1, 0 }, { 1, 0 }, { 1, 1 }, { 1, 0 } }));
    patches.add(new Patch(1, 0, 3, 3, 4, new int[][] { { 0, 0, 1, 0 }, { 1, 1, 1, 1 }, { 0, 0, 1, 0 } }));
    patches.add(new Patch(1, 1, 4, 3, 5, new int[][] { { 0, 0, 1, 0, 0 }, { 1, 1, 1, 1, 1 }, { 0, 0, 1, 0, 0 } }));
    patches.add(new Patch(1, 5, 3, 4, 3, new int[][] { { 0, 1, 0 }, { 1, 1, 1 }, { 1, 1, 1 }, { 0, 1, 0 } }));
    patches.add(new Patch(1, 4, 2, 3, 2, new int[][] { { 1, 0 }, { 1, 0 }, { 1, 1 } }));
    patches.add(new Patch(1, 1, 5, 2, 4, new int[][] { { 1, 0, 0, 1 }, { 1, 1, 1, 1 } }));
    patches.add(new Patch(1, 3, 2, 3, 2, new int[][] { { 0, 1 }, { 1, 1 }, { 1, 0 } }));
    patches.add(new Patch(1, 7, 1, 1, 5, new int[][] { { 1, 1, 1, 1, 1 } }));

    patches.add(new Patch(1, 3, 3, 1, 4, new int[][] { { 1, 1, 1, 1 } }));
    patches.add(new Patch(1, 2, 3, 2, 4, new int[][] { { 0, 1, 1, 1 }, { 1, 1, 0, 0 } }));
  }

  /**
   * Initializes all the patches with two buttons in normal mode
   */
  private void initPatches2Buttons() {
    patches.add(new Patch(2, 10, 3, 4, 2, new int[][] { { 1, 0 }, { 1, 0 }, { 1, 0 }, { 1, 1 } }));
    patches.add(new Patch(2, 7, 2, 3, 4, new int[][] { { 1, 0, 0, 0 }, { 1, 1, 1, 1 }, { 1, 0, 0, 0 } }));
    patches.add(new Patch(2, 5, 5, 3, 3, new int[][] { { 0, 0, 1 }, { 1, 1, 1 }, { 0, 0, 1 } }));
    patches.add(new Patch(2, 5, 4, 3, 3, new int[][] { { 0, 1, 0 }, { 1, 1, 1 }, { 0, 1, 0 } }));
    patches.add(new Patch(2, 3, 6, 3, 3, new int[][] { { 1, 1, 0 }, { 0, 1, 1 }, { 1, 1, 0 } }));
    patches.add(new Patch(2, 7, 4, 4, 2, new int[][] { { 1, 0 }, { 1, 1 }, { 1, 1 }, { 1, 0 } }));
    patches.add(new Patch(2, 6, 5, 2, 2, new int[][] { { 1, 1 }, { 1, 1 } }));
    patches.add(new Patch(2, 4, 6, 2, 3, new int[][] { { 0, 0, 1 }, { 1, 1, 1 } }));
  }

  /**
   * Initializes all the patches with three buttons in normal mode
   */
  private void initPatches3Buttons() {
    patches.add(new Patch(3, 10, 5, 4, 2, new int[][] { { 1, 1 }, { 1, 1 }, { 0, 1 }, { 0, 1 } }));

    patches.add(new Patch(3, 8, 6, 3, 3, new int[][] { { 0, 0, 1 }, { 1, 1, 1 }, { 1, 1, 0 } }));

    patches.add(new Patch(3, 10, 4, 3, 3, new int[][] { { 0, 0, 1 }, { 0, 1, 1 }, { 1, 1, 0 } }));
    patches.add(new Patch(3, 7, 6, 2, 3, new int[][] { { 1, 1, 0 }, { 0, 1, 1 } }));
  }

  /**
   * Initializes all the patches with zero buttons in normal mode
   */
  private void initPatches0Buttons() {
    patches.add(new Patch(0, 2, 2, 1, 3, new int[][] { { 1, 1, 1 } }));
    patches.add(new Patch(0, 2, 1, 1, 2, new int[][] { { 1, 1 } }));
    patches.add(new Patch(0, 1, 3, 2, 2, new int[][] { { 1, 1 }, { 0, 1 } }));
    patches.add(new Patch(0, 2, 3, 3, 3, new int[][] { { 1, 1, 1 }, { 0, 1, 0 }, { 1, 1, 1 } }));
    patches.add(new Patch(0, 2, 1, 4, 3, new int[][] { { 0, 1, 0 }, { 1, 1, 0 }, { 0, 1, 1 }, { 0, 1, 0 } }));
    patches.add(new Patch(0, 2, 2, 3, 2, new int[][] { { 1, 1 }, { 1, 1 }, { 0, 1 } }));
    patches.add(new Patch(0, 1, 2, 3, 2, new int[][] { { 1, 1 }, { 1, 0 }, { 1, 1 } }));
    patches.add(new Patch(0, 1, 2, 4, 3, new int[][] { { 0, 1, 1 }, { 0, 1, 0 }, { 0, 1, 0 }, { 1, 1, 0 } }));
    patches.add(new Patch(0, 4, 2, 2, 4, new int[][] { { 1, 1, 1, 0 }, { 0, 1, 1, 1 } }));
    patches.add(new Patch(0, 2, 2, 2, 3, new int[][] { { 1, 1, 1 }, { 0, 1, 0 } }));
    patches.add(new Patch(0, 3, 1, 2, 2, new int[][] { { 0, 1 }, { 1, 1 } }));
  }

  
  /**
   * Returns the value of the game version that the player has chosen
   * @return game version
   */
  public int getVersion() {
    return version;
  }
  
  /**
   * Returns the player that has the bonus square
   * @return player with the bonus 7x7 square
   */
  public Player getBonusSquare7Owner() {
    return bonusSquare7Owner;
  }
  
  /**
   * Sets the attribute bonusSquare7Owner to the player who owns it
   * @param player that owns bonus square
   */
  public void setBonusSquare7Owner(Player player) {
    Objects.requireNonNull(player);
    bonusSquare7Owner=player;
  }
  
  
  /**
   * Lets the player choose between the console version of the game and the graphic version
   * @return 1 if the player chooses the console version and 2 if he chooses the graphic one
   */
  private int chooseVersion() {
    boolean validInput = false;
    int m=0;
    System.out.println("Welcome to PatchWork!!!\n\n");
    while (!validInput) {
      System.out.println("Choose which version of the game you want to play :\n");
      System.out.println("1- Console version");
      System.out.println("2- Graphic version");
      Scanner sc = new Scanner(System.in);
      
      try {
        m = sc.nextInt();
        if (m != 2 && m != 1) {
          System.out.println("Choose either the number 1 or 2!!!\n");
          continue;
        }
        validInput = true;
      } catch (InputMismatchException e) {
        System.out.println("Invalid input! Please enter a number!!!\n");
        sc.next();
      }
    }
    return m;
  }
  
  /**
   * Returns the array representing the timeboard
   * @return timeboard
   */
  public int[] getTimeBoard() {
    return timeBoard;
  }
  
  /**
   * Switch turns between players
   */
  public void switchTurn() {
    if (turn == 1) {
      turn = 2;
    } else {
      turn = 1;
    }
  }

  /**
   * Returns the player who has the turn to play
   * 
   * @return Player The player who has the turn to play
   */
  public Player getTurnPlayer() {
    if (turn == 1) {
      return player1;
    } else {
      return player2;
    }
  }

  /**
   * Returns the string representation of the time board and its different
   * components, 1 represents the first player, 2 represents the second player B
   * represents a button and S represents a 1x1 square
   * 
   * @return String The string representing the time board
   */
  private String printTimeBoard() {
    var builder = new StringBuilder();
    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 9; j++) {
        builder = buildBoardBox(builder, i, j);
      }
      builder.append("|\n");
    }
    return builder.toString();
  }

  /**
   * Returns the string representation of a box of the time board
   * 
   * @param builder used to build the string
   * @param i       row position of the time board
   * @param j       column position of the time board
   * @return StringBuilder
   */
  private StringBuilder buildBoardBox(StringBuilder builder, int i, int j) {
    builder.append("|");
    if (player1.getPosition() == (9 * i + j)) {
      builder.append("1");
    } else {
      builder.append("_");
    }
    if (player2.getPosition() == (9 * i + j)) {
      builder.append("2");
    } else {
      builder.append("_");
    }
    if (timeBoard[9 * i + j] == 1) {
      builder.append("B");
    } else if (timeBoard[9 * i + j] == 2) {
      builder.append("S");
    } else {
      builder.append("_");
    }
    return builder;
  }

  

  /**
   * Gets the chosen piece using its position
   * 
   * @param n The position of the patch starting from the neutral time token
   * @return The chosen patch
   */
  public Patch choosePiece(int n) {
    Patch chosenPiece = patches.get(NTPos + n);
    patches.remove(NTPos + n);
    NTPos += n;
    return chosenPiece;
  }

  /**
   * Returns the patch circle around the time board as a list
   * @return patch circle
   */
  public ArrayList<Patch> getPatches() {
    return patches;
  }
  
  /**
   * Returns the position of the neutral token
   * @return neutral token position
   */
  public int getNTPos() {
    return NTPos;
  }
  
  /**
   * Allows the player to buy a patch
   * 
   * @param choice Number of the chosen piece out of the three
   * @return The chosen piece
   */
  public Patch buyPatch(int choice) {
    Patch chosenPiece;
    if (choice == 1 && patches.get(NTPos).price() <= getTurnPlayer().getButtons()) {
      chosenPiece = choosePiece(0);
    } else if (choice == 2 && patches.get(NTPos + 1).price() <= getTurnPlayer().getButtons()) {
      chosenPiece = choosePiece(1);
    } else if (choice == 3 && patches.get(NTPos + 2).price() <= getTurnPlayer().getButtons()) {
      chosenPiece = choosePiece(2);
    } else {
      System.out.println("\nYou don't have enough buttons!!!\n");
      System.out.println("Try again :\n");
      return null;
    }
    getTurnPlayer().addPatchOwned(chosenPiece);
    getTurnPlayer().setButtons(getTurnPlayer().getButtons() - chosenPiece.price());
    return chosenPiece;
  }

  
  /**
   * Increases the player's score by counting the number of buttons on his quilt
   * board
   */
  public void updateButtonScore() {
    int buttonsCollected = 0;
    for (Patch patch : getTurnPlayer().getPatchesOwned()) {
      buttonsCollected += patch.buttons();
    }
    if (version==1) {
      System.out.println("You won " + buttonsCollected + " extra buttons!!!");
    }
    

    int buttonsCurrentPlayer = getTurnPlayer().getButtons() + buttonsCollected;
    getTurnPlayer().setButtons(buttonsCurrentPlayer);
  }

  

  

  /**
   * Checks if the game ended
   * 
   * @return boolean
   */
  public boolean endGame() {
    if (player1.getPosition() == 53 && player2.getPosition() == 53) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Assigns the player who first arrived to the end of the time board to the
   * firstFinisher attribute
   */
  public void tieWinner() {
    if (player1.getPosition() == 53 && player2.getPosition() < 53) {
      firstFinisher = player1;
    } else if (player1.getPosition() < 53 && player2.getPosition() == 53) {
      firstFinisher = player2;
    }
  }

  
  

  /**
   * Allows the player to choose between the game's mode simple or normal by
   * typing 1 or 2
   * 
   * @return the number typed
   */
  private int chooseMode() {
    Scanner scanner = new Scanner(System.in);
    int m = 0;
    boolean validInput = false;
    while (!validInput) {
      System.out.println("Choose a game mode:\n");
      System.out.println("1- Simple\n");
      System.out.println("2- Normal\n");
      try {
        m = scanner.nextInt();
        if (m != 2 && m != 1) {
          System.out.println("Choose either the number 1 or 2!!!\n");
          continue;
        }
        validInput = true;
      } catch (InputMismatchException e) {
        System.out.println("Invalid input! Please enter a number!!!\n");
        scanner.next();
      }
    }

    return m;
  }

  @Override
  public String toString() {
    return "\n\n\n\n\n=====Time Board=====\n\n" + printTimeBoard() + "\n\n=====player1 Infos=====\n\n"
        + player1.toString() + "\n\n=====player2 Infos=====\n\n" + player2.toString();

  }

}
