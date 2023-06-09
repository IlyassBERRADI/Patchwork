import java.awt.Graphics2D;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

import fr.umlv.zen5.ApplicationContext;

public record ConsoleGameView() implements GameView {
  /**
   * Gets the player's choice out of the three patches in front of the neutral
   * time token
   * 
   * @param scanner Reads input of the current player
   * @return The choice made by the player
   */
  
  
  
  /**
   * Allows the player to play his turn
   */
  /*@Override
  public void play(ApplicationContext context, SimpleGameData data) {
    Scanner scanner = new Scanner(System.in);
    announcePlayerTurn(data);  
    boolean madeChoice = false;
    int posOtherPlayer = data.getTurnPlayer().equals(data.getPlayer1()) ? data.getPlayer2().getPosition() : data.getPlayer1().getPosition();
    Patch chosenPiece = null;
    while (!madeChoice) {
      int choice = getPlayerChoice(context, data);
      if (choice == 1 || choice == 2 || choice == 3) {
        chosenPiece = buyPatch(choice, data);
        if (chosenPiece != null) {
          madeChoice = true;
        }
      } else {
        moveAfterSkipping(data);
        return;
      }
    }
    playAfterBuy(chosenPiece, posOtherPlayer, data);
  }*/
  
  /**
   * Moves the player's time token if he buys a patch
   * 
   * @param chosenPiece The bought patch
   * @param scanner     Read the player's input
   */
  private void moveAfterBuying(Patch chosenPiece, SimpleGameData data) {
    int j2;
    int posCurrentPlayer = data.getTurnPlayer().getPosition();
    for (j2 = posCurrentPlayer + 1; j2 <= chosenPiece.timeStep() + posCurrentPlayer && j2 < 54; j2++) {
      if (data.getTimeBoard()[j2] == 1) {
        data.updateButtonScore();
      }
      if (data.getTimeBoard()[j2] == 2) {
        System.out.println("You moved over the square, you have to put it in your board\n");
        System.out.println("\n\nBoard : \n" + data.getTurnPlayer().getBoard()+"\n\n");
        procedureToPutPatch(new Patch(0, 0, 0, 1, 1, new int[][] { { 1 } }), data.getMode(), data.getTurnPlayer().getBoard());
      }
    }
    data.getTurnPlayer().setPosition(j2 - 1);
  }
  
  /**
   * Puts the patch in a certain position chosen by the player
   * 
   * @param scanner     Reads the player's input
   * @param chosenPiece The piece the player wants to put
   * @param mode        The game mode
   */
  public void procedureToPutPatch(Patch chosenPiece, int mode, QuiltBoard board) {
    int x = 0, y = 0;
    Patch pieceToPut;
    do {
      x = getInputX();
      y = getInputY();
      if (mode == 2) {
        pieceToPut = chooseRotation(chosenPiece, board);
      } else {
        pieceToPut = chosenPiece;
      }
    } while (!board.ableToPut(x, y, pieceToPut));
    board.putPatch(x, y, pieceToPut);
    board.addButtons(pieceToPut);
  }

  /**
   * Gets row position typed in by the player
   * 
   * @param scanner
   * @return Row position
   */
  public int getInputX() {
    Scanner scanner = new Scanner(System.in);
    int x = 0;
    boolean validInput = false;
    while (!validInput) {
      System.out.println("Board :\n"+toString()+"\n\n");
      System.out.println("In which position of the quilt do you want to put the patch");
      System.out.println("Write the line number :");
      try {
        x = scanner.nextInt();
        validInput = true;
      } catch (InputMismatchException e) {
        System.out.println("Invalid input! Please enter a number!!!\n");
        scanner.next();
      }
    }
    return x;
  }

  /**
   * Gets column position typed in by the player
   * 
   * @param scanner
   * @return Column position
   */
  public int getInputY() {
    Scanner scanner = new Scanner(System.in);
    int y = 0;
    boolean validInput = false;
    while (!validInput) {
      System.out.println("Write the column number :");
      try {
        y = scanner.nextInt();
        validInput = true;
      } catch (InputMismatchException e) {
        System.out.println("Invalid input! Please enter a number!!!\n");
        scanner.next();
      }
    }
    return y;
  }

  
  /**
   * Allows the player to type in the terminal the rotation he likes to choose 0 :
   * No rotation 1 : Rotation to the right 2 : Rotation upside down 3 : Rotation
   * to the left
   * 
   * @param p       Patch to rotate
   * @param scanner Reads player's input
   * @return Patch after rotation
   */
  public Patch chooseRotation(Patch p, QuiltBoard board) {
    Scanner scanner = new Scanner(System.in);
    boolean validInput = false;
    int i = 0;
    while (!validInput) {
      System.out.println("Choose a rotation for this patch\n0: none\n"+p.printShape()+"\n\n1: right\n"+board.switchRotation(p, 1).printShape()+
          "\n\n2: down\n"+board.switchRotation(p, 2).printShape()+"\n\n3: left\n"+board.switchRotation(p, 3).printShape());
      try {
        i = scanner.nextInt();
        validInput = true;
      } catch (InputMismatchException e) {
        System.out.println("Invalid input! Please enter an number.");
        scanner.next();
      }
    }

    return board.switchRotation(p, i);
  }

  
  
