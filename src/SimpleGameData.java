
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Objects;

/**
 * A class representing a game of Patchwork, which can be played on the terminal
 * This class provides all the necessary methods to make the game work
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

  private boolean notEnoughBut=false;
  private boolean choosingRotation=false;
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

  /*public int getVersion() {
    return version;
  }*/
  
  public Player getPlayer1() {
    return player1;
  }
  
  public Player getPlayer2() {
    return player2;
  }
  
  public int getMode() {
    return mode;
  }
  
  public void setNotEnoughBut(boolean b) {
    notEnoughBut=b;
  }
  
  public boolean getNotEnoughBut() {
    return notEnoughBut;
  }
  
  public boolean getChoosingRotation() {
    return choosingRotation;
  }
  
  public void setChoosingRotation(boolean b) {
    choosingRotation=b;
  }
  
  public Patch getChosenPatch() {
    return chosenPatch;
  }
  
  public void setChosenPatch(Patch p) {
    chosenPatch=p;
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

  public int getVersion() {
    return version;
  }
  
  public Player getBonusSquare7Owner() {
    return bonusSquare7Owner;
  }
  
  public void setBonusSquare7Owner(Player player) {
    bonusSquare7Owner=player;
  }
  
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
   * Gets the player's choice out of the three patches in front of the neutral
   * time token
   * 
   * @param scanner Reads input of the current player
   * @return The choice made by the player
   */
  /*private int getPlayerChoice(Scanner scanner) {
    int choice = 0;
    boolean validInput = false;
    while (!validInput) {
      System.out.println("Which patch do you want to buy?\n\n" + "1- " + patches.get(NTPos).toString() + "\n2- "
          + patches.get(NTPos + 1).toString());
      System.out.println();
      System.out.println("3- " + patches.get(NTPos + 2).toString());
      System.out.println("\n\n");
      System.out.println("Type the number of the patch you want or type another " + "number to skip");
      try {
        choice = scanner.nextInt();
        validInput = true;
      } catch (InputMismatchException e) {
        System.out.println("Invalid input! Please enter a number!!!\n");
        scanner.next();
      }
    }
    return choice;
  }*/

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

  
  public ArrayList<Patch> getPatches() {
    return patches;
  }
  
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
   * Moves the player's time token if he skips his turn
   * 
   * @param scanner Read the player's input
   */
  /*public void moveAfterSkipping() {
    int posCurrentPlayer = getTurnPlayer().getPosition();
    Player currentPlayer = getTurnPlayer();
    int buttonsAdded = 0;
    int posOtherPlayer = currentPlayer.equals(player1) ? player2.getPosition() : player1.getPosition();
    int i;
    for (i = posCurrentPlayer + 1; i <= posOtherPlayer + 1 && i < 54; i++) {
      if (timeBoard[i] == 1) {
        updateButtonScore();
      }
      if (timeBoard[i] == 2) {
        System.out.println("You moved over the square, you have to put it in your board\n");
        System.out.println("\n\nBoard : \n" + getTurnPlayer().getBoard());
        getTurnPlayer().getBoard().procedureToPutPatch(new Patch(0, 0, 0, 1, 1, new int[][] { { 1 } }), mode);
        timeBoard[i] = 0;
      }
      buttonsAdded++;
    }
    currentPlayer.setPosition(i - 1);
    currentPlayer.setButtons(buttonsAdded + currentPlayer.getButtons());
    switchTurn();
  }*/

  /**
   * Moves the player's time token if he buys a patch
   * 
   * @param chosenPiece The bought patch
   * @param scanner     Read the player's input
   */
  /*private void moveAfterBuying(Patch chosenPiece) {
    int j2;
    int posCurrentPlayer = getTurnPlayer().getPosition();
    for (j2 = posCurrentPlayer + 1; j2 <= chosenPiece.timeStep() + posCurrentPlayer && j2 < 54; j2++) {
      if (timeBoard[j2] == 1) {
        updateButtonScore();
      }
      if (timeBoard[j2] == 2) {
        System.out.println("You moved over the square, you have to put it in your board\n");
        System.out.println("\n\nBoard : \n" + getTurnPlayer().getBoard()+"\n\n");
        getTurnPlayer().getBoard().procedureToPutPatch(new Patch(0, 0, 0, 1, 1, new int[][] { { 1 } }), mode);
      }
    }
    getTurnPlayer().setPosition(j2 - 1);
  }*/

  /**
   * Increases the player's score by counting the number of buttons on his quilt
   * board
   */
  public void updateButtonScore() {
    int buttonsCollected = 0;
    for (Patch patch : getTurnPlayer().getPatchesOwned()) {
      buttonsCollected += patch.buttons();
    }
    System.out.println("You won " + buttonsCollected + " extra buttons!!!");

    int buttonsCurrentPlayer = getTurnPlayer().getButtons() + buttonsCollected;
    getTurnPlayer().setButtons(buttonsCurrentPlayer);
  }

  

  /**
   * Allows the player to finish his turn after buying a patch
   * 
   * @param scanner
   * @param chosenPiece
   * @param posOtherPlayer Position of the opponent
   */
  /*public void playAfterBuy(Patch chosenPiece, int posOtherPlayer) {
    Objects.requireNonNull(chosenPiece);
    getTurnPlayer().getBoard().procedureToPutPatch(chosenPiece, mode);
    moveAfterBuying(chosenPiece);
    if (getTurnPlayer().getBoard().verifyBonus() && bonusSquare7Owner == null && mode == 2) {
      bonusSquare7Owner = getTurnPlayer();
      System.out.println("You won the Square 7 * 7 bonus!!!\n");
    }
    if (getTurnPlayer().getPosition() > posOtherPlayer) {
      switchTurn();
    }
  }*/

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
   * Announces the winner by counting the score of each player
   */
  public void winner() {
    int score1 = player1.getBoard().getNbButtons() - player1.getBoard().countScoreBlank();
    int score2 = player2.getBoard().getNbButtons() - player2.getBoard().countScoreBlank();
    if (player1.equals(bonusSquare7Owner)) {
      score1 += 7;
    }
    if (player2.equals(bonusSquare7Owner)) {
      score2 += 7;
    }
    if (score1 > score2) {
      System.out.println("The winner is player1!");
    } else if (score1 < score2) {
      System.out.println("The winner is player2!");
    } else {
      if (firstFinisher.equals(player1)) {
        System.out.println("The winner is player1!");
      } else {
        System.out.println("The winner is player2!");
      }
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