  /**
   * Allows the player to buy a patch
   * 
   * @param choice Number of the chosen piece out of the three
   * @return The chosen piece
   */
  public Patch buyPatch(int choice, SimpleGameData data) {
    Patch chosenPiece;
    if (choice == 1 && data.getPatches().get(data.getNTPos()).price() <= data.getTurnPlayer().getButtons()) {
      chosenPiece = data.choosePiece(0);
    } else if (choice == 2 && data.getPatches().get(data.getNTPos() + 1).price() <= data.getTurnPlayer().getButtons()) {
      chosenPiece = data.choosePiece(1);
    } else if (choice == 3 && data.getPatches().get(data.getNTPos() + 2).price() <= data.getTurnPlayer().getButtons()) {
      chosenPiece = data.choosePiece(2);
    } else {
      System.out.println("\nYou don't have enough buttons!!!\n");
      System.out.println("Try again :\n");
      return null;
    }
    data.getTurnPlayer().addPatchOwned(chosenPiece);
    data.getTurnPlayer().setButtons(data.getTurnPlayer().getButtons() - chosenPiece.price());
    return chosenPiece;
  }
  
  /*public void draw(ApplicationContext context, SimpleGameData data) {
    announcePlayerTurn(data);
  }*/
  
  
  /**
   * Moves the player's time token if he skips his turn
   * 
   * @param scanner Read the player's input
   */
  public void moveAfterSkipping(SimpleGameData data) {
    int posCurrentPlayer = data.getTurnPlayer().getPosition();
    Player currentPlayer = data.getTurnPlayer();
    int buttonsAdded = 0;
    int posOtherPlayer = currentPlayer.equals(data.getPlayer1()) ? data.getPlayer2().getPosition() : data.getPlayer1().getPosition();
    int i;
    for (i = posCurrentPlayer + 1; i <= posOtherPlayer + 1 && i < 54; i++) {
      if (data.getTimeBoard()[i] == 1) {
        data.updateButtonScore();
      }
      if (data.getTimeBoard()[i] == 2) {
        System.out.println("You moved over the square, you have to put it in your board\n");
        System.out.println("\n\nBoard : \n" + data.getTurnPlayer().getBoard());
        procedureToPutPatch(new Patch(0, 0, 0, 1, 1, new int[][] { { 1 } }), data.getMode(), data.getTurnPlayer().getBoard());
        data.getTimeBoard()[i] = 0;
      }
      buttonsAdded++;
    }
    currentPlayer.setPosition(i - 1);
    currentPlayer.setButtons(buttonsAdded + currentPlayer.getButtons());
    data.switchTurn();
    
  }

  
  public void playAfterBuy(Patch chosenPiece, int posOtherPlayer, SimpleGameData data) {
    Objects.requireNonNull(chosenPiece);
    procedureToPutPatch(chosenPiece, data.getMode(), data.getTurnPlayer().getBoard());
    moveAfterBuying(chosenPiece, data);
    if (data.getTurnPlayer().getBoard().verifyBonus() && data.getBonusSquare7Owner() == null && data.getMode() == 2) {
      data.setBonusSquare7Owner(data.getTurnPlayer());
      System.out.println("You won the Square 7 * 7 bonus!!!\n");
    }
    if (data.getTurnPlayer().getPosition() > posOtherPlayer) {
      data.switchTurn();
    }
  }
  
  /**
   * Moves the player's time token if he buys a patch
   * 
   * @param chosenPiece The bought patch
   * @param scanner     Read the player's input
   */
  /*public void moveAfterBuying(Patch chosenPiece, SimpleGameData data) {
    int j2;
    int posCurrentPlayer = data.getTurnPlayer().getPosition();
    for (j2 = posCurrentPlayer + 1; j2 <= chosenPiece.timeStep() + posCurrentPlayer && j2 < 54; j2++) {
      if (data.getTimeBoard()[j2] == 1) {
        data.updateButtonScore();
      }
      if (data.getTimeBoard()[j2] == 2) {
        System.out.println("You moved over the square, you have to put it in your board\n");
        System.out.println("\n\nBoard : \n" + data.getTurnPlayer().getBoard()+"\n\n");
        data.getTurnPlayer().getBoard().procedureToPutPatch(new Patch(0, 0, 0, 1, 1, new int[][] { { 1 } }), mode);
      }
    }
    data.getTurnPlayer().setPosition(j2 - 1);
  }*/

  
  /**
   * Announces the player who has the turn to play
   */
  
  private void announcePlayerTurn(SimpleGameData data) {
    System.out.println("===========\n");
    if (data.getTurnPlayer().equals(data.getPlayer1())) {
      System.out.println("Its player1 turn\n");
    } else {
      System.out.println("Its player2 turn\n");
    }
    System.out.println("===========");
  }
}
